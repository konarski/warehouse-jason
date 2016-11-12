package communication.model.request;

import java.nio.ByteBuffer;

import communication.model.Request;

public class NewAgentRequest implements Request {
	
	private byte packetId = 4;
	
	public byte[] toByteArray() {
		byte[] result = ByteBuffer.allocate(5).put(packetId).putInt(0).array();
		return result;
	}

}
