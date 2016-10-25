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
	
	DatagramPacket DNSReceivePacket;
	ByteBuffer headerBuffer;
	ByteBuffer questionBuffer;
	ByteBuffer answerBuffer;
	ByteBuffer packetBuffer;
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
	
	public DatagramPacket getDNSReceivePacket() {
		return DNSReceivePacket;
	}
	
	public ByteBuffer getHeaderBuffer() {
		return headerBuffer;
	}
	
	public ByteBuffer getQuestionBuffer() {
		return questionBuffer;
	}
	
	public ByteBuffer getAnswerBuffer() {
		return answerBuffer;
	}
	
	public ByteBuffer getPacketBuffer() {
		return packetBuffer; 
	}
	
	public long getRTT() {
		return RTT;
	}
	
	public int getConnectionRetries() {
		return connectionRetries;
	}
	
}
