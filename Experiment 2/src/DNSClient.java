import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.*;


public class DNSClient {

	int TIMEOUT = 5;
	int MAXRETRIES = 3;
	int DNSPORTNUMBER = 53;
	String TYPE = "A";
	String SERVERIPADDRESS = "";
	String DOMAIN = "";
	byte[] serverIPAddressBytes = new byte[4];

	
	DatagramPacket DNSReceivePacket;
	ByteBuffer headerBuffer;
	ByteBuffer questionBuffer;
	ByteBuffer answerBuffer;
	ByteBuffer packetBuffer;
	long RTT = 0;
	int connectionRetries = 0;
	
	final static int MAXIMUM_PORTS = 49151;
	
	public static void main (String[] args) {
		DNSClient client = new DNSClient();
		client.start(args);
		
	}

	public void start (String[] args) {

		verifyAndValidateInput(args);
		createSocketConnection();
		outputBehavior();

	}
	
	public void createSocketConnection() {
		
		DNSHeader DNSHeader = new DNSHeader();	
		headerBuffer = DNSHeader.GetPacketHeader();
		
		DNSQuestion DNSQuestion = new DNSQuestion(DOMAIN, TYPE);
		questionBuffer = DNSQuestion.GetQuestion();
		
		answerBuffer = ByteBuffer.allocate(512 - headerBuffer.capacity() - questionBuffer.capacity());
		packetBuffer = ByteBuffer.allocate(headerBuffer.capacity() + questionBuffer.capacity() + answerBuffer.capacity());
		
		packetBuffer.put(headerBuffer.array());
		packetBuffer.put(questionBuffer.array());
		packetBuffer.put(answerBuffer.array());
		
		InetAddress serverIPAddress = null;
		
		try {
			serverIPAddress = InetAddress.getByAddress(serverIPAddressBytes);		
		}
		
		catch (UnknownHostException exception) {
			System.out.println("Unknown host error: " + exception);
		}
		
		DatagramSocket DNSSocket = null;
		
		try {
			DNSSocket = new DatagramSocket();
			DNSSocket.setSoTimeout(1000 * TIMEOUT);
		}
		
		catch (SocketException exception) {
			System.out.println("Socket exception " + exception);
		}
		

		Exception socketTimeOut = new Exception();
		DNSReceivePacket = new DatagramPacket(packetBuffer.array(), packetBuffer.array().length);	
		byte[] sendData = packetBuffer.array();
		DatagramPacket DNSSendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, DNSPORTNUMBER);
		
		do {
			try {
				DNSSocket.send(DNSSendPacket);
			} 
			
			catch (IOException exception) {
				System.out.println("ERROR: Could not send packet");
			}
			
			try {
				long startTime = System.currentTimeMillis();
				DNSSocket.receive(DNSReceivePacket);
				long endTime = System.currentTimeMillis();
				RTT = endTime - startTime;
			}
			
			catch (IOException exception) {
				socketTimeOut = exception;
				connectionRetries++;
				
				if (connectionRetries == MAXRETRIES) {
					System.out.println("ERROR: maximum number of retries");
					System.exit(1);
				}
			}	
		}
		
		while (socketTimeOut instanceof SocketTimeoutException && connectionRetries < MAXRETRIES);
		
