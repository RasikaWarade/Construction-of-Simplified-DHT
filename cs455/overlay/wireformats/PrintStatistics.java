package cs455.overlay.wireformats;

public class PrintStatistics {
	static private int sendTracker=0;
	static private int recvTracker=0;
	static private int relayTracker=0;
	
	static private long sendSummation=0;
	static private long recvSummation=0;
	public static int getSendTracker() {
		return sendTracker;
	}
	public static void setSendTracker(int sendTracker) {
		PrintStatistics.sendTracker = sendTracker;
	}
	public static int getRecvTracker() {
		return recvTracker;
	}
	public static void setRecvTracker(int recvTracker) {
		PrintStatistics.recvTracker = recvTracker;
	}
	public static int getRelayTracker() {
		return relayTracker;
	}
	public static void setRelayTracker(int relayTracker) {
		PrintStatistics.relayTracker = relayTracker;
	}
	public static long getSendSummation() {
		return sendSummation;
	}
	public static void setSendSummation(long sendSummation) {
		PrintStatistics.sendSummation = sendSummation;
	}
	public static long getRecvSummation() {
		return recvSummation;
	}
	public static void setRecvSummation(long recvSummation) {
		PrintStatistics.recvSummation = recvSummation;
	}
	
}
