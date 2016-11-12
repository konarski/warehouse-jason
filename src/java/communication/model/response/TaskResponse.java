package communication.model.response;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jason.asSyntax.Literal;
import communication.model.Response;

public class TaskResponse implements Response {
	
	private static int counter = 0;
	
	private byte packetId = 6;
	private byte exitId;
	private byte[] resourceId;

	public TaskResponse(byte exitId, byte[] resourceId) {
		this.exitId = exitId;
		this.resourceId = resourceId;
	}

	public Literal createPercept() {
		return Literal.parseLiteral("task(" + (counter++) + ", " + exitId + ", " + resourceId[0] + ")");
	}

	public List<Literal> createPercepts() {
		ArrayList<Literal> prcepts = new ArrayList<Literal>();
		for(int i =0; i < resourceId.length; i++) {
			prcepts.add(Literal.parseLiteral("task(" + (counter++) + ", " + exitId + ", " + resourceId[i] + ")"));
		}
		return prcepts;
	}
	
	public static Response readResponse(DataInputStream input) throws IOException {
		int packetSize = input.readInt();
		int resourceSize = packetSize - 1;
		byte exitId = input.readByte();
		byte[] resourceId = new byte[resourceSize];
		for(int i=0; i < resourceSize; i++) {
			resourceId[i] = input.readByte();
		}
		return new TaskResponse(exitId, resourceId);
	}

	@Override
	public String toString() {
		return "TaskResponse [packetId=" + packetId + ", exitId=" + exitId
				+ ", resourceId=" + Arrays.toString(resourceId) + "]";
	}
	
	

}
