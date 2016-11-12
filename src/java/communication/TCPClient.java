package communication;

import jason.asSyntax.Literal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import warehouse.WarehouseEnv;

import communication.model.Request;
import communication.model.response.ATResponse;
import communication.model.response.BIDResponse;
import communication.model.response.CreateAgentResponse;
import communication.model.response.TaskResponse;

public class TCPClient extends Thread {

	private static TCPClient client;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private WarehouseEnv env;

	public static void init(WarehouseEnv warehouseEnv, String host, int port)
			throws UnknownHostException, IOException {
		TCPClient.client = new TCPClient(warehouseEnv, host, port);
	}

	public static void init(WarehouseEnv env) {
		TCPClient.client = new TCPClient(env);
	}

	public static TCPClient getInstance() {
		return client;
	}

	private TCPClient(WarehouseEnv env) {
		this.env = env;
	}

	private TCPClient(WarehouseEnv warehouseEnv, String host, int port)
			throws UnknownHostException, IOException {
		this.env = warehouseEnv;
		socket = new Socket(host, port);
		output = new DataOutputStream(socket.getOutputStream());
		input = new DataInputStream(socket.getInputStream());
	}

	public static final byte RESPONSE_AT = 0;
	public static final byte RESPONSE_BID = 3;
	public static final byte RESPONSE_NEWAG = 5;
	public static final byte RESPONSE_TASK = 6;

	public void run() {
		boolean running = true;
		try {
			while (running) {
				byte packetId = input.readByte();
				System.out.println("PacketId= " + packetId);
				handleMessage(packetId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(Request request) throws IOException {
		if (socket != null && !socket.isClosed()) {
			output.write(request.toByteArray());
		}
	}

	public void handleMessage(byte packetId) throws IOException {
		if (packetId == RESPONSE_AT) {
			ATResponse res = (ATResponse) ATResponse.readResponse(input);
			String agentName = "a" + res.getAgentId();
			Literal literal = res.createPercept();
			env.removePerceptsByUnif(agentName,
					Literal.parseLiteral("at(_)[source(_)]"));
			env.addPercept(agentName, literal);
		} else if (packetId == RESPONSE_NEWAG) {
			try {
				CreateAgentResponse res = CreateAgentResponse
						.readResponse(input);
				env.addNewAgentWorker("a" + res.getAgentId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (packetId == RESPONSE_BID) {
			BIDResponse res = (BIDResponse) BIDResponse.readResponse(input);
			env.addPercept("a" + res.getAgentId(), res.createPercept());
		} else if (packetId == RESPONSE_TASK) {
			TaskResponse res = (TaskResponse) TaskResponse.readResponse(input);
			List<Literal> percepts = res.createPercepts();
			for (Literal percept : percepts) {
				env.addPercept("m", percept);
			}

		}

	}
}
