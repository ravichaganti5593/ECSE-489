
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
	//------------------------------------GITHUB TESTS---------------------------------------------------------
	
	public void testInputGITHUBGoogleDns() {
		String[] args = {"@8.8.8.8", "www.github.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputGITHUBMcGillDns() {
		String[] args = {"@132.206.85.18", "www.github.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------APPLE TESTS---------------------------------------------------------
	
	public void testInputAPPLEGoogleDns() {
		String[] args = {"@8.8.8.8", "www.apple.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputAPPLEMcGillDns() {
		String[] args = {"@132.206.85.18", "www.apple.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------YOUTUBE TESTS---------------------------------------------------------
	
	public void testInputYOUTUBEGoogleDns() {
		String[] args = {"@8.8.8.8", "www.youtube.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputYOUTUBEMcGillDns() {
		String[] args = {"@132.206.85.18", "www.youtube.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//------------------------------------MICROSOFT TESTS---------------------------------------------------------
	
	public void testInputMICROSOFTGoogleDns() {
		String[] args = {"@8.8.8.8", "www.microsoft.com"};
		try {
			DNSClient.main(args);
		} 		
		catch (Exception exception) {
			System.out.println("Error is " + exception);
		}
	}
	
	public void testInputMICROSOFTMcGillDns() {
		String[] args = {"@132.206.85.18", "www.microsoft.com"};
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
		
		System.out.println("www.github.com -------> using Google DNS Server");
		test.testInputGITHUBGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.github.com -------> using McGill DNS Server");
		test.testInputGITHUBMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		
		System.out.println("www.apple.com -------> using Google DNS Server");
		test.testInputAPPLEGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.apple.com -------> using McGill DNS Server");
		test.testInputAPPLEMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.youtube.com -------> using Google DNS Server");
		test.testInputYOUTUBEGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.youtube.com -------> using McGill DNS Server");
		test.testInputYOUTUBEMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		System.out.println("www.microsoft.com -------> using Google DNS Server");
		test.testInputMICROSOFTGoogleDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	
		
		System.out.println("www.microsoft.com -------> using McGill DNS Server");
		test.testInputMICROSOFTMcGillDns();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		

	
		

		
	}

	
}
