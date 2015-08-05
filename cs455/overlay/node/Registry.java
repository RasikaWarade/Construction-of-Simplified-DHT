package cs455.overlay.node;

//USE HASTABLE to store the IPAddress and Corresponding PortNUmbers
//For REG and DEREG
//plus additional ID
//Routing Table for each node

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import cs455.overlay.wireformats.OverlayNodeReportsTaskFinished;
import cs455.overlay.wireformats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;

class MNInfo {
	private String IPADDRESS;
	private int PORT_NO;
	private int NODE_ID;

	public MNInfo(String ipadd, int portno, int node_id) {
		IPADDRESS = ipadd;
		PORT_NO = portno;
		NODE_ID = node_id;
	}

	public int getNODE_ID() {
		return NODE_ID;
	}

	public String getIPADDRESS() {
		return IPADDRESS;
	}

	public void setIPADDRESS(String iPADDRESS) {
		IPADDRESS = iPADDRESS;
	}

	public int getPORT_NO() {
		return PORT_NO;
	}

	public void setPORT_NO(int pORTNO) {
		PORT_NO = pORTNO;
	}

	public void setNODE_ID(int nODEID) {
		NODE_ID = nODEID;
	}

	public void printMNInfo() {
		System.out.println(IPADDRESS + "\t" + PORT_NO + "\t" + NODE_ID);
	}
}

public class Registry {

	// Declare the variables
	ServerSocket serv = null;
	Socket connection = null;
	String clientIP = null;

	static int currPort = 0;
	static private DataOutputStream out = null;
	static private DataInputStream in = null;

	static ArrayList<MNInfo> list_MN = new ArrayList<MNInfo>();

	static int randomNode(int random) {
		if (list_MN.size() == 0) {
			return random;

		} else {
			int r = -1;
			for (int i = 0; i < list_MN.size(); i++) {
				MNInfo val = list_MN.get(i);
				if (random == val.getNODE_ID()) {
					r = -1;
					break;
				} else{
					r = random;
				}
			}
			return r;
		}

	}

