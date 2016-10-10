import java.io.UnsupportedEncodingException;
import java.nio.*;
import java.nio.charset.Charset;
import java.util.*;


public class DNSQuestion {
	
	public ArrayList<Byte> QNAME;
	public short QTYPE;
	public static final short QCLASS = 0x0001;
	public ByteBuffer Question;
	
	public DNSQuestion (String QNAME, String QTYPE) {
		this.QNAME = convertQNAME(QNAME);
		this.QTYPE = checkQTYPE(QTYPE);
		this.Question = createQuestion();

	}
	
	public ArrayList<Byte> convertQNAME(String QNAME) {
		ArrayList<Byte> result = new ArrayList<Byte>();
		String[] array = QNAME.split(".");
		
		for (String i: array) {
			try {
				byte[] arrayOfBytes = i.getBytes("UTF-8");
				result.add((byte) arrayOfBytes.length);
				for (byte j: arrayOfBytes) {
					result.add(j);
				}
			} 
			
			catch (UnsupportedEncodingException exception) {
				System.out.println("Error in UTF-8 format : " + exception);
			}
		}
		
		result.add((byte) 0);
		return result;
	}
	
	
//	public ByteBuffer convertQNAME(String QNAME) {
//		
//		String[] splitArray = QNAME.split(".");
//		int sizeOfBytes = 0;
//		
//		for (String i: splitArray) {
//			sizeOfBytes = sizeOfBytes + 1 + i.length();		
//		}
//		
//		sizeOfBytes += 1;	//for the last one byte as 0
//		
//		ByteBuffer result = ByteBuffer.allocate(sizeOfBytes);
//		
//		for (String i: splitArray) {
//			
//			try {
//				result.put(i.getBytes(Charset.forName("UTF-8")));
//			}
//			
//			catch (Exception exception) {
//				System.out.println("Error in format of UTF-8 --> " + exception);
//			}
//		}
//		
//		result.put((byte) 0x00);
//		return result;
//		
//	}
	
	
	public short checkQTYPE (String QTYPE) {
		
		if (QTYPE.equals('A')) {		//send query for IP address
			return 0x1;
		}
		
		else if (QTYPE.equals("NS")) {	//send query for name server
			return 0x2;
		}
		
		else if (QTYPE.equals("MX")) {	//send query for mail server
			return 0xf;
		}
		
		else {
			return 0x1;					//default, doesn't matter (what about CName??)
		}
	}
	
	
	public ByteBuffer createQuestion() {
		ArrayList<Byte> result = new ArrayList<Byte>(QNAME);
		
		//add QTYPE to question
		result.add((byte) 0);
		result.add((byte) QTYPE);
		
		//add QCLASS to question
		result.add((byte) 0);
		result.add((byte) 1);
		
		ByteBuffer resultQuestion = ByteBuffer.allocate(result.size());
		
		for (Byte bytes: result) {
			resultQuestion.put(bytes);
		}
		
		return resultQuestion;
		
	}
	
	
	public ByteBuffer GetQuestion() {
		return Question;
	}
	
}
