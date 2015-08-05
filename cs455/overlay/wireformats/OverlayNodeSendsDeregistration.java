package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeSendsDeregistration implements Event {

	private String MESSAGE_TYPE = null;
	private String LENGTH = null;
	private String IPADDRESS = null;
	private int PORT_NUMBER = 0;
	private int NODE_ID = 0;

	public OverlayNodeSendsDeregistration() {

	}

	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}

	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}

	public String getLENGTH() {
		return LENGTH;
	}

	public void setLENGTH(String lENGTH) {
		LENGTH = lENGTH;
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

	public int getNODE_ID() {
		return NODE_ID;
	}

	public void setNODE_ID(int nODEID) {
		NODE_ID = nODEID;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nLENGTH:" + this.LENGTH);
		str.append("\nIPADDRESS:" + this.IPADDRESS);
		str.append("\nPORT_NUMBER:" + this.PORT_NUMBER);
		str.append("\nNODE_ID:" + this.NODE_ID);
		return str.toString();
	}

	public OverlayNodeSendsDeregistration(byte[] marshall) throws IOException {
		ByteArrayInputStream baIn = new ByteArrayInputStream(marshall);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baIn));

		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);

		// STRING
		int datalength2 = din.readInt();
		byte[] data2 = new byte[datalength2];
		din.readFully(data2);
		LENGTH = new String(data2);

		// STRING
		int datalength3 = din.readInt();
		byte[] data3 = new byte[datalength3];
		din.readFully(data3);
		IPADDRESS = new String(data3);

		// INT
		PORT_NUMBER = din.readInt();

		// INT
		NODE_ID = din.readInt();
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

			// STRING
			byte[] data2 = LENGTH.getBytes();
			int datalength2 = data2.length;
			dout.writeInt(datalength2);
			dout.write(data2, 0, datalength2);

			// STRING
			byte[] data3 = IPADDRESS.getBytes();
			int datalength3 = data3.length;
			dout.writeInt(datalength3);
			dout.write(data3, 0, datalength3);

			// INT
			dout.writeInt(PORT_NUMBER);

			// INT
			dout.writeInt(NODE_ID);
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
