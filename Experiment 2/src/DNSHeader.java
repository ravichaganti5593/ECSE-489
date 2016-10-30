import java.nio.*;
import java.util.*;

/*
 * This file includes the format of parameters to define a DNS Header
 */

public class DNSHeader {
	
	public static final int sizeOfHeader = 12;
	private ByteBuffer PacketHeader = ByteBuffer.allocate(sizeOfHeader);
	
	byte QR;			//Query/Response Flag
	byte Opcode;		//Operation Code
	byte AA;			//Authoritative Answer Flag
	byte TC;			//Truncation Flag
	byte RD;			//Recursion Desired
	byte RA;			//Recursion Available
	byte Z;				//Zero
	byte RCode;			//RCode
	
	short ID;			//Identifier
	short QDCount;		//Question count
	short ANCount;		//Answer record count
	short NSCount;		//Authority record count
	short ARCount;		//Additional record count
	
	
	public DNSHeader () {
		
		QR = 0x0;
		Opcode = 0x0;
		AA = 0x0;
		TC = 0x0;
		RD = 0x1;
		RA = 0x0;
		Z = 0x0;
		RCode = 0x0;
		
		Random randomNumber = new Random();
		ID = (short) randomNumber.nextInt(Short.MAX_VALUE + 1);
		QDCount = 0x1;
		ANCount = 0x0;
		NSCount = 0x0;
		ARCount = 0x0;
		
		PacketHeader.putShort(ID);
		
		short conversion = (short) (QR << 15 | Opcode << 11 | AA << 10 | TC << 9 | RD << 8 | RA << 7 | Z << 4 | RCode);
		
		PacketHeader.putShort(conversion);
		PacketHeader.putShort(QDCount);
		PacketHeader.putShort(ANCount);
		PacketHeader.putShort(NSCount);
		PacketHeader.putShort(ARCount);
	}

	 
	public ByteBuffer GetPacketHeader() {
		return PacketHeader;
	}
	
}
