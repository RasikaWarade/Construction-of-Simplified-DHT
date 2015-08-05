package cs455.overlay.wireformats;

public interface protocol {
	public final int OVERLAY_NODE_SENDS_REGISTRATION=2;
	public final int REGISTRY_REPORTS_REGISTRATION_STATUS=3;
	public final int OVERLAY_NODE_SENDS_DEREGISTRATION=4;
	public final int REGISTRY_REPORTS_DEREGISTRATION_STATUS=5;
	public final int REGISTRY_SENDS_NODE_MANIFEST=6;
	public final int NODE_REPORTS_OVERLAY_SETUP_STATUS=7;
	public final int REGISTRY_REQUESTS_TASK_INITIATE=8;
	public final int OVERLAY_NODE_SENDS_DATA=9;
	public final int OVERLAY_NODE_REPORTS_TASK_FINISHED=10;
	public final int REGISTRY_REQUESTS_TRAFFIC_SUMMARY=11;
	public final int OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY=12;
}
