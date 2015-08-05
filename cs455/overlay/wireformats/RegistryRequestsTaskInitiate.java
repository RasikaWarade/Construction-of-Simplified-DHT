package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistryRequestsTaskInitiate implements Event{

	private String MESSAGE_TYPE = null;
	private int NO_OF_DATA_PACKETS_TO_SEND = 0;
	
	public RegistryRequestsTaskInitiate(){
		
	}
	
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public int getNO_OF_DATA_PACKETS_TO_SEND() {
		return NO_OF_DATA_PACKETS_TO_SEND;
	}

	public void setNO_OF_DATA_PACKETS_TO_SEND(int nOOFDATAPACKETSTOSEND) {
		NO_OF_DATA_PACKETS_TO_SEND = nOOFDATAPACKETSTOSEND;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nNO_OF_DATA_PACKETS_TO_SEND:" + this.NO_OF_DATA_PACKETS_TO_SEND);
		return str.toString();

	}

	
	public RegistryRequestsTaskInitiate(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		
		// INT
		NO_OF_DATA_PACKETS_TO_SEND = din.readInt();
		
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
			dout.writeInt(NO_OF_DATA_PACKETS_TO_SEND);

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
