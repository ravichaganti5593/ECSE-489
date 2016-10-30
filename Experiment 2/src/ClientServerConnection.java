import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ClientServerConnection {
	
	int TIMEOUT = 5;
	int MAXRETRIES = 3;
	int DNSPORTNUMBER = 53;
	public String TYPE = "A";
	String SERVERIPADDRESS = "";
	String DOMAIN = "";
	byte[] serverIPAddressBytes = new byte[4];
	
	DatagramPacket ReceivedPacket;
	ByteBuffer headerPacketBuffer;
	ByteBuffer questionPacketBuffer;
	ByteBuffer answerPacketBuffer;
	ByteBuffer totalPacketBuffer;
	long RTT = 0;
	int connectionRetries = 0;
	
	public ClientServerConnection(int TIMEOUT, int MAXRETRIES, int DNSPORTNUMBER, String TYPE, String SERVERIPADDRESS, String DOMAIN, byte[] serverIPAddressBytes) {
		this.TIMEOUT = TIMEOUT;
		this.MAXRETRIES = MAXRETRIES;
		this.DNSPORTNUMBER = DNSPORTNUMBER;
		this.TYPE = TYPE;
		this.SERVERIPADDRESS = SERVERIPADDRESS;
		this.DOMAIN = DOMAIN;
		this.serverIPAddressBytes = serverIPAddressBytes;
	}
	
	
	public void createSocketConnection() {
		
		DNSHeader DNSHeader = new DNSHeader();	
		headerPacketBuffer = DNSHeader.GetPacketHeader();
		
		DNSQuestion DNSQuestion = new DNSQuestion(DOMAIN, TYPE);
		questionPacketBuffer = DNSQuestion.GetQuestion();
		
		answerPacketBuffer = ByteBuffer.allocate(512 - headerPacketBuffer.capacity() - questionPacketBuffer.capacity());
		totalPacketBuffer = ByteBuffer.allocate(headerPacketBuffer.capacity() + questionPacketBuffer.capacity() + answerPacketBuffer.capacity());
		
		totalPacketBuffer.put(headerPacketBuffer.array());
		totalPacketBuffer.put(questionPacketBuffer.array());
		totalPacketBuffer.put(answerPacketBuffer.array());
		
		InetAddress serverIPAddress = null;
		
		try {
			serverIPAddress = InetAddress.getByAddress(serverIPAddressBytes);		
		}
		
		catch (UnknownHostException exception) {
			System.out.println("Error: there is an unknown host exception error: " + exception);
		}
		
		DatagramSocket UDPSocket = null;
		
		try {
			UDPSocket = new DatagramSocket();
			UDPSocket.setSoTimeout(1000 * TIMEOUT);
		}
		
		catch (SocketException exception) {
			System.out.println("Error: Socket connection failed with exception: " + exception);
		}
		

		
		ReceivedPacket = new DatagramPacket(totalPacketBuffer.array(), totalPacketBuffer.array().length);	
		byte[] sentTotalPacketArray = totalPacketBuffer.array();
		DatagramPacket DNSSendPacket = new DatagramPacket(sentTotalPacketArray, sentTotalPacketArray.length, serverIPAddress, DNSPORTNUMBER);
		
		Exception timeoutForSocket = new Exception();
		do {
			try {
				UDPSocket.send(DNSSendPacket); 
			} 
			
			catch (IOException exception) {
				System.out.println("ERROR: Could not send packet");
			}
			
			try {
				long sendingTimeStamp = System.currentTimeMillis();
				UDPSocket.receive(ReceivedPacket);
				long receivingTimeStamp = System.currentTimeMillis();
				RTT = receivingTimeStamp - sendingTimeStamp; 
			}
			
			catch (IOException exception) {
				timeoutForSocket = exception;
				connectionRetries++;
				
				if (connectionRetries == MAXRETRIES) {
					System.out.println("ERROR: MAX retries has been reached");
					System.exit(1);
				}
			}	
		}
		
		while (timeoutForSocket instanceof SocketTimeoutException && connectionRetries < MAXRETRIES);
		
		UDPSocket.close(); 
	}
	
	public DatagramPacket getDNSReceivePacket() {
		return ReceivedPacket;
	}
	
	public ByteBuffer getHeaderBuffer() {
		return headerPacketBuffer;
	}
	
	public ByteBuffer getQuestionBuffer() {
		return questionPacketBuffer;
	}
	
	public ByteBuffer getAnswerBuffer() {
		return answerPacketBuffer;
	}
	
	public ByteBuffer getPacketBuffer() {
		return totalPacketBuffer; 
	}
	
	public long getRTT() {
		return RTT;
	}
	
	public int getConnectionRetries() {
		return connectionRetries;
	}
	
}
