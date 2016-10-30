import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class DNSServerResponse {
	
	DatagramPacket ReceivedPacket;
	ByteBuffer headerPacketBuffer;
	ByteBuffer questionPacketBuffer;
	ByteBuffer answerPacketBuffer;
	ByteBuffer totalPacketBuffer;
	long RTT = 0;
	int connectionRetries = 0;
	 
	String DOMAIN = "";
	String SERVERIPADDRESS = "";
	String TYPE = "A";
	
	public DNSServerResponse(DatagramPacket DNSReceivePacket, ByteBuffer headerBuffer, ByteBuffer questionBuffer, ByteBuffer answerBuffer, ByteBuffer packetBuffer, long RTT, int connectionRetries, String DOMAIN, String SERVERIPADDRESS, String TYPE) {
		this.ReceivedPacket = DNSReceivePacket;
		this.headerPacketBuffer = headerBuffer;
		this.questionPacketBuffer = questionBuffer;
		this.answerPacketBuffer = answerBuffer;
		this.totalPacketBuffer = packetBuffer; 
		this.RTT = RTT;
		this.connectionRetries = connectionRetries;
		this.DOMAIN = DOMAIN;
		this.SERVERIPADDRESS = SERVERIPADDRESS;
		this.TYPE = TYPE;
	}
	
	//will print all the required output as described in the lab experiment guidelines
	public void outputBehavior() { 
	
		byte[] dataReceived = ReceivedPacket.getData();
		
		byte[] answerFromDataReceived = Arrays.copyOfRange(dataReceived, headerPacketBuffer.capacity() + questionPacketBuffer.capacity(), dataReceived.length);
		
		System.out.println("DnsClient sending request for " + DOMAIN);
		System.out.println("Server: " + SERVERIPADDRESS);
		System.out.println("Request type: " + TYPE);
		
		checkDataReceived(dataReceived);
	
		System.out.println("Response received after " + RTT + " ms ("	+ connectionRetries + " retries)");
		
		//Check ANCount
		int answerRecords = ReceivedPacket.getData()[6] << 8 | ReceivedPacket.getData()[7];
		//Check ARCount 
		int additionalRecords = ReceivedPacket.getData()[10] << 8 | ReceivedPacket.getData()[11];
		
		if (additionalRecords == 0 && answerRecords == 0) {
			System.out.println("NOT FOUND: no answer records or additional records found in received packet from server"); 
			System.exit(1);
		}
		
		System.out.println("***Answer  Section  (" + answerRecords + "  records)***");
		
		
		/*
		 * to print all the values in all the answer records for A, NS, MX and CNAME
		 */
		int followingAnswerResult = 0, answerResult = 0;
		
		for (int i = 0; i < answerRecords; i++) {
			short RDLength = (short) ((answerFromDataReceived[followingAnswerResult + 10] << 8) | (answerFromDataReceived[followingAnswerResult + 11])); 
																													
			answerResult = followingAnswerResult;
			followingAnswerResult = followingAnswerResult + (12 + RDLength); 
			
			byte authority = (byte) (dataReceived[3] & 0x4);		//check with AA data and 4 octets
			boolean isAuthority = authority == 4 ? true : false; 	//if equal to 4, then dataReceived[3] == 1 (authoritative)
			 
			DNSRecords(Arrays.copyOfRange(answerFromDataReceived, answerResult, followingAnswerResult), RDLength, isAuthority);		
			
		}	

		System.out.println("***Additional Section (" + additionalRecords + " records)***");
		
	}
	
	//will check ID, QR, RA and RCode for any mismatch/errors between request and response packets
	public void checkDataReceived(byte[] dataReceived) {
		//check ID for request and response packets
		if (dataReceived[0] != headerPacketBuffer.get(0) && dataReceived[1]  != headerPacketBuffer.get(1)) {
			System.out.println("ERROR: IDs mismatch for request and response." );
			System.exit(1);
		}
		
		//check QR --> QR should be 1 for response or 0 for request 
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
		switch ((byte)(dataReceived[3] << 4)) {
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
	}
	
	public void DNSRecords(byte[] answer, int RDLength, boolean authority) {
		
		//retrieve values from response for Type, TTL, authority value and RDLength
		short responseType = (short) (answer[2] << 8 | answer[3]);
		int responseTTL = Math.abs(answer[6] << 24 | answer[7] << 16 | answer[8] << 8 | answer[9]);
		String responseAuthorityValue = authority ? "auth" : "nonauth";
		
		int rDataIndex = 12; //bytes up to rdata 
		
		if (responseType == 0x0001) {		//TYPE: "A", 		
			TypeA(answer, rDataIndex, RDLength, responseTTL, responseAuthorityValue);
		}
		
		else if (responseType == 0x0002) {	//TYPE: "NS"
			TypeNS(answer, rDataIndex, RDLength, responseTTL, responseAuthorityValue);
		}
		
		else if (responseType == 0x000f) {	//TYPE: "MX"
			TypeMX(answer, rDataIndex, RDLength, responseTTL, responseAuthorityValue);
		}
		
		else if (responseType == 0x0005) {	//TYPE: "CNAME"
			TypeCNAME(answer, rDataIndex, RDLength, responseTTL, responseAuthorityValue);
		}
	}
	
	
	//for Type A: for an A (IP address) record, then RDATA is the IP address (four octets)
	public void TypeA(byte[] answer, int rDataIndex, int RDLength, int responseTTL, String responseAuthorityValue) {
		ByteBuffer IPAddress = ByteBuffer.allocate(4); //RDATA has IP address in 4 octets
		
		int length = rDataIndex + RDLength;
		for (int i = rDataIndex; i < length; i++) {		//need to understand this part
			IPAddress.put(answer[i]);
		}
		
		InetAddress IPAddressArray = null; 
		try {
			IPAddressArray = InetAddress.getByAddress(IPAddress.array());
		}
		
		catch (UnknownHostException exception) {
			System.out.println("Error: Unknown host exception ->" + exception);
		}
		
		System.out.println("IP	" + IPAddressArray.getHostAddress() + "	" + responseTTL + "	" + responseAuthorityValue); 
	}
	
	
	//for Type NS: name of the server specified using the same format as the QNAME field
	public void TypeNS(byte[] answer, int rDataIndex, int RDLength, int responseTTL, String responseAuthorityValue) {

		String compressedStringData = retrieveCompressedData(answer, rDataIndex, RDLength);
		
		//if there is an offset  
		if (answer[0] == 0xc0) {
			int rDataOffset = answer[1] - 12;		//shift by an offset of 12 for mcgill.ca
			String domainOffset = DOMAIN.substring(rDataOffset);	//simply add thing like mcgill.ca 
			String fullNameServer = (compressedStringData.concat(domainOffset));
			System.out.println("NS	" + fullNameServer + "	" + responseTTL + "	" + responseAuthorityValue);
		} 
		
		else {
			String fullNameServer = compressedStringData.concat(DOMAIN);
			System.out.println("NS	" + fullNameServer + "	" + responseTTL + "	" + responseAuthorityValue);
		}
	}
	
	//for Type MX: mail server records, then RDATA has the format of preference and exchange
	public void TypeMX(byte[] answer, int rDataIndex, int RDLength, int responseTTL, String responseAuthorityValue) {
		
		//specifying the preference given to this resource record among others at the same owner (lower values are preferred);
		short preference = (short) (answer[12] << 8 | answer[13]);
		
		//parsing rDataIndex with an increment of 2 to remove preference index (same reason for decreasing RDLength by 2) since data is in exchange
		String compressedStringData = retrieveCompressedData(answer, rDataIndex + 2, RDLength - 2);
		
		//if there is an offset
		if (answer[answer.length - 2] == 0xc0) {		//shift 2 because of preference
			int rDataOffset = answer[answer.length - 1] - 12;		//shift by an offset of 12 for mcgill.ca
			String domainOffset = DOMAIN.substring(rDataOffset);		//simply add thing like mcgill.ca 
			String fullNameServer = (compressedStringData.concat(domainOffset));
			System.out.println("MX	" + fullNameServer + "	" + preference + "	" + responseTTL + "	" + responseAuthorityValue);
		} 
		
		else { 
			String fullMailServer = compressedStringData.concat(DOMAIN);
			System.out.println("MX	" + fullMailServer + "	" + preference + "	" + responseTTL + "	" + responseAuthorityValue);
		}
	}
	
	//for type CNAME: canonical names
	public void TypeCNAME(byte[] answer, int rDataIndex, int RDLength, int responseTTL, String responseAuthorityValue) {
		
		String compressedStringData = retrieveCompressedData(answer, rDataIndex, RDLength);
		
		//if there is an offset 
		if (answer[rDataIndex] == 0xc0) {	
			int rDataOffset = answer[rDataIndex + 1] - 12;		//shift by an offset of 12 for mcgill.ca
			String fullNameServer = DOMAIN.substring(rDataOffset);	//simply add thing like mcgill.ca 
			System.out.println("CNAME	" + fullNameServer + "	" + responseTTL + "	"	+ responseAuthorityValue);
		} 
		
		//no offset
		else {
			System.out.println("CNAME	" + compressedStringData + "	" + responseTTL + "	" + responseAuthorityValue);
		}
	
	}
	
	//allocate space and store rData into ByteBuffer type by checking if there is an offset 
	public ByteBuffer convertRDataIntoByteBuffer(byte[] answer, int rDataIndex, int RDLength) {
		ByteBuffer resultByteBuffer = ByteBuffer.allocate(RDLength);
		for (byte b : Arrays.copyOfRange(answer, rDataIndex, answer.length)) {
			//if there is an offset
			if (String.format("%02X", b).equals("C0")) {	//format b byte and compare it with C0 for offset (java byte to hex string)
				break;
			}
			
			//no offset
			else {
				resultByteBuffer.put(b);
			}
		}
		
		return resultByteBuffer;
	}
	
	//retrieve name server from compressed data 
	public String retrieveCompressedData(byte[] answer, int rDataIndex, int RDLength) {
		
		ByteBuffer resultByteBuffer = convertRDataIntoByteBuffer(answer, rDataIndex, RDLength); 
		
		StringBuilder resultSB = new StringBuilder();
		for (int j = 0; j < resultByteBuffer.array().length; j++) {
			//values in compressed form are converted back to domain form
			char[] values = new char[resultByteBuffer.array()[j++]];
			
			for (int k = 0; k < values.length; k++) {
				values[k] = (char) resultByteBuffer.array()[j++];
			}
			
			resultSB.append(values);
			
			if (resultByteBuffer.array().length > j + 1) {
				resultSB.append(".");
			}
			
			j -=1;  
		}

		return resultSB.toString();	
	}
	
}