		DNSSocket.close();
	}
	
	
	public void outputBehavior() {
		byte[] dataReceived = DNSReceivePacket.getData();
		int answerIndex = headerBuffer.capacity() + questionBuffer.capacity();
		byte[] receivedAnswer = Arrays.copyOfRange(dataReceived, answerIndex, dataReceived.length);
		byte[] answerFromType = Arrays.copyOfRange(receivedAnswer, 2, dataReceived.length);
		
		int numAnswers = DNSReceivePacket.getData()[6] << 8 | DNSReceivePacket.getData()[7];
		
		System.out.println("DnsClient sending request for " + DOMAIN);
		System.out.println("Server: " + SERVERIPADDRESS);
		System.out.println("Request type: " + TYPE);
		
		if (dataReceived[1]  != headerBuffer.get(1) || dataReceived[0] != headerBuffer.get(0) ) {
			System.out.println("ERROR: Unexpected Response: Request and response IDs don't match" );
			System.exit(1);
		}
		//check if response is a response 
		
		if (((byte) ((dataReceived[2] >> 8) & 1)) != 1) {
			System.out.println("ERROR: Unexpected Response: Response packet is a request");
			System.exit(1);
		}
		
		//check if server supports recursive queries
		if (((byte) ((dataReceived[3] >> 8) & 1)) != 1){
			System.out.println("ERROR: Server does not support recursive queries");
			System.exit(1);
		}
		//check if there is an error condition
		byte rcode = (byte)(dataReceived[3] << 4);
		if (rcode == 1) {
			System.out.println("ERROR: the name server was unable to interpret the query");
			System.exit(1);
		}
		else if (rcode == 2) {
			System.out.println("ERROR: the name server was unable to interpret the query");
		}
		
		System.out.println("Response received after " + RTT + " milliseconds ("	+ connectionRetries + " retries)");
		System.out.println("***Answer  Section  (" + numAnswers + "  records)***");
	}
	
	public void verifyAndValidateInput(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Invalid input: Need to enter at least 2 arguments");
			System.exit(1);
		}	
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t")) {
				try {
					TIMEOUT = Integer.parseInt(args[i + 1].trim());
					
					if (TIMEOUT < 0) {
						System.out.println("Invalid input: 'TIMEOUT' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'TIMEOUT' value must be a number");
                    
				}
				
				i++;
			}
			
			else if (args[i].equals("-r")) {
				try {
					MAXRETRIES = Integer.parseInt(args[i + 1].trim());
					
					if (MAXRETRIES < 0) {
						System.out.println("Invalid input: 'MAXRETRIES' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'MAXRETRIES' value must be a number");
                    			
				}
				
				i++;
			}
			
			else if (args[i].equals("-p")) {
				try {
					DNSPORTNUMBER = Integer.parseInt(args[i + 1].trim());
					
					if (DNSPORTNUMBER < 1 || DNSPORTNUMBER > MAXIMUM_PORTS) {
						System.out.println("Invalid input: 'PORT' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'PORT' value must be a number");
                    					
				}
				
				i++;
			}
			
			else if (args[i].equals("-mx")) {
				TYPE = "MX";
			}
			
			else if (args[i].equals("-nx")) {
				TYPE = "NS";
			}
			
			else if (args[i].charAt(0) == '@') {
				SERVERIPADDRESS = args[i].substring(1);
				if (!checkValidIPAddress(SERVERIPADDRESS)) {
					System.out.println("Invalid input: IP address is not a valid input");
					System.exit(1);
				}
				
				else {
					//get bytes from server IP address
					int indexOfByte = 0;
					int startIndex = 0;
					
					for (int j = 0; j < SERVERIPADDRESS.length(); j++) {
						if (SERVERIPADDRESS.charAt(j) == '.' || j == SERVERIPADDRESS.length() - 1) {
							String subIPAddress;
							
							if (SERVERIPADDRESS.charAt(j) == '.') {
								subIPAddress = SERVERIPADDRESS.substring(startIndex, j);
							}
							
							else {
								subIPAddress = SERVERIPADDRESS.substring(startIndex);
							}
							
							serverIPAddressBytes[indexOfByte] = (byte) Integer.parseInt(subIPAddress);
							indexOfByte++;
							startIndex = j + 1;
						}
					}
					
				}
				
			}
			
			else {
				DOMAIN = args[i];
			}
			
		}
		
	}
	
	public boolean checkValidIPAddress (String IPAddress) {
		try {
	        if (IPAddress == null || IPAddress.isEmpty()) {
	            return false;
	        }

	        String[] parts = IPAddress.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt(s);
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if (IPAddress.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } 
		
		catch (NumberFormatException exception) {
	        return false;
	    }
	}
	
	
}
