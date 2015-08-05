package cs455.overlay.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import cs455.overlay.wireformats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;

class MessagingNodeClient extends Thread {

	private static int reg_Port = 0;
	private static String reg_host = null;

	public MessagingNodeClient(String regHost, int regPort) {
		// TODO Auto-generated constructor stub
		reg_host = regHost;
		reg_Port = regPort;
	}

	public static void printStatistics() throws IOException,
			NoSuchAlgorithmException {
		// Declare the variables
		Socket clientSocket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		// get current ipaddress and port
		InetAddress IPadd = InetAddress.getLocalHost();
		String currIP = IPadd.getHostAddress();
		int currPort = reg_Port;// assign any port by giving 0
		
		try {
			// create socket at client
			clientSocket = new Socket(currIP, currPort);
			System.out.println("get Port:" + clientSocket.getPort());
			System.out.println("Local Port:" + clientSocket.getLocalPort());
			System.out.println("Registry on host:" + reg_host + " and Port: "
					+ reg_Port);
			// initialize input and output streams over the socket to perform
			// read and write with the server
			out = new DataOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + reg_host);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ reg_host);
			e.printStackTrace();
			
		}

		
		String message="MESSAGE TYPE: PRINT_STATISTICS :";
		byte[] data = new byte[message.getBytes().length];
		data = message.getBytes();
		int datalength = data.length;
		//System.out.println("To client:" + data.toString());
		out.writeInt(datalength);
		out.write(data, 0, datalength);
		out.flush();
		

		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
	}

	public static void unregister() throws IOException,
			NoSuchAlgorithmException {
		// Declare the variables
		Socket clientSocket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			// create socket at client
			clientSocket = new Socket(reg_host, reg_Port);
			System.out.println("get Port:" + clientSocket.getPort());
			System.out.println("Local Port:" + clientSocket.getLocalPort());
			System.out.println("Registry on host:" + reg_host + " and Port: "
					+ reg_Port);
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
		int currPort = reg_Port;// assign any port by giving 0

		// check unregister conditions ***
		// Send the marshall bytes to server
		byte[] reg_request = new byte[1024];
		OverlayNodeSendsDeregistration t = new OverlayNodeSendsDeregistration();

		// **select node ID from list if same ip and port
		int assignedID = 9999;
		File file11 = new File("keys.txt");
		FileReader fr11 = new FileReader(file11.getName());
		BufferedReader textr11 = new BufferedReader(fr11);
		String lin11 = null;
		String delims = " ";
		int ll11 = 0;
		while ((lin11 = textr11.readLine()) != null) {

			if (!((lin11.trim()).equals(""))) {
				String tokens[] = lin11.split(delims);
				if (tokens[0].trim().equalsIgnoreCase(currIP)) {

					if (Integer.parseInt(tokens[1].trim()) == currPort) {
						assignedID = Integer.parseInt(tokens[2].trim());
					}
				}
			}
		}
		textr11.close();
		//
		t.setMESSAGE_TYPE("OVERLAY_NODE_SENDS_DEREGISTRATION");
		t.setLENGTH(String.valueOf(currIP.length()));
		t.setIPADDRESS(currIP);
		t.setPORT_NUMBER(currPort);
		t.setNODE_ID(assignedID);

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

		// Listen on the socket for the reply
		int datalength2 = 0;
		if ((datalength2 = in.readInt()) != 0) {
			byte[] data2 = new byte[datalength2];
			in.readFully(data2, 0, datalength2);
			// String recv = new String(data2);
			// System.out.println("server>" + recv);
			RegistryReportsDeregistrationStatus t3 = new RegistryReportsDeregistrationStatus(
					data2);
			System.out.println("server :>" + t3.toString());

			// Node has been successfully unregistered with the BootStrap server

		}

		// close socket connection when command is executed
		// close input and output streams
		clientSocket.close();
		in.close();
		out.close();
	}

	public void run() {
		String value2 = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Node is in the overlay!");
			System.out.println("You can exit by command: exit-overlay");
			value2 = br.readLine();
			while (value2 != null || value2.equalsIgnoreCase("quit")) {
				System.out.println("Command: " + value2);
				// MessagingNode commands
				if (value2.equalsIgnoreCase("print-counters-and-diagnostics")) {
					System.out.println("SENDING PRINT MESSAGE TO REGISTRY!");
					printStatistics();

				} else if (value2.equalsIgnoreCase("exit-overlay")) {
					System.out.println("SENDING DEREGISTRATION REQUEST!");
					unregister();
					System.out
							.println("Node Exited from the overlay successfully!");
					break;
				} else if (value2.equalsIgnoreCase("bye")) {
					System.out
							.println("To exit first need to unregistered. Initiating exit-overlay request!");
					unregister();
					System.out
							.println("Node Exited from the overlay successfully!");
					break;
				} else {
					System.out.println("Wrong Command!");
				}
				value2 = br.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}
		// Send this message to other node
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			System.out.println("Exiting MessagingNodeClient!");

		}

	}
}
