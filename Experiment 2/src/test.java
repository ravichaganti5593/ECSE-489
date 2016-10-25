
public class test {
		
	//------------------------------------------------------------------------------------------------------
	//------------------------------------MCGILL TESTS------------------------------------------------------
	
	public void testInputMcGillGoogleDns() {
		String[] args = {"@8.8.8.8", "www.mcgill.ca"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputMcGillGoogleDnsNS() {	
		String[] args = {"-ns", "@8.8.8.8", "mcgill.ca"};
		try {
			DNSClient.main(args);
		}		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputMcGillGoogleDnsMX() {		
		String[] args = {"-mx", "@8.8.8.8", "mcgill.ca"};
		try {
			DNSClient.main(args);
		} 
		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}

	public void testInputMcGillDns() {		
		String[] args = {"@132.206.85.18", "www.mcgill.ca"};
		try {
			DNSClient.main(args);
		} 
		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputMcGillDnsNS() {		
		String[] args = {"-ns", "@132.206.85.18", "mcgill.ca"};
		try {
			DNSClient.main(args);
		} 
		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------GOOGLE TESTS------------------------------------------------------
	
	public void testInputGoogleGoogleDns() {
		String[] args = {"@8.8.8.8", "www.google.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
		
	public void testInputGoogleMcGillDns() {		
		String[] args = {"@132.206.85.18", "www.google.com"};
		try {
			DNSClient.main(args);
		} 
		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}

	//------------------------------------------------------------------------------------------------------
	//------------------------------------FACEBOOK TESTS----------------------------------------------------
	
	public void testInputFacebookGoogleDns() {
		String[] args = {"@8.8.8.8", "www.facebook.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputFacebookMcGillDns() {
		String[] args = {"@132.206.85.18", "www.facebook.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------NDTV TESTS---------------------------------------------------------
	
	public void testInputNDTVGoogleDns() {
		String[] args = {"@8.8.8.8", "www.ndtv.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputNDTVMcGillDns() {
		String[] args = {"@132.206.85.18", "www.ndtv.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------LINKEDIN TESTS---------------------------------------------------------
	
	public void testInputLINKEDINGoogleDns() {
		String[] args = {"@8.8.8.8", "www.linkedin.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputLINKEDINMcGillDns() {
		String[] args = {"@132.206.85.18", "www.linkedin.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------CBC TESTS---------------------------------------------------------
	
	public void testInputCBCGoogleDns() {
		String[] args = {"@8.8.8.8", "www.cbc.ca"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputCBCMcGillDns() {
		String[] args = {"@132.206.85.18", "www.cbc.ca"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------AMAZON TESTS---------------------------------------------------------
	
	public void testInputAMAZONGoogleDns() {
		String[] args = {"@8.8.8.8", "www.amazon.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputAMAZONMcGillDns() {
		String[] args = {"@132.206.85.18", "www.amazon.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------YAHOO TESTS---------------------------------------------------------
	
	public void testInputYAHOOGoogleDns() {
		String[] args = {"@8.8.8.8", "www.yahoo.ca"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputYAHOOMcGillDns() {
		String[] args = {"@132.206.85.18", "www.yahoo.ca"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	
	public static void main (String[] args) {
		test test = new test();
		System.out.println("www.mcgill.ca -------> using Google DNS Server");
		test.testInputMcGillGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.mcgill.ca -------> using Google DNS Server - for NS");
		test.testInputMcGillGoogleDnsNS();		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.mcgill.ca -------> using Google DNS Server - for MX");
		test.testInputMcGillGoogleDnsMX();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.mcgill.ca -------> using McGill DNS Server");
		test.testInputMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.mcgill.ca -------> using McGill DNS Server - for NS");
		test.testInputMcGillDnsNS();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.google.com -------> using Google DNS Server");
		test.testInputGoogleGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.google.com -------> using McGill DNS Server");
		test.testInputGoogleMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.facebook.com -------> using Google DNS Server");
		test.testInputFacebookGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.facebook.com -------> using McGill DNS Server");
		test.testInputFacebookMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.ndtv.com -------> using Google DNS Server");
		test.testInputNDTVGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.ndtv.com -------> using McGill DNS Server");
		test.testInputNDTVMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		System.out.println("www.amazon.com -------> using Google DNS Server");
		test.testInputAMAZONGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.amazon.com -------> using McGill DNS Server");
		test.testInputAMAZONMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.linkedin.com -------> using Google DNS Server");
		test.testInputLINKEDINGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.linkedin.com -------> using McGill DNS Server");
		test.testInputLINKEDINMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.cbc.ca -------> using Google DNS Server");
		test.testInputCBCGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.cbc.ca -------> using McGill DNS Server");
		test.testInputCBCMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		

	
		
		System.out.println("www.yahoo.ca -------> using Google DNS Server");
		test.testInputYAHOOGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.yahoo.ca -------> using McGill DNS Server");
		test.testInputYAHOOMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
	}

	
}
