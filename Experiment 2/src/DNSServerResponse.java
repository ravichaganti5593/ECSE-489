import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class DNSServerResponse {
	
	DatagramPacket DNSReceivePacket;
	ByteBuffer headerBuffer;
	ByteBuffer questionBuffer;
	ByteBuffer answerBuffer;
	ByteBuffer packetBuffer;
	long RTT = 0;
	int connectionRetries = 0;
	 
	String DOMAIN = "";
	String SERVERIPADDRESS = "";
	String TYPE = "A";
	
	public DNSServerResponse(DatagramPacket DNSReceivePacket, ByteBuffer headerBuffer, ByteBuffer questionBuffer, ByteBuffer answerBuffer, ByteBuffer packetBuffer, long RTT, int connectionRetries, String DOMAIN, String SERVERIPADDRESS, String TYPE) {
		this.DNSReceivePacket = DNSReceivePacket;
		this.headerBuffer = headerBuffer;
		this.questionBuffer = questionBuffer;
		this.answerBuffer = answerBuffer;
		this.packetBuffer = packetBuffer;
		this.RTT = RTT;
		this.connectionRetries = connectionRetries;
		this.DOMAIN = DOMAIN;
		this.SERVERIPADDRESS = SERVERIPADDRESS;
		this.TYPE = TYPE;
	}
	
	
	public void outputBehavior() { 
		
		
		byte[] dataReceived = DNSReceivePacket.getData();
		

		int answerIndex = headerBuffer.capacity() + questionBuffer.capacity();
		byte[] receivedAnswer = Arrays.copyOfRange(dataReceived, answerIndex, dataReceived.length);
		
		System.out.println("DnsClient sending request for " + DOMAIN);
		System.out.println("Server: " + SERVERIPADDRESS);
		System.out.println("Request type: " + TYPE);
		
		//check ID
		if (dataReceived[1]  != headerBuffer.get(1) && dataReceived[0] != headerBuffer.get(0) ) {
			System.out.println("ERROR: IDs mismatch for request and response." );
			System.exit(1);
		}
		
		//check QR
		if (((byte) ((dataReceived[2] >> 8) & 1)) != 1) {
			System.out.println("ERROR: Response packet is not response but request.");
			System.exit(1);
		}
		
		//check RA for recursive queries
		if (((byte) ((dataReceived[3] >> 8) & 1)) != 1){
			System.out.println("ERROR: Cannot have queries that are recursive");
			System.exit(1);
		}
		 
		//Check for RCode error types
		byte RCode = (byte)(dataReceived[3] << 4);
		
		switch (RCode) {
			case 1:
				System.out.println("ERROR RCODE 1: the name server was unable to interpret the query");
				System.exit(1);
		
			case 2:
				System.out.println("ERROR RCODE 2: the name server was unable to process this query due to a problem with the name server");
				System.exit(1);
				
			case 3:
				System.out.println("Error RCODE 3: the domain name referenced in the query does not exist");
				System.exit(1);
				
			case 4:
				System.out.println("Error RCODE 4: the name server does not support the requested kind of query");
				System.exit(1);
				
			case 5:
				System.out.println("Error RCODE 5: the name server refuses to perform the requested operation for policy reasons");
				System.exit(1);
		}
	
		System.out.println("Response received after " + RTT + " ms ("	+ connectionRetries + " retries)");
		
		//Check ANCount
		int answerRecords = DNSReceivePacket.getData()[6] << 8 | DNSReceivePacket.getData()[7];
		//Check ARCount 
		int additionalRecords = DNSReceivePacket.getData()[10] << 8 | DNSReceivePacket.getData()[11];
		
		if (additionalRecords == 0 && answerRecords == 0) {
			System.out.println("NOT FOUND");
			System.exit(1);
		}
		
		System.out.println("***Answer  Section  (" + answerRecords + "  records)***");
		
		int nextAnswerIndex = 0;
		int currentAnswerIndex = 0;
		for (int i = 0; i < answerRecords; i++) {
			short RDLength = (short) ((receivedAnswer[nextAnswerIndex + 10] << 8) | (receivedAnswer[nextAnswerIndex + 11])); 
																													
			currentAnswerIndex = nextAnswerIndex;
			nextAnswerIndex = nextAnswerIndex + (12 + RDLength);
			
			byte authorityBit = (byte) (dataReceived[3] & 0x4);		//check with AA data and 4 octets
			boolean isAuthority = authorityBit == 4 ? true : false;
			 
			DNSRecords(Arrays.copyOfRange(receivedAnswer, currentAnswerIndex, nextAnswerIndex), RDLength, isAuthority);		
			
		}	
		

		System.out.println("***Additional Section (" + additionalRecords + " records)***");

		
			
	}
	
	public void DNSRecords(byte[] data, int RDLength, boolean authority) {
		
		//retrieve values from response for Type, Class, TTL, and RDLength
		short Type = (short) (data[2] << 8 | data[3]);
		short Class = (short) (data[4] << 4 | data[5]);
		int TTL = Math.abs(data[6] << 24 | data[7] << 16 | data[8] << 8 | data[9]);
		String authorityValue = authority ? "auth" : "nonauth";
		
		if (Class != 0x0001) {
			System.out.println("Error: CLASS value is not 0x0001");
			System.exit(1);
		}
		
		int startIndex = 12; //bytes up to rdata
		
		if (Type == 0x0001) {		//TYPE: "A", 		
			TypeA(data, startIndex, RDLength, TTL, authorityValue);
		}
		
		else if (Type == 0x0002) {	//TYPE: "NS"
			TypeNS(data, startIndex, RDLength, TTL, authorityValue);
		}
		
		else if (Type == 0x000f) {	//TYPE: "MX"
			TypeMX(data, startIndex, RDLength, TTL, authorityValue);
		}
		
		else if (Type == 0x0005) {	//TYPE: "CNAME"
			TypeCNAME(data, startIndex, RDLength, TTL, authorityValue);
		}
	}
	
	
	//for Type A: for an A (IP address) record, then RDATA is the IP address (four octets)
	public void TypeA(byte[] data, int startIndex, int RDLength, int TTL, String authorityValue) {
		ByteBuffer IPAddress = ByteBuffer.allocate(4); //RDATA has IP address in 4 octets
		for (int i = startIndex; i < startIndex + RDLength; i++) {		//need to understand this part
			IPAddress.put(data[i]);
		}
		
		InetAddress address = null;
		try {
			address = InetAddress.getByAddress(IPAddress.array());
		}
		
		catch (UnknownHostException exception) {
			System.out.println("Error: Unknown host exception ->" + exception);
		}
		
		System.out.println("IP	" + address.getHostAddress() + "	" + TTL + "	" + authorityValue); 
	}
	
	
	//for Type NS: name of the server specified using the same format as the QNAME field
	public void TypeNS(byte[] data, int startIndex, int RDLength, int TTL, String authorityValue) {

		ByteBuffer buffer = ByteBuffer.allocate(RDLength);
		for (byte b : Arrays.copyOfRange(data, startIndex, data.length)) {
			if (String.format("%02X", b).equals("C0")) {
				break;
			}
			buffer.put(b);
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < buffer.array().length; i++) {
			if (buffer.array()[i] == 0) {
				break;
			}
			char[] chars = new char[buffer.array()[i++]];
			for (int j = 0; j < chars.length; j++) {
				chars[j] = (char) buffer.array()[i++];
			}
			
			builder.append(chars);
			
			if (i < buffer.array().length - 1) {
				builder.append(".");
			}
			i--;
		}

		// StringBuilder builder = new StringBuilder();
		// builder.append(chars);
		String first = builder.toString();
		
		if (data[0] == 0xc0) {
			int pointer = data[1];
			int offset = pointer - 12;
			String secondName = DOMAIN.substring(offset);
			String ns = (first.concat(secondName));
			System.out.println("NS	" + ns + "	" + TTL + "	" + authorityValue);
		} 
		
		else {
			String fullNameServer = first.concat(DOMAIN);
			System.out.println("NS	" + fullNameServer + "	" + TTL + "	" + authorityValue);
		}
	}
	
	public void TypeMX(byte[] data, int startIndex, int RDLength, int TTL, String authorityValue) {
		short preference = (short) (data[12] << 8 | data[13]);

		ByteBuffer buffer = ByteBuffer.allocate(RDLength - 2);
		StringBuilder builder = new StringBuilder();

		for (byte b : Arrays.copyOfRange(data, startIndex + 2, data.length)) {
			if (String.format("%02X", b).equals("C0")) {
				break;
			}
			buffer.put(b);
		}

		for (int i = 0; i < buffer.array().length; i++) {
			if (buffer.array()[i] == 0) {
				break;
			}
			char[] chars = new char[buffer.array()[i++]];
			for (int j = 0; j < chars.length; j++) {
				chars[j] = (char) buffer.array()[i++];
			}
			builder.append(chars);
			if (i < buffer.array().length - 1) {
				builder.append(".");
			}
			i--;
		}

		String first = builder.toString();
		
		if (data[data.length - 2] == 0xc0) {
			int pointer = data[data.length - 1];
			int offset = pointer - 12;
			String secondName = DOMAIN.substring(offset);
			String mx = (first.concat(secondName));
			System.out.println("MX	" + mx + "	" + preference + "	" + TTL + "	" + authorityValue);
		} 
		
		else {
			String fullMailServer = first.concat(DOMAIN);
			System.out.println("MX	" + fullMailServer + "	" + preference + "	" + TTL + "	" + authorityValue);
		}
	}
	
	public void TypeCNAME(byte[] data, int startIndex, int RDLength, int TTL, String authorityValue) {
		
		if (data[startIndex] == 0xc0) {
			int pointer = data[startIndex + 1];
			int offset = pointer - 12;
			String cname = DOMAIN.substring(offset);
			System.out.println("CNAME	" + cname + "	" + TTL + "	"	+ authorityValue);
		} 
		
		else {
			ByteBuffer buffer = ByteBuffer.allocate(RDLength);
			StringBuilder builder = new StringBuilder();

			for (byte b : Arrays.copyOfRange(data, startIndex, data.length - 1)) {	//-1 to get rid of 0 in the end
				if (String.format("%02X", b).equals("C0")) {
					break;
				}
				buffer.put(b);
			}
			
			for (int i = 0; i < buffer.array().length - 2; i++) {
				if (buffer.array()[i] == 0) {
					break;
				}
				char[] chars = new char[buffer.array()[i++]];
				for (int j = 0; j < chars.length; j++) {
					chars[j] = (char) buffer.array()[i++];
				}
				
				builder.append(chars);
				
				if (i < buffer.array().length - 1) {
					builder.append(".");
				}
				i--;
			}
			String cname = builder.toString();

			System.out.println("CNAME	" + cname + "	" + TTL + "	" + authorityValue);
		}
	}
	
}
