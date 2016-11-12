package communication.model.request;

import java.nio.ByteBuffer;

import communication.model.Request;

public class GOTORequest implements Request{
	
	//goto	0	id_agenta (8bit)	id_node (8bit)
	private byte packetId = 1;
	private int packetSize = 2;
	private byte agentId;
	private byte nodeId;
	
	public GOTORequest(byte agentId, byte nodeId) {
		this.agentId = agentId;
		this.nodeId = nodeId;
	}

	public byte[] toByteArray() {
		byte[] result = ByteBuffer.allocate(7).put(packetId).putInt(packetSize).put(agentId).put(nodeId).array();
		return result;
	}
	
}
