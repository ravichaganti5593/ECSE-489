/*
 * This file includes the format of parameters used to define a DNS Header
 */

public class DNSHeader {
	
	public byte QR;				//Query/Response Flag
	public byte Opcode;			//Operation Code
	public byte AA;				//Authoritative Answer Flag
	public byte TC;				//Truncation Flag
	public byte RD;				//Recursion Desired
	public byte RA;				//Recursion Available
	public byte Z;				//Zero
	public byte RCode;			//RCode
	
	public short ID;			//Identifier
	public short QDCount;		//Question count
	public short ANCount;		//Answer record count
	public short NSCount;		//Authority record count
	public short ARCount;		//Additional record count
	
	
	public DNSHeader(byte QR, byte Opcode, byte AA, byte TC, byte RD, byte RA, byte Z, byte RCode, short ID, short QDCount, short ANCount, short NSCount, short ARCount) {
		this.QR = QR;
		this.Opcode = Opcode;
		this.AA = AA;
		this.TC = TC;
		this.RD = RD;
		this.RA = RA;
		this.Z = Z;
		this.RCode = RCode;
		this.ID = ID;
		this.QDCount = QDCount;
		this.ANCount = ANCount;
		this.NSCount = NSCount;
		this.ARCount = ARCount;
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
