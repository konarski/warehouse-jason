package communication.model.response;

import java.io.DataInputStream;
import java.io.IOException;

import jason.asSyntax.Literal;
import communication.model.Response;

public class BIDResponse implements Response {
	
	private byte packetId = 3;
	private byte agentId;
	private int distance;
	private int time;
	private byte resourceId;
	private int taskId;
	private byte exitId;
	
	public BIDResponse(byte agentId, int taskId, int distance, int time, byte resourceId, byte exitId) {
		this.agentId = agentId;
		this.distance = distance;
		this.time = time;
		this.resourceId = resourceId;
		this.taskId = taskId;
		this.exitId = exitId;
	}
	
	public byte getAgentId() {
		return agentId;
	}
	
	public Literal createPercept() {
		return Literal.parseLiteral("bid(" + taskId + "," + distance + "," + time + "," + exitId + "," + resourceId + ")");
	}

	public static Response readResponse(DataInputStream input) throws IOException {
		int packetSize = input.readInt();
		byte agentId = input.readByte();
		int taskId = input.readInt();
		byte exitId = input.readByte();
		byte nodeId = input.readByte();
		int distance = input.readInt();
		int time = input.readInt();
		return new BIDResponse(agentId, taskId, distance, time, nodeId, exitId);
	}
	
}