	void run() {
		try {

			serv = new ServerSocket(currPort, 3000000); // create new server
			// socket
			System.out.println("ServerSocket Created");
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
					
					
				}else{
				// checking unmarshalling

				OverlayNodeSendsRegistration t2 = new OverlayNodeSendsRegistration(
						data2);
				// check if registration or dereg request
				if (t2.getMESSAGE_TYPE().equalsIgnoreCase(
						"OVERLAY_NODE_SENDS_REGISTRATION")) {
					System.out.println("MESSAGE :>" + t2.toString());

					// Assign node identifier random between 0 and 127

					int random = -1;
					while (random == -1) {
						Random rand = new Random();
						int x = rand.nextInt(127);
						// Check if the nodeID is not duplicate***
						random = randomNode(x);
					}

					System.out.println("NODE_ID:>" + random);
					list_MN.add(new MNInfo(t2.getIPADDRESS(), t2
							.getPORT_NUMBER(), random));

					RegistryReportsRegistrationStatus t = new RegistryReportsRegistrationStatus();

					t.setMESSAGE_TYPE("REGISTRY_REPORTS_REGISTRATION_STATUS");

					// Assign -1 if failed
					t.setSUCCESS_STATUS(random);
					String info_string = "Registration request successful. No. of messaging nodes="
							+ list_MN.size();
					// if success
					t.setLENGTH_INFO(String.valueOf(info_string.length()));
					// if failed

					t.setINFORMATION_STRING(info_string);

					System.out.println("MESSAGE>");
					System.out.println(t.toString());
					// Successful Registration done
					/*
					 * // Send Registered String clientMsg = "Registered!";
					 * byte[] data = new byte[clientMsg.getBytes().length]; data
					 * = clientMsg.getBytes(); int datalength = data.length;
					 * 
					 * System.out.println("server>:" + data.toString());
					 * out.writeInt(datalength); // Give the command to register
					 * the node with BootStrap Server out.write(data, 0,
					 * datalength); out.flush();
					 */

					// checking marshalling
					byte[] sent = t.getBytes();
					int sentlength = sent.length;
					out.writeInt(sentlength);
					// Give the command to register the node with BootStrap
					// Server
					out.write(sent, 0, sentlength);
					out.flush();
					// Node has been successfully registered with the BootStrap
					// server

				} else if (t2.getMESSAGE_TYPE().equalsIgnoreCase(
						"OVERLAY_NODE_SENDS_DEREGISTRATION")) {
					OverlayNodeSendsDeregistration t3 = new OverlayNodeSendsDeregistration(
							data2);
					System.out.println("MESSAGE :>" + t3.toString());
					// Check if already registered......

					/*
					// Send Unregistered
					String clientMsg = "Unregistered!";
					byte[] data = new byte[clientMsg.getBytes().length];
					data = clientMsg.getBytes();
					int datalength = data.length;

					
					System.out.println("server>:" + data.toString());
					out.writeInt(datalength);
					// Give the command to register the node with BootStrap
					// Server
					out.write(data, 0, datalength);
					out.flush();
					*/
					// Successful unRegistration
					
					
					RegistryReportsDeregistrationStatus t = new RegistryReportsDeregistrationStatus();

					t.setMESSAGE_TYPE("REGISTRY_REPORTS_DEREGISTRATION_STATUS");

					
					//Remove from the messaging node list***
					int assignedID=t3.getNODE_ID();
					for (int i = 0; i < list_MN.size(); i++) {
						MNInfo value = list_MN.get(i);
						if(value.getNODE_ID()==assignedID){
							list_MN.remove(value);
							System.out.println("assignedID "+assignedID+" removed from list!");
						}
					}
					// Assign -1 if failed
					t.setSUCCESS_STATUS(assignedID);//random.....****to be searched in the list and removed
					String info_string = "Deregistration request successful. No. of messaging nodes="
							+ list_MN.size();
					// if success
					t.setLENGTH_INFO(String.valueOf(info_string.length()));
					// if failed

					t.setINFORMATION_STRING(info_string);

					System.out.println("MESSAGE>");
					System.out.println(t.toString());
					// Successful Registration done

					// checking marshalling
					byte[] sent = t.getBytes();
					int sentlength = sent.length;
					out.writeInt(sentlength);
					// Give the command to register the node with BootStrap
					// Server
					out.write(sent, 0, sentlength);
					out.flush();
				}else if(t2.getMESSAGE_TYPE().equalsIgnoreCase(
				"OVERLAY_NODE_REPORTS_TASK_FINISHED")){
					
					OverlayNodeReportsTaskFinished t3 = new OverlayNodeReportsTaskFinished(
							data2);
					System.out.println("MESSAGE :>" + t3.toString());
					
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

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out
					.println("Please enter the port number for the registry...");
		} else {
			currPort = Integer.parseInt(args[0]);
			Registry server = new Registry();
			RegistryClient client = new RegistryClient(args[0]);
			client.start();
			while (true) {
				server.run();
				File file = new File("keys.txt");
				file.delete();

				File file2 = new File("keys.txt");
				FileWriter fileWritter2;
				try {
					fileWritter2 = new FileWriter(file2.getName(), true);
					BufferedWriter bufferWritter2 = new BufferedWriter(
							fileWritter2);

					// Loop through elements.
					// Save the MessagingNodeInfo
					for (int i = 0; i < list_MN.size(); i++) {
						MNInfo value = list_MN.get(i);
						value.printMNInfo();
						bufferWritter2
								.write(value.getIPADDRESS() + " "
										+ value.getPORT_NO() + " "
										+ value.getNODE_ID());
						bufferWritter2.newLine();
					}
					bufferWritter2.close();
					// Saved in file
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}