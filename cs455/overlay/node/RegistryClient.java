package cs455.overlay.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import cs455.overlay.wireformats.NodeReportsOverlaySetupStatus;
import cs455.overlay.wireformats.OverlayNodeReportsTaskFinished;
import cs455.overlay.wireformats.OverlayNodeReportsTrafficSummary;
import cs455.overlay.wireformats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireformats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireformats.RegistryRequestsTaskInitiate;
import cs455.overlay.wireformats.RegistryRequestsTrafficSummary;
import cs455.overlay.wireformats.RegistrySendsNodeManifest;

class TrafficSummary{
	private int NODEID=0;
	private int sentTracker=0;
	private int recvTracker=0;
	private int relayTracker=0;
	private long sendSum=0;
	private long recvSum=0;
	public int getNODEID() {
		return NODEID;
	}
	public void setNODEID(int nODEID) {
		NODEID = nODEID;
	}
	public int getSentTracker() {
		return sentTracker;
	}
	public void setSentTracker(int sentTracker) {
		this.sentTracker = sentTracker;
	}
	public int getRecvTracker() {
		return recvTracker;
	}
	public void setRecvTracker(int recvTracker) {
		this.recvTracker = recvTracker;
	}
	public int getRelayTracker() {
		return relayTracker;
	}
	public void setRelayTracker(int relayTracker) {
		this.relayTracker = relayTracker;
	}
	public long getSendSum() {
		return sendSum;
	}
	public void setSendSum(long sendSum) {
		this.sendSum = sendSum;
	}
	public long getRecvSum() {
		return recvSum;
	}
	public void setRecvSum(long recvSum) {
		this.recvSum = recvSum;
	}
	
}

class RountingEntry {
	private int Distance;
	private int nodeid;
	private String RNodeIP = null;
	private int RNodePort = 0;

	public String getRNodeIP() {
		return RNodeIP;
	}

	public void setRNodeIP(String rNodeIP) {
		RNodeIP = rNodeIP;
	}

	public int getRNodePort() {
		return RNodePort;
	}

	public void setRNodePort(int rNodePort) {
		RNodePort = rNodePort;
	}

	public int getDistance() {
		return Distance;
	}

	public void setDistance(int distance) {
		Distance = distance;
	}

	public int getNodeid() {
		return nodeid;
	}

	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}

	public void printEntry() {
		System.out.println(Distance + "\t" + nodeid + "\t" + RNodeIP + "\t"
				+ RNodePort);
	}
}

class RoutingTable {
	private String NodeIP = null;
	private int NodePort = 0;
	private int NodeID = 9999;
	ArrayList<RountingEntry> routingTable = new ArrayList<RountingEntry>();
	
	public String getNodeIP() {
		return NodeIP;
	}

	public void setNodeIP(String nodeIP) {
		NodeIP = nodeIP;
	}

	public int getNodePort() {
		return NodePort;
	}

	public void setNodePort(int nodePort) {
		NodePort = nodePort;
	}

	public int getNodeID() {
		return NodeID;
	}

	public void setNodeID(int nodeID) {
		NodeID = nodeID;
	}

	public ArrayList<RountingEntry> getRoutingTable() {
		return routingTable;
	}

	public void setRoutingTable(ArrayList<RountingEntry> routingTable) {
		this.routingTable = routingTable;
	}

	public void printTable() {
		System.out.println("IPADDRESS:" + NodeIP + "\tPORT:" + NodePort
				+ "\tNODEID:" + NodeID);
		System.out.println("Distance" + "\t NodeId" + "\t IP\t PORT");
		for (int i = 0; i < routingTable.size(); i++) {
			RountingEntry value = routingTable.get(i);
			value.printEntry();
		}
	}
}

class RegistryClient extends Thread {

	private int currPort = 0;

