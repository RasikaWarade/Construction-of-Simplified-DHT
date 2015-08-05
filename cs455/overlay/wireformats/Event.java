package cs455.overlay.wireformats;

public interface Event {
	public byte[] getBytes();
	public int getType();
	public void writeBytes(byte[] marshall);
}
