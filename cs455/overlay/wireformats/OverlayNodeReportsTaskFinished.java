package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeReportsTaskFinished implements Event{
	
	private String MESSAGE_TYPE = null;
	private String IPADDRESS = null;
	private int PORT_NUMBER = 0;
	private int NODEID = 0;
	
	public OverlayNodeReportsTaskFinished(){
		
	}

	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public String getIPADDRESS() {
		return IPADDRESS;
	}

	public void setIPADDRESS(String iPADDRESS) {
		IPADDRESS = iPADDRESS;
	}

	public int getPORT_NUMBER() {
		return PORT_NUMBER;
	}

	public void setPORT_NUMBER(int pORTNUMBER) {
		PORT_NUMBER = pORTNUMBER;
	}

	public int getNODEID() {
		return NODEID;
	}

	public void setNODEID(int nODEID) {
		NODEID = nODEID;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nIPADDRESS:" + this.IPADDRESS);
		str.append("\nPORT_NUMBER:" + this.PORT_NUMBER);
		str.append("\nNODEID:" + this.NODEID);
		return str.toString();

	}
	
	public OverlayNodeReportsTaskFinished(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		
		// STRING
		int datalength2 = din.readInt();
		byte[] data2 = new byte[datalength2];
		din.readFully(data2);
		IPADDRESS = new String(data2);
		
		// INT
		PORT_NUMBER = din.readInt();
		
		// INT
		NODEID = din.readInt();
		
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

			// STRING
			byte[] data2 = IPADDRESS.getBytes();
			int datalength2 = data2.length;
			dout.writeInt(datalength2);
			dout.write(data2, 0, datalength2);
			
			// INT
			dout.writeInt(PORT_NUMBER);
			
			// INT
			dout.writeInt(NODEID);
			
			
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
