import java.util.*;
import java.net.*;
import java.io.*;


public class DNSClient {

	int TIMEOUT = 5;
	int MAXRETRIES = 3;
	int PORTNUMBER = 53;
	String TYPE = "A";
	String SERVER = "";
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
				SERVER = args[i].substring(1);
				if (!checkValidServer(SERVER)) {
					System.out.println("Invalid input: IP address is not a valid input");
					System.exit(1);
				}
				
			}
			
			else {
				DOMAIN = args[i];
			}
			
		}
		
	}
	
	public boolean checkValidServer (String SERVER) {
		try {
	        if (SERVER == null || SERVER.isEmpty()) {
	            return false;
	        }

	        String[] parts = SERVER.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt(s);
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if (SERVER.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } 
		
		catch (NumberFormatException exception) {
	        return false;
	    }
	}
	
	
}
