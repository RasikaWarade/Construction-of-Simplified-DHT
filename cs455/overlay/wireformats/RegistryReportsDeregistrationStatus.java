package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistryReportsDeregistrationStatus implements Event {

	private String MESSAGE_TYPE = null;
	private int SUCCESS_STATUS = 0;
	private String LENGTH_INFO = null;
	private String INFORMATION_STRING = null;

	public RegistryReportsDeregistrationStatus() {

	}

	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public int getSUCCESS_STATUS() {
		return SUCCESS_STATUS;
	}

	public void setSUCCESS_STATUS(int sUCCESSSTATUS) {
		SUCCESS_STATUS = sUCCESSSTATUS;
	}

	public String getLENGTH_INFO() {
		return LENGTH_INFO;
	}

	public void setLENGTH_INFO(String lENGTHINFO) {
		LENGTH_INFO = lENGTHINFO;
	}

	public String getINFORMATION_STRING() {
		return INFORMATION_STRING;
	}

	public void setINFORMATION_STRING(String iNFORMATIONSTRING) {
		INFORMATION_STRING = iNFORMATIONSTRING;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nSUCCESS_STATUS:" + this.SUCCESS_STATUS);
		str.append("\nLENGTH_INFO:" + this.LENGTH_INFO);
		str.append("\nINFORMATION_STRING:" + this.INFORMATION_STRING);
		return str.toString();

	}

	public RegistryReportsDeregistrationStatus(byte[] marshall)
			throws IOException {
		ByteArrayInputStream baIn = new ByteArrayInputStream(marshall);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baIn));

		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);

		// INT
		SUCCESS_STATUS = din.readInt();

		// STRING
		int datalength2 = din.readInt();
		byte[] data2 = new byte[datalength2];
		din.readFully(data2);
		LENGTH_INFO = new String(data2);

		// STRING
		int datalength3 = din.readInt();
		byte[] data3 = new byte[datalength3];
		din.readFully(data3);
		INFORMATION_STRING = new String(data3);

		baIn.close();
		din.close();
	}

	public byte[] getBytes() {
		// TODO Auto-generated method stub
		byte[] marshallBytes = null;
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(
				baOut));

		try {

			// STRING
			byte[] data = MESSAGE_TYPE.getBytes();
			int datalength = data.length;
			dout.writeInt(datalength);
			dout.write(data, 0, datalength);

			// INT
			dout.writeInt(SUCCESS_STATUS);

			// STRING
			byte[] data2 = LENGTH_INFO.getBytes();
			int datalength2 = data2.length;
			dout.writeInt(datalength2);
			dout.write(data2, 0, datalength2);

			// STRING
			byte[] data3 = INFORMATION_STRING.getBytes();
			int datalength3 = data3.length;
			dout.writeInt(datalength3);
			dout.write(data3, 0, datalength3);

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

	public int getType() {
		return 0;
	}

	public void writeBytes(byte[] marshall) {
		// TODO Auto-generated method stub

	}

}
