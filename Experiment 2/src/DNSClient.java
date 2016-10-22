import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.*;


public class DNSClient {

	int TIMEOUT = 5;
	int MAXRETRIES = 3;
	int PORTNUMBER = 53;
	String TYPE = "A";
	String IPADDRESS = "";
	String DOMAIN = "";
	
	final static int MAXIMUM_PORTS = 49151;
	
	public static void main (String[] args) throws Exception {
		 
		try {
			 DNSClient DNSClient = new DNSClient();
			 DNSClient.start(args);
		} 
		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
			return;
		}
		
	}
	
	
	
	public void start (String[] args) {
		//implement sockets
		
		verifyAndValidateInput(args);
		createSocketConnection();

	}
	
	public void createSocketConnection() {
		
		DNSHeader DNSHeader = new DNSHeader();	
		ByteBuffer headerBuffer = DNSHeader.GetPacketHeader();
		
		DNSQuestion DNSQuestion = new DNSQuestion(DOMAIN, TYPE);
		ByteBuffer questionBuffer = DNSQuestion.GetQuestion();
		
		ByteBuffer answerBuffer = ByteBuffer.allocate(512 - headerBuffer.capacity() - questionBuffer.capacity());
		ByteBuffer packetBuffer = ByteBuffer.allocate(headerBuffer.capacity() + questionBuffer.capacity() + answerBuffer.capacity());
		
		packetBuffer.put(headerBuffer.array());
		packetBuffer.put(questionBuffer.array());
		packetBuffer.put(answerBuffer.array());
		
		InetAddress serverIPAddress = null;
		
		try {
			serverIPAddress = InetAddress.getByAddress(addr);
		}
		
		catch (UnknownHostException e) {
			System.out.println(e);
		}
		
		
		
		
	}
	
	
	public void verifyAndValidateInput(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Invalid input: Need to enter at least 2 arguments");		
		}	
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t")) {
				try {
					TIMEOUT = Integer.parseInt(args[++i]);
					
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
					MAXRETRIES = Integer.parseInt(args[++i]);
					
					if (MAXRETRIES < 0) {
						System.out.println("Invalid input: 'TIMEOUT' value must be greater than 0");
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
					PORTNUMBER = Integer.parseInt(args[++i]);
					
					if (PORTNUMBER < 1 || PORTNUMBER > MAXIMUM_PORTS) {
						System.out.println("Invalid input: 'TIMEOUT' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'MAXRETRIES' value must be a number");
                    					
				}
				
				i++;
			}
			
			else if (args[i].equals("mx")) {
				TYPE = "MX";
			}
			
			else if (args[i].equals("ns")) {
				TYPE = "NS";
			}
			
			else if (args[i].charAt(0) == '@') {
				IPADDRESS = args[i].substring(1);
				if (!checkValidIPAddress(IPADDRESS)) {
					System.out.println("Invalid input: IP address is not a valid input");
					System.exit(1);
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
