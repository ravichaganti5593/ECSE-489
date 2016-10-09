import java.util.*;
import java.net.*;
import java.io.*;


public class DNSClient {

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
		
	}
	
}
