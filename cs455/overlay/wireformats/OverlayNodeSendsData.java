package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeSendsData implements Event {

	private String MESSAGE_TYPE = null;
	private int DESTINATION_ID = 0;
	private int SOURCE_ID = 0;
	private int PAYLOAD = 0;
	private int DISSE_TRACE_FIELD_LEN = 0;
	private String  DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED= null;
	
	public OverlayNodeSendsData(){
		
	}
	
	public void setDISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED(
			String dISSETRACECOMPRISENODESPACKETSTRAVERSED) {
		DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED = dISSETRACECOMPRISENODESPACKETSTRAVERSED;
	}

	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public int getDESTINATION_ID() {
		return DESTINATION_ID;
	}

	public void setDESTINATION_ID(int dESTINATIONID) {
		DESTINATION_ID = dESTINATIONID;
	}

	public int getSOURCE_ID() {
		return SOURCE_ID;
	}

	public void setSOURCE_ID(int sOURCEID) {
		SOURCE_ID = sOURCEID;
	}

	public int getPAYLOAD() {
		return PAYLOAD;
	}

	public void setPAYLOAD(int pAYLOAD) {
		PAYLOAD = pAYLOAD;
	}

	public int getDISSE_TRACE_FIELD_LEN() {
		return DISSE_TRACE_FIELD_LEN;
	}

	public void setDISSE_TRACE_FIELD_LEN(int dISSETRACEFIELDLEN) {
		DISSE_TRACE_FIELD_LEN = dISSETRACEFIELDLEN;
	}

	public String getDISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED() {
		return DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED;
	}

	

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nDESTINATION_ID:" + this.DESTINATION_ID);
		str.append("\nSOURCE_ID:" + this.SOURCE_ID);
		str.append("\nPAYLOAD:" + this.PAYLOAD);
		str.append("\nDISSEMINATION TRACE FIELD LENGTH(No.of hops):" + this.DISSE_TRACE_FIELD_LEN);
		str.append("\nDISSEMINATION TRACE COMPRISING NODEIDS THAT THE PACKET TRAVERSED THROUGH:" + this.DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED);
		return str.toString();

	}
	public OverlayNodeSendsData(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		
		// INT
		DESTINATION_ID = din.readInt();
		
		// INT
		SOURCE_ID = din.readInt();
		
		// INT
		PAYLOAD = din.readInt();
		
		// INT
		DISSE_TRACE_FIELD_LEN = din.readInt();
		/*
		// INT
		DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED = din.readInt();
		*/
		// STRING
		int datalength2 = din.readInt();
		byte[] data2 = new byte[datalength2];
		din.readFully(data2);
		DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED = new String(data2);
		
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
			dout.writeInt(DESTINATION_ID);
			
			// INT
			dout.writeInt(SOURCE_ID);
			
			// INT
			dout.writeInt(PAYLOAD);
			
			// INT
			dout.writeInt(DISSE_TRACE_FIELD_LEN);
			/*
			// INT
			dout.writeInt(DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED);
			*/
			
			// STRING
			byte[] data2 = DISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED.getBytes();
			int datalength2 = data2.length;
			dout.writeInt(datalength2);
			dout.write(data2, 0, datalength2);
			
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
