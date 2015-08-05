package cs455.overlay.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import cs455.overlay.wireformats.NodeReportsOverlaySetupStatus;
import cs455.overlay.wireformats.OverlayNodeReportsTaskFinished;
import cs455.overlay.wireformats.OverlayNodeReportsTrafficSummary;
import cs455.overlay.wireformats.OverlayNodeSendsData;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.PrintStatistics;
import cs455.overlay.wireformats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireformats.RegistryRequestsTaskInitiate;
import cs455.overlay.wireformats.RegistryRequestsTrafficSummary;
import cs455.overlay.wireformats.RegistrySendsNodeManifest;

public class MessagingNode {

	// Declare the variables
	ServerSocket serv = null;
	Socket connection = null;
	String clientIP = null;

	static int currPort = 0;
	static private DataOutputStream out = null;
	static private DataInputStream in = null;
	static private int MYNODEID=-1;
	static private String MYIP=null;
	static private int MYPORT=0;
	
	static private RoutingTable fingerTable=new RoutingTable();
	static private String listOfNodes="";
	static private int NoOfNodes=0;
	static private int column=0;
	static private int sendTracker=0;
	static private int recvTracker=0;
	static private int relayTracker=0;
	
	static private long sendSummation=0;
	static private long recvSummation=0;
	static private StorageNode s_n = new StorageNode();
	
