package communication.model.response;

import java.io.DataInputStream;
import java.io.IOException;

import jason.asSyntax.Literal;
import communication.model.Response;

public class ATResponse implements Response {
	
	//at 3 id_agenta (8bit)	id_node (8bit)
	private byte packetId = 0;
	private byte agentId;
	private byte nodeId;
	
	
	public ATResponse(byte agentId, byte nodeId) {
		this.agentId = agentId;
		this.nodeId = nodeId;
	}

	public byte getAgentId() {
		return agentId;
	}

	public Literal createPercept() {
		return Literal.parseLiteral("at("+ nodeId +")");
	}
	
	public static Response readResponse(DataInputStream input) throws IOException {
		int packetSize = input.readInt();
		byte agentId = input.readByte();
		byte nodeId = input.readByte();
		return new ATResponse(agentId, nodeId);
	}
}
