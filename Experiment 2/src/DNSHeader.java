import java.nio.*;
import java.util.*;

/*
 * This file includes the format of parameters to define a DNS Header
 */

public class DNSHeader {
	
	public static final int sizeOfHeader = 12;
	private ByteBuffer HeaderNode = ByteBuffer.allocate(sizeOfHeader);
	
	private byte QR;				//Query/Response Flag
	private byte Opcode;			//Operation Code
	private byte AA;				//Authoritative Answer Flag
	private byte TC;				//Truncation Flag
	private byte RD;				//Recursion Desired
	private byte RA;				//Recursion Available
	private byte Z;				//Zero
	private byte RCode;			//RCode
	
	private short ID;			//Identifier
	private short QDCount;		//Question count
	private short ANCount;		//Answer record count
	private short NSCount;		//Authority record count
	private short ARCount;		//Additional record count
	
	
	public DNSHeader () {
		
		QR = (byte) 0;
		Opcode = (byte) 0;
		AA = (byte) 0;
		TC = (byte) 0;
		RD = (byte) 1;
		RA = (byte) 0;
		Z = (byte) 0;
		RCode = (byte) 0;
		
		Random random = new Random();
		ID = (short) random.nextInt(Short.MAX_VALUE + 1);
		QDCount = (short) 1;
		ANCount = (short) 0;
		NSCount = (short) 0;
		ARCount = (short) 0;
		
		HeaderNode.putShort(ID);
		
		//convert 2nd layer of DNS Header into 16 bit type (short)
		short conversion = (short) (QR << 15 | Opcode << 11 | AA << 10 | TC << 9 | 
									RD << 8 | RA << 7 | Z << 4 | RCode);
		
		HeaderNode.putShort(conversion);
		HeaderNode.putShort(QDCount);
		HeaderNode.putShort(ANCount);
		HeaderNode.putShort(NSCount);
		HeaderNode.putShort(ARCount);
	}

	
	public ByteBuffer GetHeaderNode() {
		return HeaderNode;
	}

	
	public byte GetQR() {
		return QR;
	}
	
	public byte GetOpcode () {
		return Opcode;
	}
	
	public byte GetAA() {
		return AA;
	}
	
	public byte GetTC() {
		return TC;
	}
	
	public byte GetRD() {
		return RD;
	}
	
	public byte GetRA() {
		return RA;
	}
	
	public byte GetZ() {
		return Z;
	}
	
	public byte GetRCode() {
		return RCode;
	}
	
	public short GetID() {
		return ID;
	}
	
	public short GetQDCount() {
		return QDCount;
	}
	
	public short GetANCount() {
		return ANCount;
	}
	
	public short GetNSCount() {
		return NSCount;
	}
	
	public short GetARCount() {
		return ARCount;
	}
		
	
}
