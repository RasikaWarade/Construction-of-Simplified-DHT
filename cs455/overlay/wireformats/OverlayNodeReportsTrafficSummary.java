package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeReportsTrafficSummary implements Event{

	private String MESSAGE_TYPE = null;
	private int ASSIGNED_ID = 0;
	private int TOTAL_NO_OF_PACKETS_SENT = 0;
	private int TOTAL_NO_OF_PACKETS_RELAYED = 0;
	private long SUM_OF_PACKETS_SENT = 0;
	private int  TOTAL_PACKETS_RECVD= 0;
	private long SUM_OF_PACKETS_RECVD=0;
	
	
	public int getTOTAL_NO_OF_PACKETS_SENT() {
		return TOTAL_NO_OF_PACKETS_SENT;
	}

	public void setTOTAL_NO_OF_PACKETS_SENT(int tOTALNOOFPACKETSSENT) {
		TOTAL_NO_OF_PACKETS_SENT = tOTALNOOFPACKETSSENT;
	}

	public OverlayNodeReportsTrafficSummary(){
		
	}
	
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public int getASSIGNED_ID() {
		return ASSIGNED_ID;
	}

	public void setASSIGNED_ID(int aSSIGNEDID) {
		ASSIGNED_ID = aSSIGNEDID;
	}

	

	public int getTOTAL_NO_OF_PACKETS_RELAYED() {
		return TOTAL_NO_OF_PACKETS_RELAYED;
	}

	public void setTOTAL_NO_OF_PACKETS_RELAYED(int tOTALNOOFPACKETSRELAYED) {
		TOTAL_NO_OF_PACKETS_RELAYED = tOTALNOOFPACKETSRELAYED;
	}

	public long getSUM_OF_PACKETS_SENT() {
		return SUM_OF_PACKETS_SENT;
	}

	public void setSUM_OF_PACKETS_SENT(long sUMOFPACKETSSENT) {
		SUM_OF_PACKETS_SENT = sUMOFPACKETSSENT;
	}

	public int getTOTAL_PACKETS_RECVD() {
		return TOTAL_PACKETS_RECVD;
	}

	public void setTOTAL_PACKETS_RECVD(int tOTALPACKETSRECVD) {
		TOTAL_PACKETS_RECVD = tOTALPACKETSRECVD;
	}

	public long getSUM_OF_PACKETS_RECVD() {
		return SUM_OF_PACKETS_RECVD;
	}

	public void setSUM_OF_PACKETS_RECVD(long sUMOFPACKETSRECVD) {
		SUM_OF_PACKETS_RECVD = sUMOFPACKETSRECVD;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nASSIGNED_ID:" + this.ASSIGNED_ID);
		str.append("\nTOTAL_NO_OF_PACKETS_STARTED:" + this.TOTAL_NO_OF_PACKETS_SENT);
		str.append("\nTOTAL_NO_OF_PACKETS_RELAYED:" + this.TOTAL_NO_OF_PACKETS_RELAYED);
		str.append("\nSUM_OF_PACKETS_SENT:" + this.SUM_OF_PACKETS_SENT);
		str.append("\nTOTAL_PACKETS_RECVD:" + this.TOTAL_PACKETS_RECVD);
		str.append("\nSUM_OF_PACKETS_RECVD:" + this.SUM_OF_PACKETS_RECVD);
		return str.toString();

	}
	
	public OverlayNodeReportsTrafficSummary(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		
		// INT
		ASSIGNED_ID = din.readInt();
		
		// INT
		TOTAL_NO_OF_PACKETS_SENT = din.readInt();
		
		// INT
		TOTAL_NO_OF_PACKETS_RELAYED = din.readInt();
		
		// LONG
		SUM_OF_PACKETS_SENT = din.readLong();
		
		// INT
		TOTAL_PACKETS_RECVD = din.readInt();
		
		// LONG
		SUM_OF_PACKETS_RECVD = din.readLong();
		
		
		baIn.close();
		din.close();
	}
	
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		byte[] marshallBytes=null;
		ByteArrayOutputStream baOut=new ByteArrayOutputStream();
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(baOut));
	
		try {

			// STRING
			byte[] data = MESSAGE_TYPE.getBytes();
			int datalength = data.length;
			dout.writeInt(datalength);
			dout.write(data, 0, datalength);

			// INT
			dout.writeInt(ASSIGNED_ID);
			
			// INT
			dout.writeInt(TOTAL_NO_OF_PACKETS_SENT);
			
			// INT
			dout.writeInt(TOTAL_NO_OF_PACKETS_RELAYED);
			
			// LONG
			dout.writeLong(SUM_OF_PACKETS_SENT);
			
			// INT
			dout.writeInt(TOTAL_PACKETS_RECVD);
			
			// LONG
			dout.writeLong(SUM_OF_PACKETS_RECVD);
			
			dout.flush();
			marshallBytes = baOut.toByteArray();

			baOut.close();
			dout.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return marshallBytes;
	}
	public int getType(){
		return 0;
	}

	public void writeBytes(byte[] marshall) {
		// TODO Auto-generated method stub
		
	}

}
