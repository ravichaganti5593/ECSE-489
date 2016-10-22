import java.nio.*;
import java.util.*;

/*
 * This file includes the format of parameters to define a DNS Header
 */

public class DNSHeader {
	
	public static final int sizeOfHeader = 12;
	private ByteBuffer PacketHeader = ByteBuffer.allocate(sizeOfHeader);
	
	private byte QR;			//Query/Response Flag
	private byte Opcode;		//Operation Code
	private byte AA;			//Authoritative Answer Flag
	private byte TC;			//Truncation Flag
	private byte RD;			//Recursion Desired
	private byte RA;			//Recursion Available
	private byte Z;				//Zero
	private byte RCode;			//RCode
	
	private short ID;			//Identifier
	private short QDCount;		//Question count
	private short ANCount;		//Answer record count
	private short NSCount;		//Authority record count
	private short ARCount;		//Additional record count
	
	
	public DNSHeader () {
		
		QR = 0x0;
		Opcode = 0x0;
		AA = 0x0;
		TC = 0x0;
		RD = 0x1;
		RA = 0x0;
		Z = 0x0;
		RCode = 0x0;
		
		Random random = new Random();
		ID = (short) random.nextInt(Short.MAX_VALUE + 1);
		QDCount = 0x1;
		ANCount = 0x0;
		NSCount = 0x0;
		ARCount = 0x0;
		
		PacketHeader.putShort(ID);
		
		//convert 2nd layer of DNS Header into 16 bit type (short)
		short conversion = (short) (QR << 15 | Opcode << 11 | AA << 10 | TC << 9 | RD << 8 | RA << 7 | Z << 4 | RCode);
		
//		byte toRD = (byte) (QR << 7 | Opcode << 3 | AA << 2 | TC << 1 | RD);  	
//		byte toRcode = (byte) (RA << 7 | Z << 4 | RCode);
//		
//		PacketHeader.put(toRD);
//		PacketHeader.put(toRcode);
		
		PacketHeader.putShort(conversion);
		PacketHeader.putShort(QDCount);
		PacketHeader.putShort(ANCount);
		PacketHeader.putShort(NSCount);
		PacketHeader.putShort(ARCount);
	}

	 
	public ByteBuffer GetPacketHeader() {
		return PacketHeader;
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