	ArrayList<RountingEntry> entries=new ArrayList<RountingEntry>();
	private static PrintStatistics stat=new PrintStatistics();
	 public static synchronized void incrementSendTracker() {
		 sendTracker++;
	 }
	 public static synchronized void incrementRecvTracker() {
		 recvTracker++;
	 }
	 public static synchronized void incrementRelayTracker() {
		 relayTracker++;
	 }
	 public static synchronized void incrementSendSum(long payload) {
		 sendSummation+=payload;
	 }
	 public static synchronized void incrementRecvSum(long payload) {
		 recvSummation+=payload;
	 }
	public int findSucc(int destID){
		int succ=-1;
		
		// Check if the key is in the starts for the node
		for (int p = 0; p < column; p++) {

			// Find the successor now for node
			if (destID == s_n.start[p]) {
				succ = s_n.start[p];
				// System.out.println("SUCC  (2)"+"  "+succ);
				return succ;

			}
		}
		

		// Check if the key is in the intervals for the node
		for (int p = 0; p < column; p++) {
			if (s_n.start_int[p] <= s_n.end_int[p]) {
				if ((destID >= s_n.start_int[p])
						&& (destID <= s_n.end_int[p])) {
					// System.out.println("FOund in interval(1) Start:"+s_n.start_int[p]+"  End:"+s_n.end_int[p]);
					succ = s_n.start[p];
					// succ=findSucc(z,total,s_n,succ);
					//succ=findSucc(destID);
					return succ;
				}
			} else {
				if (((destID >= s_n.start_int[p]) && (destID >= s_n.end_int[p]))
						|| ((destID <= s_n.start_int[p]) && (destID <= s_n.end_int[p]))) {
					// System.out.println("FOund in interval(2) Start:"+s_n.start_int[p]+"  End:"+s_n.end_int[p]);
					succ = s_n.start[p];
					// succ=findSucc(z,total,s_n,succ);
					return succ;
				}
			}

		}
		
		return succ;
	}
	public static void sendData(OverlayNodeSendsData t, String host,
			int port) throws IOException, NoSuchAlgorithmException {
		// Declare the variables
		Socket clientSocket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			// create socket at client
			clientSocket = new Socket(host, port);
			System.out.println("Local Port:"+clientSocket.getLocalPort());
			System.out.println("Connecting nodet:" + host + " and Port: "
					+ port);
			// initialize input and output streams over the socket to perform
			// read and write with the server
			out = new DataOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + host);
			
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ host);
			e.printStackTrace();
			
		}

		// checking marshalling
		byte[] sent = t.getBytes();
		int sentlength = sent.length;
		out.writeInt(sentlength);
		// Give the command to register the node with BootStrap Server
		out.write(sent, 0, sentlength);
		out.flush();		
		
		
		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
		
	}
	
	void run() throws NoSuchAlgorithmException, InterruptedException {
		try {

			serv = new ServerSocket(currPort, 3000000); // create new server
			// socket
			
			System.out.println("Serv Local Port:"+serv.getLocalPort());
			System.out.println("ClientServerSocket Created");
			System.out.println("Waiting for connection on port no : "
					+ currPort);
			// Accepting the connections
			connection = serv.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());

			// output and input stream
			out = new DataOutputStream(connection.getOutputStream());
			out.flush();
			in = new DataInputStream(connection.getInputStream());

			// Listen on the socket for the reply
			int datalength2 = 0;
			if ((datalength2 = in.readInt()) != 0) {
				byte[] data2 = new byte[datalength2];
				in.readFully(data2, 0, datalength2);
				String recv = new String(data2);
				if(recv.contains("PRINT")){
					System.out.println("Getting Statistics Information for the node from Registry!");
					System.out.println("Packets Sent:"+stat.getSendTracker());
					System.out.println("Packets Received:"+stat.getRecvTracker());
					System.out.println("Packets Relayed:"+stat.getRelayTracker());
					System.out.println("Packets Sum of all send packets:"+stat.getSendSummation());
					System.out.println("Packets Sum of all received packets:"+stat.getRecvSummation());
					
				}else
				if(recv.contains("REGISTRY_REQUESTS_TRAFFIC_SUMMARY")){
				System.out.println("server>" + recv);
				
				/*
				// checking unmarshalling
				RegistryRequestsTrafficSummary st=new RegistryRequestsTrafficSummary(data2);
				if(st.getMESSAGE_TYPE().equalsIgnoreCase(
				"REGISTRY_REQUESTS_TRAFFIC_SUMMARY")){
				System.out.println(st.toString());
				*/
				//Report the summary to the registry
				
				//send the packet
				
				OverlayNodeReportsTrafficSummary t = new OverlayNodeReportsTrafficSummary();

				t.setMESSAGE_TYPE("OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY");
				t.setASSIGNED_ID(MYNODEID);
				t.setTOTAL_NO_OF_PACKETS_SENT(sendTracker);
				t.setTOTAL_NO_OF_PACKETS_RELAYED(relayTracker);
				t.setSUM_OF_PACKETS_SENT(sendSummation);
				t.setTOTAL_PACKETS_RECVD(recvTracker);
				t.setSUM_OF_PACKETS_RECVD(recvSummation);
				
				System.out.println("MESSAGE>");
				System.out.println(t.toString());
				

				// checking marshalling
				byte[] sent = t.getBytes();
				// System.out.println("client>:"+ss);

				int sentlength = sent.length;
				out.writeInt(sentlength);
				// Give the command to register the node with BootStrap
				// Server
				out.write(sent, 0, sentlength);
				out.flush();
				
				//reset the counters***
				stat.setRecvSummation(recvSummation);
				stat.setRecvTracker(recvTracker);
				stat.setRelayTracker(relayTracker);
				stat.setSendTracker(sendTracker);
				stat.setSendSummation(sendSummation);
				sendTracker=0;
				relayTracker=0;
				recvTracker=0;
				sendSummation=0;
				recvSummation=0;
				
				}
				else{
					RegistryRequestsTaskInitiate t1=new RegistryRequestsTaskInitiate(data2);
					if (t1.getMESSAGE_TYPE().equalsIgnoreCase(
				
				"REGISTRY_REQUESTS_TASK_INITIATE")) {
						//Thread.sleep(5000);
						
				System.out.println("MESSAGE:>"+t1.toString());
				System.out.println("Task initiated at this system:");
				
				System.out.println("Number of data packets to be sent:"+t1.getNO_OF_DATA_PACKETS_TO_SEND());
				
				int noOfPackets=t1.getNO_OF_DATA_PACKETS_TO_SEND();
				//list of node identifiers
				String delims=" ";
				//System.out.println("list:"+listOfNodes);
				String tokens[]=(listOfNodes.trim()).split(delims);
				int[] listNo=new int [NoOfNodes];
				for(int p=0;p<tokens.length;p++){
					listNo[p]=Integer.parseInt(tokens[p].trim());
				}
				
				
				
				//remove mynodeid from the list
				int[] useLIST=new int[NoOfNodes-1];
				int p=0;
				for(int l=0;l<NoOfNodes;l++){
					if(MYNODEID==listNo[l]){
						//do nothing
					}else{
						useLIST[p]=listNo[l];
						p++;
					}
				}
				System.out.print("Used List of all node identifiers: [");
				
				for (int i = 0; i < useLIST.length; i++) {
					System.out.print(useLIST[i] + "\t");
				}
				System.out.println("]");
				//Use routingInfo
				System.out.println("Finger Table:");
				fingerTable.printTable();
				
				//Generate finger table
				
				s_n.no = MYNODEID;
				s_n.start = new int[column];
				s_n.succ = new int[column];
				s_n.start_int = new int[column];
				s_n.end_int = new int[column];
				
				
				// Generating Finger tables for self storage node
				RountingEntry rEntry =new RountingEntry();
				for(int i=0;i<entries.size();i++){
					rEntry=entries.get(i);
					s_n.start[i]=rEntry.getNodeid();
					
				}
				// Interval
				for (int v = 0; v < column; v++)
					for (int u = 0; u < column; u++) {
						s_n.start_int[u] = s_n.start[u];

						if ((u + 1) >= column) {

							s_n.end_int[u] = s_n.start[(u + 1) % column];

						} else
							s_n.end_int[u] = s_n.start[u + 1];

					}
				/*
				// Generating successors for each entry
				for (int k = 0; k < column; k++) {// create the rows in
					// finger tables 'm'
					int j = 0;
					while (j < column && s_n.start[k] > s_n.start[j]) {
						j++;
					}
					if (j >= column) {
						s_n.succ[k] = s_n.start[0];

					} else {
						s_n.succ[k] = s_n.start[j];
					}

				}
				*/
				System.out.println("FT:>");
				for(int i=0;i<column;i++){
					System.out.println(s_n.start[i]+"\t"+s_n.start_int[i]+"\t"+s_n.end_int[i]);
				}
				
				for(int i=0;i<noOfPackets;i++){
					//Select a random payload
					int payload=0;
					Random rand = new Random();
					payload=rand.nextInt();
					
					//select random destination
					Random generator = new Random();
					int randomIndex = generator.nextInt(useLIST.length);
					
					//OVERLAY_NODE_SENDS_DATA
					OverlayNodeSendsData dt=new OverlayNodeSendsData();
					dt.setMESSAGE_TYPE("OVERLAY_NODE_SENDS_DATA");
					dt.setDESTINATION_ID(useLIST[randomIndex]);
					dt.setSOURCE_ID(MYNODEID);
					dt.setPAYLOAD(payload);
					dt.setDISSE_TRACE_FIELD_LEN(0);
					dt.setDISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED("");
					
					System.out.println("MESSAGE:>");
					System.out.println(dt.toString());
					//Increment no of hops***
					//Add the current nodeid in the list if not source and sink***
					
					//use routing logic!
					int succ=findSucc(useLIST[randomIndex]);
					System.out.println("DEST:"+useLIST[randomIndex]+"\t SUCC:"+succ);
					
					String destHost="";
					int destPort=0;
					
					RountingEntry rEntry2 =new RountingEntry();
					for(int f=0;f<entries.size();f++){
						rEntry2=entries.get(f);
						if(rEntry2.getNodeid()==succ){
							destHost=rEntry2.getRNodeIP();
							destPort=rEntry2.getRNodePort();
						}
						
					}
					sendData(dt,destHost,destPort);
					//Send the packet
					System.out.println("Packet ["+(i+1)+"] sent! to host:"+destHost+" port:"+destPort );
					incrementSendTracker();
					incrementSendSum(payload);
				}
				
				//if successful...inform registry about the task finished
				OverlayNodeReportsTaskFinished t = new OverlayNodeReportsTaskFinished();

				t.setMESSAGE_TYPE("OVERLAY_NODE_REPORTS_TASK_FINISHED");
				InetAddress IPadd = InetAddress.getLocalHost();
				String currIP = IPadd.getHostAddress();
				t.setIPADDRESS(currIP);
				t.setPORT_NUMBER(currPort);
				t.setNODEID(MYNODEID);//***nodeid
				
				System.out.println("MESSAGE>");
				System.out.println(t.toString());
				// Successful Registration done

				// checking marshalling
				byte[] sent = t.getBytes();
				// System.out.println("client>:"+ss);

				int sentlength = sent.length;
				out.writeInt(sentlength);
				// Give the command to register the node with BootStrap
				// Server
				out.write(sent, 0, sentlength);
				out.flush();
				
			}else if(t1.getMESSAGE_TYPE().equalsIgnoreCase("OVERLAY_NODE_SENDS_DATA")){
				OverlayNodeSendsData dt=new OverlayNodeSendsData(data2);
				//list of node identifiers
				String delims=" ";
				//System.out.println("list:"+listOfNodes);
				String tokens[]=(listOfNodes.trim()).split(delims);
				int[] listNo=new int [NoOfNodes];
				for(int p=0;p<tokens.length;p++){
					listNo[p]=Integer.parseInt(tokens[p].trim());
				}
				
				
				
				//remove mynodeid from the list
				int[] useLIST=new int[NoOfNodes-1];
				int p=0;
				for(int l=0;l<NoOfNodes;l++){
					if(MYNODEID==listNo[l]){
						//do nothing
					}else{
						useLIST[p]=listNo[l];
						p++;
					}
				}
				System.out.print("Used List of all node identifiers: [");
				
				for (int i = 0; i < useLIST.length; i++) {
					System.out.print(useLIST[i] + "\t");
				}
				System.out.println("]");
				//Use routingInfo
				System.out.println("Finger Table:");
				fingerTable.printTable();
				
				//Generate finger table
				
				s_n.no = MYNODEID;
				s_n.start = new int[column];
				s_n.succ = new int[column];
				s_n.start_int = new int[column];
				s_n.end_int = new int[column];
				
				
				// Generating Finger tables for self storage node
				RountingEntry rEntry =new RountingEntry();
				for(int i=0;i<entries.size();i++){
					rEntry=entries.get(i);
					s_n.start[i]=rEntry.getNodeid();
					
				}
				// Interval
				for (int v = 0; v < column; v++)
					for (int u = 0; u < column; u++) {
						s_n.start_int[u] = s_n.start[u];

						if ((u + 1) >= column) {

							s_n.end_int[u] = s_n.start[(u + 1) % column];

						} else
							s_n.end_int[u] = s_n.start[u + 1];

					}
				/*
				// Generating successors for each entry
				for (int k = 0; k < column; k++) {// create the rows in
					// finger tables 'm'
					int j = 0;
					while (j < column && s_n.start[k] > s_n.start[j]) {
						j++;
					}
					if (j >= column) {
						s_n.succ[k] = s_n.start[0];

					} else {
						s_n.succ[k] = s_n.start[j];
					}

				}
				*/
				System.out.println("FT:>");
				for(int i=0;i<column;i++){
					System.out.println(s_n.start[i]+"\t"+s_n.start_int[i]+"\t"+s_n.end_int[i]);
				}
				
				//increment recvTracker
				int destID=dt.getDESTINATION_ID();
				if(destID==MYNODEID){
					System.out.println("PACKET:>"+dt.toString());
					//check if the source id matches the nodeid
					System.out.println("Matches!--------------"+MYNODEID);
					incrementRecvTracker();
					incrementRecvSum(dt.getPAYLOAD());
							
				}else{
				//else
				//find the succ for the node in the finger table and send the packet id
				
				System.out.println("Relayed-------------");
				incrementRelayTracker();
				int succ=findSucc(dt.getDESTINATION_ID());
				
				System.out.println("DEST:"+dt.getDESTINATION_ID()+"\t SUCC:"+succ);
				
				String destHost="";
				int destPort=0;
				
				RountingEntry rEntry2 =new RountingEntry();
				for(int f=0;f<entries.size();f++){
					rEntry2=entries.get(f);
					if(rEntry2.getNodeid()==succ){
						destHost=rEntry2.getRNodeIP();
						destPort=rEntry2.getRNodePort();
					}
					
				}
			
				//increment  the hop
				int hop=dt.getDISSE_TRACE_FIELD_LEN();
				dt.setDISSE_TRACE_FIELD_LEN(hop+1);
				//add the mynodeid to dissemination
				String node=dt.getDISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED();
				node=node+" "+String.valueOf(MYNODEID);
				dt.setDISSE_TRACE_COMPRISE_NODES_PACKETS_TRAVERSED(node);
				//System.out.println("RELAYED PACKET:>"+dt.toString());
				sendData(dt,destHost,destPort);
				//Send the packet
				
				
				}
			}else
				// check if registration or dereg request
				if (t1.getMESSAGE_TYPE().equalsIgnoreCase(
						"REGISTRY_SENDS_NODE_MANIFEST")) {
					RegistrySendsNodeManifest t2 = new RegistrySendsNodeManifest(
							data2);
					System.out.println("Registry :>" + t2.toString());
					//save the routing table***
					
					column=Integer.parseInt(t2.getROUTING_TABLE_SIZE());
					
					for(int i=0; i<Integer.parseInt(t2.getROUTING_TABLE_SIZE());i++){ //Now for default 3***
						RountingEntry rEntry =new RountingEntry();
						rEntry.setDistance((int) Math.pow(2,i));
						rEntry.setNodeid(t2.getNODEID_1_HOP());
						rEntry.setRNodeIP(t2.getIPADDRESS_1_HOP());
						rEntry.setRNodePort(t2.getPORTNO_1_HOP());
						i++;
						entries.add(rEntry);
						if(i<Integer.parseInt(t2.getROUTING_TABLE_SIZE())){
						RountingEntry rEntry1 =new RountingEntry();
						rEntry1.setDistance((int) Math.pow(2,i));
						rEntry1.setNodeid(t2.getNODEID_2_HOP());
						rEntry1.setRNodeIP(t2.getIPADDRESS_2_HOP());
						rEntry1.setRNodePort(t2.getPORTNO_2_HOP());
						i++;
						entries.add(rEntry1);
						if(i<Integer.parseInt(t2.getROUTING_TABLE_SIZE())){
						RountingEntry rEntry2 =new RountingEntry();
						rEntry2.setDistance((int) Math.pow(2,i));
						rEntry2.setNodeid(t2.getNODEID_4_HOP());
						rEntry2.setRNodeIP(t2.getIPADDRESS_4_HOP());
						rEntry2.setRNodePort(t2.getPORTNO_4_HOP());
						i++;
						entries.add(rEntry2);
						}
						}
						
						
					}
					InetAddress IPadd = InetAddress.getLocalHost();
					String currIP = IPadd.getHostAddress();
					fingerTable.setNodeID(MYNODEID);
					fingerTable.setNodeIP(MYIP);
					fingerTable.setNodePort(MYPORT);
					fingerTable.setRoutingTable(entries);
					
					listOfNodes=t2.getLIST_OF_ALL_NODEIDS();
					NoOfNodes=Integer.parseInt(t2.getNUMBER_OF_IDS());
					
					//saved routing table
					//send the response
					NodeReportsOverlaySetupStatus t = new NodeReportsOverlaySetupStatus();

					t.setMESSAGE_TYPE("NODE_REPORTS_OVERLAY_SETUP_STATUS");

					
					//Remove from the messaging node list***
					/*int assignedID=;
					for (int i = 0; i < list_MN.size(); i++) {
						MNInfo value = list_MN.get(i);
						if(value.getNODE_ID()==assignedID){
							list_MN.remove(value);
							System.out.println("assignedID "+assignedID+" removed from list!");
						}
					}*/
					// Assign -1 if failed
					t.setSUCCESS_STATUS(MYNODEID);//random.....****to be searched in the list and removed
					String info_string = "OVERLAY_SETUP done!";
					// if success
					t.setLENGTH_INFO(String.valueOf(info_string.length()));
					// if failed

					t.setINFORMATION_STRING(info_string);

					System.out.println("MESSAGE>");
					System.out.println(t.toString());
					// Successful Registration done

					// checking marshalling
					byte[] sent = t.getBytes();
					// System.out.println("client>:"+ss);

					int sentlength = sent.length;
					out.writeInt(sentlength);
					// Give the command to register the node with BootStrap
					// Server
					out.write(sent, 0, sentlength);
					out.flush();
				}
				
				
				}
			}

			
			

		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {

			try {
				in.close();
				out.close();
				connection.close();
				serv.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
}		
	public static void register(String reg_host, int reg_port)
			throws IOException, NoSuchAlgorithmException {
		// Declare the variables
		Socket clientSocket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			// create socket at client
			clientSocket = new Socket(reg_host, reg_port);
			System.out.println("get Port:"+clientSocket.getPort());
			System.out.println("Local Port:"+clientSocket.getLocalPort());
			

			// initialize input and output streams over the socket to perform
			// read and write with the server
			out = new DataOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + reg_host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ reg_host);
			e.printStackTrace();
			System.exit(1);
		}

		// get current ipaddress and port
		InetAddress IPadd = InetAddress.getLocalHost();
		String currIP = IPadd.getHostAddress();
		int currPort = reg_port;// assign any port by giving 0

		// Send the marshall bytes to server
		byte[] reg_request = new byte[1024];
		OverlayNodeSendsRegistration t = new OverlayNodeSendsRegistration();

		t.setMESSAGE_TYPE("OVERLAY_NODE_SENDS_REGISTRATION");
		t.setLENGTH(String.valueOf(currIP.length()));
		t.setIPADDRESS(currIP);
		t.setPORT_NUMBER(currPort);
		
		MYIP=currIP;
		MYPORT=currPort;

		System.out.println("MESSAGE>");
		System.out.println(t.toString());

		// checking marshalling
		byte[] sent = t.getBytes();
		String ss = new String(sent);
		// System.out.println("client>:"+ss);

		int sentlength = sent.length;
		out.writeInt(sentlength);
		// Give the command to register the node with BootStrap Server
		out.write(sent, 0, sentlength);
		out.flush();

		/*
		 * 
		 * //checking unmarshalling OverlayNodeSendsRegistration t2=new
		 * OverlayNodeSendsRegistration(sent);
		 * System.out.println("UNMARSHALLING:\n"+t2.toString());
		 */

		// Listen on the socket for the reply
		int datalength2 = 0;
		if ((datalength2 = in.readInt()) != 0) {
			byte[] data2 = new byte[datalength2];
			in.readFully(data2, 0, datalength2);
			// String recv = new String(data2);
			// System.out.println("server>" + recv);
			RegistryReportsRegistrationStatus t3 = new RegistryReportsRegistrationStatus(
					data2);
			System.out.println("server :>" + t3.toString());

			MYNODEID=t3.getSUCCESS_STATUS();
			
			// Node has been successfully registered with the BootStrap server

		}

		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
	}


	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.out.println("Please enter Registry_host Registry_portno");
		} else {
			String reg_host = args[0];
			int reg_port = Integer.parseInt(args[1]);

			// First register the MessagingNode with the Registry
			try {
				register(reg_host, reg_port);

				// Start the client and server
				MessagingNode server = new MessagingNode();
				MessagingNodeClient client = new MessagingNodeClient(reg_host,
						reg_port);
				client.start();
				// *****if server needed
				currPort=reg_port;
				while (true) {
					server.run();
				 }
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
