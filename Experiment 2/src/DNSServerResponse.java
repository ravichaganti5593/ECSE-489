import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class DNSServerResponse {
	
	
	//****************		//print additional records as well!!!******************
	
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
		if (dataReceived[1]  != headerBuffer.get(1) || dataReceived[0] != headerBuffer.get(0) ) {
			System.out.println("ERROR: Unexpected Response: Request and response IDs don't match" );
			System.exit(1);
		}
		
		//check QR
		if (((byte) ((dataReceived[2] >> 8) & 1)) != 1) {
			System.out.println("ERROR: Unexpected Response: Response packet is a request");
			System.exit(1);
		}
		
		//check RA for recursive queries
		if (((byte) ((dataReceived[3] >> 8) & 1)) != 1){
			System.out.println("ERROR: Server does not support recursive queries");
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
	
		System.out.println("Response received after " + RTT + " milliseconds ("	+ connectionRetries + " retries)");
		
		//Check ANCount
		int numAnswers = DNSReceivePacket.getData()[6] << 8 | DNSReceivePacket.getData()[7];
		System.out.println("***Answer  Section  (" + numAnswers + "  records)***");
		
		int nextAnswerIndex = 0;
		int currentAnswerIndex = 0;
		for (int i = 0; i < numAnswers; i++) {
			//CHANGE THIS ******************* to +10 and + 9
			short RDLength = (short) ((receivedAnswer[(nextAnswerIndex + 12) - 2] << 8) | (receivedAnswer[(nextAnswerIndex + 12) - 1])); 
																													
			currentAnswerIndex = nextAnswerIndex;
			nextAnswerIndex += (12 + RDLength);
			
			byte authorityBit = (byte) (dataReceived[3] & 0x4);		//check with AA data and 4 octets
			boolean isAuthority = authorityBit == 4 ? true : false;
			 
			DNSRecords(Arrays.copyOfRange(receivedAnswer, currentAnswerIndex, nextAnswerIndex), RDLength, isAuthority);
		}
			
	}
	
	public void DNSRecords(byte[] data, int RDLength, boolean authority) {
		
		//retrieve values from response for Type, Class, TTL, and RDLength
		short Type = (short) (data[2] << 8 | data[3]);
		short Class = (short) (data[4] << 4 | data[5]);
		int TTL = data[6] << 24 | data[7] << 16 | data[8] << 8 | data[9];
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
			TypeNS();
		}
		
		else if (Type == 0x000f) {	//TYPE: "MX"
			TypeMX();
		}
		
		else if (Type == 0x0005) {	//TYPE: "CNAME"
			TypeCNAME();
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
	public void TypeNS() {
		//to do
	}
	
	public void TypeMX() {
		//to do
	}
	
	public void TypeCNAME() {
		//to do
	}
	
}
