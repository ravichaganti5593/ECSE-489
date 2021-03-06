import java.io.UnsupportedEncodingException;
import java.nio.*;
import java.util.*;


public class DNSQuestion {
	
	public short QTYPE;
	public static final short QCLASS = (short) 0x0001;
	byte[] QNAME;
	
	public ByteBuffer Question;
	
	public DNSQuestion (String QNAME, String QTYPE) {
		this.QNAME = convertQNAME(QNAME).array();
		this.QTYPE = checkQTYPE(QTYPE);
		this.Question = generateQuestion();

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
	
			
	public short checkQTYPE (String QTYPE) {
		
		if (QTYPE.equals("A")) {		//send query for IP address
			return 0x1;
		}
		
		else if (QTYPE.equals("NS")) {	//send query for name server
			return 0x2;
		}
		
		else if (QTYPE.equals("MX")) {	//send query for mail server
			return 0xf;
		}
		
		else {
			return 0x1;					
		}
	}
	
	
	public ByteBuffer generateQuestion() {
		
		ByteBuffer resultQuestion = ByteBuffer.allocate(QNAME.length + 2*Short.BYTES);
		resultQuestion.put(QNAME);
		resultQuestion.putShort(QTYPE);
		resultQuestion.putShort(QCLASS);

		return resultQuestion;
		
	}
	
	
	public ByteBuffer GetQuestion() {
		return Question;
	}	
}
