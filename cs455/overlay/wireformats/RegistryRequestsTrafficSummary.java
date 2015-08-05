package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistryRequestsTrafficSummary implements Event{

	private String MESSAGE_TYPE = null;
	public RegistryRequestsTrafficSummary(){
		
	}
	
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		return str.toString();
	}
	
	public RegistryRequestsTrafficSummary(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		
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
