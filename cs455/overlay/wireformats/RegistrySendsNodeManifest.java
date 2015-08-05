package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistrySendsNodeManifest implements Event{
	
	private String MESSAGE_TYPE = null;
	private String ROUTING_TABLE_SIZE = null;
	
	private int NODEID_1_HOP = 0;
	private String LEN_1_HOP=null;
	private String IPADDRESS_1_HOP=null;
	private int PORTNO_1_HOP = 0;
	
	private int NODEID_2_HOP = 0;
	private String LEN_2_HOP=null;
	private String IPADDRESS_2_HOP=null;
	private int PORTNO_2_HOP = 0;
	
	private int NODEID_4_HOP = 0;
	private String LEN_4_HOP=null;
	private String IPADDRESS_4_HOP=null;
	private int PORTNO_4_HOP = 0;
	
	private String NUMBER_OF_IDS=null;
	private String LIST_OF_ALL_NODEIDS=null;
	
	
	public String getLIST_OF_ALL_NODEIDS() {
		return LIST_OF_ALL_NODEIDS;
	}


	public void setLIST_OF_ALL_NODEIDS(String lISTOFALLNODEIDS) {
		LIST_OF_ALL_NODEIDS = lISTOFALLNODEIDS;
	}


	public RegistrySendsNodeManifest(){
		
	}

	
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}


	public void setMESSAGE_TYPE(String mESSAGETYPE) {
		MESSAGE_TYPE = mESSAGETYPE;
	}


	public String getROUTING_TABLE_SIZE() {
		return ROUTING_TABLE_SIZE;
	}


	public void setROUTING_TABLE_SIZE(String rOUTINGTABLESIZE) {
		ROUTING_TABLE_SIZE = rOUTINGTABLESIZE;
	}


	public int getNODEID_1_HOP() {
		return NODEID_1_HOP;
	}


	public void setNODEID_1_HOP(int nODEID_1HOP) {
		NODEID_1_HOP = nODEID_1HOP;
	}


	public String getLEN_1_HOP() {
		return LEN_1_HOP;
	}


	public void setLEN_1_HOP(String lEN_1HOP) {
		LEN_1_HOP = lEN_1HOP;
	}


	public String getIPADDRESS_1_HOP() {
		return IPADDRESS_1_HOP;
	}


	public void setIPADDRESS_1_HOP(String iPADDRESS_1HOP) {
		IPADDRESS_1_HOP = iPADDRESS_1HOP;
	}


	public int getPORTNO_1_HOP() {
		return PORTNO_1_HOP;
	}


	public void setPORTNO_1_HOP(int pORTNO_1HOP) {
		PORTNO_1_HOP = pORTNO_1HOP;
	}


	public int getNODEID_2_HOP() {
		return NODEID_2_HOP;
	}


	public void setNODEID_2_HOP(int nODEID_2HOP) {
		NODEID_2_HOP = nODEID_2HOP;
	}


	public String getLEN_2_HOP() {
		return LEN_2_HOP;
	}


	public void setLEN_2_HOP(String lEN_2HOP) {
		LEN_2_HOP = lEN_2HOP;
	}


	public String getIPADDRESS_2_HOP() {
		return IPADDRESS_2_HOP;
	}


	public void setIPADDRESS_2_HOP(String iPADDRESS_2HOP) {
		IPADDRESS_2_HOP = iPADDRESS_2HOP;
	}


	public int getPORTNO_2_HOP() {
		return PORTNO_2_HOP;
	}


	public void setPORTNO_2_HOP(int pORTNO_2HOP) {
		PORTNO_2_HOP = pORTNO_2HOP;
	}


	public int getNODEID_4_HOP() {
		return NODEID_4_HOP;
	}


	public void setNODEID_4_HOP(int nODEID_4HOP) {
		NODEID_4_HOP = nODEID_4HOP;
	}


	public String getLEN_4_HOP() {
		return LEN_4_HOP;
	}


	public void setLEN_4_HOP(String lEN_4HOP) {
		LEN_4_HOP = lEN_4HOP;
	}


	public String getIPADDRESS_4_HOP() {
		return IPADDRESS_4_HOP;
	}


	public void setIPADDRESS_4_HOP(String iPADDRESS_4HOP) {
		IPADDRESS_4_HOP = iPADDRESS_4HOP;
	}


	public int getPORTNO_4_HOP() {
		return PORTNO_4_HOP;
	}


	public void setPORTNO_4_HOP(int pORTNO_4HOP) {
		PORTNO_4_HOP = pORTNO_4HOP;
	}


	public String getNUMBER_OF_IDS() {
		return NUMBER_OF_IDS;
	}


	public void setNUMBER_OF_IDS(String nUMBEROFIDS) {
		NUMBER_OF_IDS = nUMBEROFIDS;
	}




	public String toString() {
		StringBuilder str = new StringBuilder();
		int i=0;
		str.append("\nMESSAGE_TYPE:" + this.MESSAGE_TYPE);
		str.append("\nROUTING_TABLE_SIZE:" + this.ROUTING_TABLE_SIZE);
		
		str.append("\nNODEID_1_HOP:" + this.NODEID_1_HOP);
		str.append("\nLEN_1_HOP:" + this.LEN_1_HOP);
		str.append("\nIPADDRESS_1_HOP:" + this.IPADDRESS_1_HOP);
		str.append("\nPORTNO_1_HOP:" + this.PORTNO_1_HOP);
		i++;
		if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
		str.append("\nNODEID_2_HOP:" + this.NODEID_2_HOP);
		str.append("\nLEN_2_HOP:" + this.LEN_2_HOP);
		str.append("\nIPADDRESS_2_HOP:" + this.IPADDRESS_2_HOP);
		str.append("\nPORTNO_2_HOP:" + this.PORTNO_2_HOP);
		i++;
		if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
		str.append("\nNODEID_4_HOP:" + this.NODEID_4_HOP);
		str.append("\nLEN_4_HOP:" + this.LEN_4_HOP);
		str.append("\nIPADDRESS_4_HOP:" + this.IPADDRESS_4_HOP);
		str.append("\nPORTNO_4_HOP:" + this.PORTNO_4_HOP);
		
		}
		}		
		str.append("\n Number of node IDs in the system:" + this.NUMBER_OF_IDS);
		str.append("\nList of all node IDs in the system:" + this.LIST_OF_ALL_NODEIDS);
		
		return str.toString();

	}
	
	public RegistrySendsNodeManifest(byte[] marshall) throws IOException{
		ByteArrayInputStream baIn=new ByteArrayInputStream(marshall);
		DataInputStream din= new DataInputStream(new BufferedInputStream(baIn));
		
		// STRING
		int datalength = din.readInt();
		byte[] data = new byte[datalength];
		din.readFully(data);
		MESSAGE_TYPE = new String(data);
		int i=0;
		// STRING
		int datalength2 = din.readInt();
		byte[] data2 = new byte[datalength2];
		din.readFully(data2);
		ROUTING_TABLE_SIZE = new String(data2);
		//HOP I
		// INT
		NODEID_1_HOP = din.readInt();		
		// STRING
		int datalength3 = din.readInt();
		byte[] data3 = new byte[datalength3];
		din.readFully(data3);
		LEN_1_HOP = new String(data3);		
		// STRING
		int datalength4 = din.readInt();
		byte[] data4 = new byte[datalength4];
		din.readFully(data4);
		IPADDRESS_1_HOP = new String(data4);		
		// INT
		PORTNO_1_HOP = din.readInt();		
		
		i++;
		if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
		//HOP II
		// INT
		NODEID_2_HOP = din.readInt();		
		// STRING
		int datalength5 = din.readInt();
		byte[] data5 = new byte[datalength5];
		din.readFully(data5);
		LEN_2_HOP = new String(data5);		
		// STRING
		int datalength6 = din.readInt();
		byte[] data6 = new byte[datalength6];
		din.readFully(data6);
		IPADDRESS_2_HOP = new String(data6);		
		// INT
		PORTNO_2_HOP = din.readInt();		
		
		i++;
		if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
		//HOP III
		// INT
		NODEID_4_HOP = din.readInt();		
		// STRING
		int datalength7 = din.readInt();
		byte[] data7 = new byte[datalength7];
		din.readFully(data7);
		LEN_4_HOP = new String(data7);		
		// STRING
		int datalength8 = din.readInt();
		byte[] data8 = new byte[datalength8];
		din.readFully(data8);
		IPADDRESS_4_HOP = new String(data8);		
		// INT
		PORTNO_4_HOP = din.readInt();		
		}
		}
		// STRING
		int datalength9 = din.readInt();
		byte[] data9 = new byte[datalength9];
		din.readFully(data9);
		NUMBER_OF_IDS = new String(data9);
		
		// STRING
		int datalength10 = din.readInt();
		byte[] data10 = new byte[datalength10];
		din.readFully(data10);
		LIST_OF_ALL_NODEIDS = new String(data10);
		/*
		// INT
		LIST_OF_ALL_NODEIDS = din.readInt();
		*/
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
			byte[] data2 = ROUTING_TABLE_SIZE.getBytes();
			int datalength2 = data2.length;
			dout.writeInt(datalength2);
			dout.write(data2, 0, datalength2);
			int i=0;
			//////////////HOP I/////////
			// INT
			dout.writeInt(NODEID_1_HOP);
			
			// STRING
			byte[] data3 = LEN_1_HOP.getBytes();
			int datalength3 = data3.length;
			dout.writeInt(datalength3);
			dout.write(data3, 0, datalength3);
			
			// STRING
			byte[] data4 = IPADDRESS_1_HOP.getBytes();
			int datalength4 = data4.length;
			dout.writeInt(datalength4);
			dout.write(data4, 0, datalength4);
			
			// INT
			dout.writeInt(PORTNO_1_HOP);
			i++;
			if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
			//////////////HOP II/////////
			// INT
			dout.writeInt(NODEID_2_HOP);
			
			// STRING
			byte[] data5 = LEN_2_HOP.getBytes();
			int datalength5 = data5.length;
			dout.writeInt(datalength5);
			dout.write(data5, 0, datalength5);
			
			// STRING
			byte[] data6 = IPADDRESS_2_HOP.getBytes();
			int datalength6 = data6.length;
			dout.writeInt(datalength6);
			dout.write(data6, 0, datalength6);
			
			// INT
			dout.writeInt(PORTNO_2_HOP);
			i++;
			if(i<Integer.parseInt(this.ROUTING_TABLE_SIZE)){
			//////////////HOP III/////////
			// INT
			dout.writeInt(NODEID_4_HOP);
			
			// STRING
			byte[] data7 = LEN_4_HOP.getBytes();
			int datalength7 = data7.length;
			dout.writeInt(datalength7);
			dout.write(data7, 0, datalength7);
			
			// STRING
			byte[] data8 = IPADDRESS_4_HOP.getBytes();
			int datalength8 = data8.length;
			dout.writeInt(datalength8);
			dout.write(data8, 0, datalength8);
			
			// INT
			dout.writeInt(PORTNO_4_HOP);
			
			}
			}
			// STRING
			byte[] data9 = NUMBER_OF_IDS.getBytes();
			int datalength9 = data9.length;
			dout.writeInt(datalength9);
			dout.write(data9, 0, datalength9);
			
			// STRING
			byte[] data10 = LIST_OF_ALL_NODEIDS.getBytes();
			int datalength10 = data10.length;
			dout.writeInt(datalength10);
			dout.write(data10, 0, datalength10);
			
			/*
			// INT
			dout.writeInt(LIST_OF_ALL_NODEIDS);
			*/
			
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
