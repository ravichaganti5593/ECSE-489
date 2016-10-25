import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.*;


public class DNSClient {

	
	public static void main (String[] args) throws Exception {

		
		try {
			DNSClient client = new DNSClient();
			client.start(args);
		}

		catch (Exception exception) {
			System.out.println("Error in main method: " + exception);
		}
	}

	public void start (String[] args) {
		System.out.println("-------------Executing Program-----------------");
		//first verify input and store values into input parameters
		DNSClientParameters DNSClientParameters = new DNSClientParameters();
		DNSClientParameters.verifyAndValidateInput(args);
		
		//create socket connection
		ClientServerConnection ClientServerConnection = new ClientServerConnection(DNSClientParameters.getTIMEOUT(), DNSClientParameters.getMAXRETRIES(), DNSClientParameters.getDNSPORTNUMBER(), DNSClientParameters.getTYPE(), DNSClientParameters.getSERVERIPADDRESS(), DNSClientParameters.getDOMAIN(), DNSClientParameters.getServerIPAddressBytes());
		ClientServerConnection.createSocketConnection();

		
		//print output by interpreting server response
		DNSServerResponse DNSServerResponse = new DNSServerResponse(ClientServerConnection.getDNSReceivePacket(), ClientServerConnection.getHeaderBuffer(), ClientServerConnection.getQuestionBuffer(), ClientServerConnection.getAnswerBuffer(), ClientServerConnection.getPacketBuffer(), ClientServerConnection.getRTT(), ClientServerConnection.getConnectionRetries(), DNSClientParameters.getDOMAIN(), DNSClientParameters.getSERVERIPADDRESS(), DNSClientParameters.getTYPE());
		DNSServerResponse.outputBehavior();
		
		System.out.println("Program successfully finished");

	}
	

	
	

	

	
	
}
