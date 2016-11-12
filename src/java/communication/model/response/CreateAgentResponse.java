package communication.model.response;

import java.io.DataInputStream;
import java.io.IOException;

import jason.asSyntax.Literal;
import communication.model.Response;

public class CreateAgentResponse implements Response {
	
	private byte packetId = 5;
	private int packetSize = 1;
	private byte agentId;

	public CreateAgentResponse(byte agentId) {
		super();
		this.agentId = agentId;
	}
	
	public byte getAgentId() {
		return agentId;
	}

	public Literal createPercept() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static CreateAgentResponse readResponse(DataInputStream input) throws IOException {
		System.out.println("READ AT");
		int packetSize = input.readInt();
		byte agentId = input.readByte();
		return new CreateAgentResponse(agentId);
	}

}