	static ArrayList<MNInfo> list_MN_client = new ArrayList<MNInfo>();
	static ArrayList<RoutingTable> list_RT = new ArrayList<RoutingTable>();
	static ArrayList<TrafficSummary> summary = new ArrayList<TrafficSummary>();
	private static int countFinished=0;
	private static int flagSetup=0;
	public RegistryClient(String port) {
		// TODO Auto-generated constructor stub
		currPort = Integer.parseInt(port);
	}

	public static ArrayList<RountingEntry> rtEntries(int[] list, int nr,
			int nodeid) {
		ArrayList<RountingEntry> routingTable = new ArrayList<RountingEntry>();

		for (int i = 1; i <= nr; i++) {
			RountingEntry re = new RountingEntry();
			for (int j = 0; j < list.length; j++) {
				// locate the location for the node in the list
				if (list[j] == nodeid) {
					re.setDistance((int) Math.pow(2, i - 1));
					double dist = j + Math.pow(2, i - 1);

					// if wraps up
					if ((int) dist > list.length - 1) {

						dist = dist % list.length;

					}
					int Node = list[(int) dist];
					re.setNodeid(Node);

					// set ip
					// set port

					for (int ll = 0; ll < list_MN_client.size(); ll++) {
						MNInfo val = list_MN_client.get(ll);
						// val.printMNInfo();
						// setip***
						// setport***
						if (val.getNODE_ID() == Node) {
							re.setRNodeIP(val.getIPADDRESS());
							re.setRNodePort(val.getPORT_NO());
						}

					}

				}
			}
			routingTable.add(re);
		}
		return routingTable;

	}

	public static int sendManifest(RegistrySendsNodeManifest t, String host,
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
			System.out.println("Error to send Manifest message!");
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

		int flag=0;
		// WAIT FOR RESPONSE MESSAGE: NODE_REPORTS_OVERLAY_SETUP_STATUS
		// Listen on the socket for the reply
		System.out.println("Waiting for NODE_REPORTS_OVERLAY_SETUP_STATUS from :"+host);
		int datalength2 = 0;
		if ((datalength2 = in.readInt()) != 0) {
			byte[] data2 = new byte[datalength2];
			in.readFully(data2, 0, datalength2);
			NodeReportsOverlaySetupStatus t3 = new NodeReportsOverlaySetupStatus(
					data2);
			System.out.println("server :>" + t3.toString());

			// Node has been successfully unregistered with the BootStrap server
			flag=1;
		}

		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
		return flag;
	}

