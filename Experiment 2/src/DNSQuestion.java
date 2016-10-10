
public class DNSQuestion {
	
	public byte[] QNAME;
	public short QTYPE;
	public String QCLASS;
	
	public DNSQuestion (String QNAME, String QTYPE) {
		this.QNAME = convertQNAME(QNAME);
		this.QTYPE = checkQTYPE(QTYPE);

	}
	
	
	public byte[] convertQNAME(String QNAME) {
		byte[] answer = new byte[3];
		return answer;
	}
	
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
	
}
