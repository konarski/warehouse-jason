package communication.model.request;

import java.nio.ByteBuffer;

import communication.model.Request;

public class BIDRequest implements Request {
	//bid	1	id_agenta (8bit)	size (32bit)	list of nodes (size*8bit)	
	private byte packetId = 2;
	private int packetSize = 7;
	private byte agentId;
	private int taskId;
	private byte exitId;
	private byte resourceType;
	
	public BIDRequest(byte agentId, int taskId, byte resourceType, byte nodeId) {
		this.agentId = agentId;
		this.resourceType = resourceType;
		this.exitId = nodeId;
		this.taskId = taskId;
	}
	
	public byte[] toByteArray() {
		byte[] result = ByteBuffer.allocate(12).put(packetId).putInt(packetSize).put(agentId).putInt(taskId).put(exitId).put(resourceType).array();
		return result;
	}
	
	
	
	
}
