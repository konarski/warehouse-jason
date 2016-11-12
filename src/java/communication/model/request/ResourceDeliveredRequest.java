package communication.model.request;

import java.nio.ByteBuffer;

import communication.model.Request;

public class ResourceDeliveredRequest implements Request{

	private byte packetId = 9;
	private int packetSize = 2;
	private byte exitId;
	private byte resourceType;
	
	public ResourceDeliveredRequest(byte exitId, byte resourceType) {
		this.exitId = exitId;
		this.resourceType = resourceType;
	}
	
	public byte[] toByteArray() {
		byte[] result = ByteBuffer.allocate(7).put(packetId).putInt(packetSize).put(exitId).put(resourceType).array();
		return result;
	}

}