	public static void sendInitiate(RegistryRequestsTaskInitiate t, String host,
			int port) throws IOException, NoSuchAlgorithmException, InterruptedException {
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
			// checking marshalling
			byte[] sent = t.getBytes();
			int sentlength = sent.length;
			out.writeInt(sentlength);
			// Give the command to register the node with BootStrap Server
			out.write(sent, 0, sentlength);
			out.flush();
			Thread.sleep(2000);
			
			try{
			// Listen on the socket for the reply
			System.out.println("Waiting for OVERLAY_NODE_REPORTS_TASK_FINISHED from :"+host);
			int datalength2 = 0;
			if(in.read()!=0){
			if ((datalength2 = in.readInt()) != 0) {
				byte[] data2 = new byte[datalength2];
				in.readFully(data2, 0, datalength2);
				OverlayNodeReportsTaskFinished t3 = new OverlayNodeReportsTaskFinished(
						data2);
				System.out.println("server :>" + t3.toString());

				// Node has been successfully unregistered with the BootStrap server
				countFinished++;
			}
			}
			}catch(EOFException e){
				System.out.println("Could not read the data ...Sorry!");
			}
			
			
			// close socket connection when command is executed
			// close input and output streams
			clientSocket.close();
			in.close();
			out.close();
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + host);
			
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ host);
			e.printStackTrace();
			
		}

		
		
	}
	public void run() {
		String value2 = null;
		String delims = " ";
		String[] tokens = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			value2 = br.readLine();
			while (value2 != null || value2.equalsIgnoreCase("quit")) {
				System.out.println("Command: " + value2);
				// Registry commands
				
				if (value2.equalsIgnoreCase("list-messaging-nodes")) {
					System.out.println("MESSAGING NODES (IP PORT NODEID):>");
					File file = new File("keys.txt");
					FileReader fr = new FileReader(file.getName());
					BufferedReader textr = new BufferedReader(fr);
					String lin4 = null;
					delims = " ";
					while ((lin4 = textr.readLine()) != null) {

						if (!((lin4.trim()).equals(""))) {
							String toke[] = lin4.split(delims);
							System.out.println(toke[0] + "\t" + toke[1] + "\t"
									+ toke[2]);
						}
					}
					textr.close();

				} else if (value2.equalsIgnoreCase("list-routing-tables")) {
					System.out.println("ROUTING TABLES :>");
					for (int i = 0; i < list_RT.size(); i++) {
						// for each node ...calculate the routing table
						// as per size Nr
						RoutingTable r = list_RT.get(i);
						r.printTable();
					}

				} else if (value2.contains("start")) {
					tokens = null;
					tokens = value2.split(delims);

					if (tokens[0].equalsIgnoreCase("start")) {
						if (tokens.length != 2) {
							System.out
									.println("Please provide number-of-messages ex. start 25000");

						} else {
							if(flagSetup==0){
								System.out.println("Cannot initiate without overlay setup!");
							}else{
							System.out.println("INITIATING REQUESTS :>");
							
							//Initiate the request to all the messaging nodes
							for (int i = 0; i < list_RT.size(); i++) {
								// for each node ...calculate the routing table
								// as per size Nr
								RoutingTable r = list_RT.get(i);
								ArrayList<RountingEntry> routingTable = r
										.getRoutingTable();
								RegistryRequestsTaskInitiate t = new RegistryRequestsTaskInitiate();
								t
										.setMESSAGE_TYPE("REGISTRY_REQUESTS_TASK_INITIATE");
								t.setNO_OF_DATA_PACKETS_TO_SEND(Integer.parseInt(tokens[1].trim()));
								t.toString();
								sendInitiate(t, r.getNodeIP(), r.getNodePort());
								
								
							}
							//if(countFinished==list_RT.size()){
								System.out.println("TASK_COMPLETE!");
								System.out.println("INITIATING TRAFFIC SUMMARY REPORTS!");
								//Initiate the request to all the messaging nodes
								for (int i = 0; i < list_RT.size(); i++) {
									// for each node ...calculate the routing table
									// as per size Nr
									RoutingTable r = list_RT.get(i);
									ArrayList<RountingEntry> routingTable = r
											.getRoutingTable();
									//***
									//RegistryRequestsTrafficSummary t = new RegistryRequestsTrafficSummary();
									//t.setMESSAGE_TYPE("REGISTRY_REQUESTS_TRAFFIC_SUMMARY");
									//t.toString();
									//sendSummary(t, r.getNodeIP(), r.getNodePort());
									
									sendString(r.getNodeIP(),r.getNodePort());
								}
								//wait for 20 seconds
								Thread.sleep(20000);
								//print the traffic summary report at the registry
								System.out.println("\tPackets_Sent  Packets_Received  Packets_Relayed  SUM_SENT  SUM_RECVD");
								System.out.println("----------------------------------------------------------------------------------");
								long sum1=0;
								long sum2=0;
								long sum3=0;
								long sum4=0;
								long sum5=0;
								
								for(int i=0;i<list_RT.size();i++){
									TrafficSummary s=summary.get(i);
									System.out.format("Node %d|\t",i);
									System.out.println(s.getSentTracker()+"\t\t"+s.getRecvTracker()+"\t\t"+s.getRelayTracker()+"\t\t"+s.getSendSum()+"\t"+s.getRecvSum());
									System.out.println("----------------------------------------------------------------------------------");
									sum1+=s.getSentTracker();
									sum2+=s.getRecvTracker();
									sum3+=s.getRelayTracker();
									sum4+=s.getSendSum();
									sum5+=s.getRecvSum();
								}
								System.out.print("Sum   |"+sum1+"\t\t"+sum2+"\t\t"+sum3+"\t\t"+sum4+"\t"+sum5);
								
							}
						}
					}
					//}
				} else if (value2.contains("setup-overlay")) {
					tokens = null;
					tokens = value2.split(delims);

					if (tokens[0].equalsIgnoreCase("setup-overlay")) {
						if (tokens.length != 2) {
							System.out
									.println("Please provide number-of-routing-table-entries ex. setup-overlay 3");

						} else {
							System.out.println("INITIATING OVERLAY SETUP :>");
							// Sending Manifest msg to each messaging node which
							// contains routing tbale info for each node
							// list of all nodeids
							// check for Nr size different -****

							int Nr = Integer.parseInt(tokens[1].trim());// default
							//Math.log
							
							// 3
							int[] list = new int[1000];

							// get the current MNnodeList info first
							File file = new File("keys.txt");
							FileReader fr = new FileReader(file.getName());
							BufferedReader textr = new BufferedReader(fr);
							String lin4 = null;
							delims = " ";
							int kk = 0;
							while ((lin4 = textr.readLine()) != null) {

								if (!((lin4.trim()).equals(""))) {
									String toke[] = lin4.split(delims);
									list_MN_client.add(new MNInfo(toke[0]
											.trim(), Integer.parseInt(toke[1]
											.trim()), Integer.parseInt(toke[2]
											.trim())));

									// System.out.println(toke[0]+"\t"+toke[1]+"\t"+toke[2]);
									list[kk] = Integer.parseInt(toke[2].trim());
									kk++;
								}
							}
							textr.close();
							/*
							if(Nr>=(Math.log(list_MN_client.size()))/(Math.log(2)) && Nr<=(Math.log(list_MN_client.size()))/(Math.log(2))+1){
								
							}
							else{
								Nr=(int) ((int)(Math.log(list_MN_client.size()))/(Math.log(2)));
							}
							*/
							System.out.println("ROUTING TABLE SIZE:"+Nr);
							int[] listAllNodeIDs = new int[kk];
							for (int i = 0; i < kk; i++) {
								listAllNodeIDs[i] = list[i];
							}
							// Sort the node identifier list
							Arrays.sort(listAllNodeIDs);

							for (int i = 0; i < list_MN_client.size(); i++) {
								MNInfo val = list_MN_client.get(i);
								val.printMNInfo();

							}
							String listOfNodes="";//For  sending in manifest message
							System.out.print("List of all node identifiers: [");
							for (int i = 0; i < listAllNodeIDs.length; i++) {
								System.out.print(listAllNodeIDs[i] + "\t");
								listOfNodes=listOfNodes+" "+String.valueOf(listAllNodeIDs[i]);
							}
							System.out.println("]");
							// Calculate the routing table for each node

							for (int i = 0; i < listAllNodeIDs.length; i++) {

								// for each node ...calculate the routing table
								// as per size Nr

								RoutingTable r = new RoutingTable();
								r.setNodeID(listAllNodeIDs[i]);
								for (int ll = 0; ll < list_MN_client.size(); ll++) {
									MNInfo val = list_MN_client.get(ll);

									// val.printMNInfo();
									// setip***
									// setport***

									if (val.getNODE_ID() == listAllNodeIDs[i]) {
										r.setNodeIP(val.getIPADDRESS());
										r.setNodePort(val.getPORT_NO());
									}

								}

								ArrayList<RountingEntry> tableEntries = new ArrayList<RountingEntry>();
								tableEntries = rtEntries(listAllNodeIDs, Nr,
										listAllNodeIDs[i]);
								// set entries
								r.setRoutingTable(tableEntries);

								// r.printTable();

								list_RT.add(r);
							}

							// For each messaging node....send the MANIFEST
							// message
							int flag=0;
							int count=0;
							for (int i = 0; i < list_RT.size(); i++) {
								// for each node ...calculate the routing table
								// as per size Nr
								RoutingTable r = list_RT.get(i);
								ArrayList<RountingEntry> routingTable = r
										.getRoutingTable();
								RegistrySendsNodeManifest t = new RegistrySendsNodeManifest();
								t
										.setMESSAGE_TYPE("REGISTRY_SENDS_NODE_MANIFEST");
								t.setROUTING_TABLE_SIZE(String.valueOf(Nr));

								int j = 0;
								RountingEntry rEntry = routingTable.get(j);
								t.setNODEID_1_HOP(rEntry.getNodeid());
								t.setLEN_1_HOP(String.valueOf(rEntry
										.getRNodeIP().length()));
								t.setIPADDRESS_1_HOP(rEntry.getRNodeIP());
								t.setPORTNO_1_HOP(rEntry.getRNodePort());
								j++;
								if(j<Nr){
								RountingEntry rEntry2 = routingTable.get(j);
								t.setNODEID_2_HOP(rEntry2.getNodeid());
								t.setLEN_2_HOP(String.valueOf(rEntry2
										.getRNodeIP().length()));
								t.setIPADDRESS_2_HOP(rEntry2.getRNodeIP());
								t.setPORTNO_2_HOP(rEntry2.getRNodePort());
								j++;
								if(j<Nr){
								RountingEntry rEntry3 = routingTable.get(j);
								t.setNODEID_4_HOP(rEntry3.getNodeid());
								t.setLEN_4_HOP(String.valueOf(rEntry3
										.getRNodeIP().length()));
								t.setIPADDRESS_4_HOP(rEntry3.getRNodeIP());
								t.setPORTNO_4_HOP(rEntry3.getRNodePort());
								}
								}
								t.setNUMBER_OF_IDS(String
										.valueOf(listAllNodeIDs.length));
								
								
								
								t.setLIST_OF_ALL_NODEIDS(listOfNodes);// ****add

								flag=sendManifest(t, r.getNodeIP(), r.getNodePort());
								if(flag==0){
									System.out.println("Node failed for overlay setup: "+r.getNodeIP());
									count++;
								}
							}
							System.out.println("Count Failed:"+count);
							if(count==0){
								System.out.println("Registry now ready to initiate tasks!");
								flagSetup=1;
							}
						}
						
					}

				} else if (value2.equalsIgnoreCase("bye")) {
					break;
				} else {
					System.out.println("Wrong Command!");
				}
				value2 = br.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		// Send this message to other node
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			System.out.println("Exiting RegistryClient!");

		}

	}

	private void sendString(String host, int port) throws IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
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
		String message="MESSAGE TYPE: REGISTRY_REQUESTS_TRAFFIC_SUMMARY";
		byte[] data = new byte[message.getBytes().length];
		data = message.getBytes();
		int datalength = data.length;
		System.out.println("To client:" + message);
		out.writeInt(datalength);
		out.write(data, 0, datalength);
		out.flush();
				
		// Listen on the socket for the reply
		System.out.println("Waiting for OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY from :"+host);
		int datalength2 = 0;
		if ((datalength2 = in.readInt()) != 0) {
			byte[] data2 = new byte[datalength2];
			in.readFully(data2, 0, datalength2);
			OverlayNodeReportsTrafficSummary t3 = new OverlayNodeReportsTrafficSummary(
					data2);
			System.out.println("server :>" + t3.toString());

			TrafficSummary s=new TrafficSummary();
			s.setNODEID(t3.getASSIGNED_ID());
			s.setSendSum(t3.getSUM_OF_PACKETS_SENT());
			s.setRecvSum(t3.getSUM_OF_PACKETS_RECVD());
			s.setSentTracker(t3.getTOTAL_NO_OF_PACKETS_SENT());
			s.setRecvTracker(t3.getTOTAL_PACKETS_RECVD());
			s.setRelayTracker(t3.getTOTAL_NO_OF_PACKETS_RELAYED());
			summary.add(s);
			
		}
		
		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
		
	}



}
