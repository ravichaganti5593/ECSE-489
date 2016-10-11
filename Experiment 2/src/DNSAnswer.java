import java.io.IOException;
import java.nio.ByteBuffer;

public class DNSAnswer {
	
	public ByteBuffer NAME;
	public short QTYPE;
	public static final short QCLASS = 0x0001;
	
	int TTL;
	short RDLENGTH;
	byte[] RDATA;
	short PREFERENCE;
	byte[] EXCHANGE;		//same format as NAME field
	
	public DNSAnswer(String ANAME, String ATYPE, short ACLASS, int TTL, short RDLENGTH, byte[] RDATA, short PREFERENCE, byte[] EXCHANGE) {
		
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
