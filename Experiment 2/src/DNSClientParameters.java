
public class DNSClientParameters {
	
	int TIMEOUT = 5;
	int MAXRETRIES = 3;
	int DNSPORTNUMBER = 53;
	String TYPE = "A";
	String SERVERIPADDRESS = "";
	String DOMAIN = "";
	byte[] serverIPAddressBytes = new byte[4];
	
	
	public void verifyAndValidateInput(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Invalid input: Need to enter at least 2 input arguments");
			System.exit(1);
		}	
		
		else if (args.length > 9) {
			System.out.println("Invalid input: Cannot have more than 9 input arguments");
			System.exit(1);
		}
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t")) {
				try {
					TIMEOUT = Integer.parseInt(args[++i].trim());
					
					if (TIMEOUT < 0) {
						System.out.println("Invalid input: 'TIMEOUT' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'TIMEOUT' value must be a number");
                    
				}
			}
			
			else if (args[i].equals("-r")) {
				try {
					MAXRETRIES = Integer.parseInt(args[++i].trim());
					
					if (MAXRETRIES < 0) {
						System.out.println("Invalid input: 'MAXRETRIES' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'MAXRETRIES' value must be a number");
                    			
				}
			}
			
			else if (args[i].equals("-p")) {
				try {
					DNSPORTNUMBER = Integer.parseInt(args[++i].trim());
					
					if (DNSPORTNUMBER < 1) {
						System.out.println("Invalid input: 'PORT' value must be greater than 0");
						System.exit(1);
					}
				}
				
				catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input: 'PORT' value must be a number");
                    					
				}
			}
			
			else if (args[i].equals("-mx")) {
				TYPE = "MX";
			}
			
			else if (args[i].equals("-ns")) {
				TYPE = "NS";
			}
			
			else if (args[i].charAt(0) == '@') {
				SERVERIPADDRESS = args[i].substring(1);
				if (!checkValidIPAddress(SERVERIPADDRESS)) {
					System.out.println("Invalid input: DNS server '@' is not a valid input");
					System.exit(1);
				}
				
				else {
					//get bytes from server IP address
					int indexOfByte = 0;
					int indexOfStart = 0;
					int length = SERVERIPADDRESS.length();
					
					for (int k = 0; k < length; k++) {
						if (k == SERVERIPADDRESS.length() - 1 || SERVERIPADDRESS.charAt(k) == '.') {
							String subIPAddress;
							
							if (SERVERIPADDRESS.charAt(k) == '.') {
								subIPAddress = SERVERIPADDRESS.substring(indexOfStart, k);
							}
							
							else {
								subIPAddress = SERVERIPADDRESS.substring(indexOfStart);
							}
							
							serverIPAddressBytes[indexOfByte] = (byte) Integer.parseInt(subIPAddress);
							indexOfByte++;
							indexOfStart = k + 1;
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
	
	
	public int getTIMEOUT() {
		return TIMEOUT;
	}
	
	public int getMAXRETRIES() {
		return MAXRETRIES;
	}
	
	public int getDNSPORTNUMBER() {
		return DNSPORTNUMBER;
	}
	
	public String getTYPE() {
		return TYPE;
	}
	
	public String getSERVERIPADDRESS() {
		return SERVERIPADDRESS;
	}
	
	public String getDOMAIN() {
		return DOMAIN;
	}
	
	public byte[] getServerIPAddressBytes() {
		return serverIPAddressBytes;
	}
}
