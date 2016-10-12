import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DNSAnswer {
	
	public ByteBuffer ANAME;
	public short ATYPE;
	public static final short ACLASS = 0x0001;
	
	int TTL;
	short RDLENGTH;
	byte[] RDATA;
	short PREFERENCE;
	byte[] EXCHANGE;		//same format as NAME field
	
	public DNSAnswer(String ANAME, String ATYPE, short ACLASS, int TTL, short RDLENGTH, byte[] RDATA, short PREFERENCE, byte[] EXCHANGE) {
		
		this.ANAME = convertQNAME(ANAME);
		
		
		
		try {
			this.ATYPE = checkQTYPE(ATYPE);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ByteBuffer convertQNAME(String QNAME) {
		
		String[] splitArray = QNAME.split("\\.");
		int sizeOfBytes = 0;
		
		for (String i: splitArray) {
			sizeOfBytes = sizeOfBytes + 1 + i.length();	
		}
		
		sizeOfBytes += 1;	//for the last one byte as 0
		
		ByteBuffer result = ByteBuffer.allocate(sizeOfBytes);
		
		for (String i: splitArray) {
			result.put((byte) i.length());
			
			try {
				result.put(i.getBytes("UTF-8"));
			}
			
			catch (UnsupportedEncodingException exception) {
				System.out.println("Error in format of UTF-8 --> " + exception);
			}
		}
		
		result.put((byte) 0x00);
		return result;

	}
	
	public short checkQTYPE (String QTYPE) throws IOException {
		
		if (QTYPE.equals('A')) {		//send query for IP address
			return 0x1;
		}
		
		else if (QTYPE.equals("NS")) {	//send query for name server
			return 0x2;
		}
		
		else if (QTYPE.equals("MX")) {	//send query for mail server
			return 0xf;
		}
		
		else if (QTYPE.equals("CNAME")) {	//send query for mail server
			return 0x5;
		}
		
		else {
			throw new IOException("This type of question is unsupported");			
		}
	}
	
}
