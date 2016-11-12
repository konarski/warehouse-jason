package warehouse;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import communication.TCPClient;

public class WarehouseEnv extends Environment {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
//	private static final String SERVER_HOST = "192.168.0.2";
	private static final String SERVER_HOST = "25.60.102.17";
	private static final int SERVER_PORT = 8589;
	
	private TCPClient client;
	
	/** Called before the MAS execution with the args informed in .mas2j */
	@Override
	public void init(String[] args) {
		super.init(args);
//		initThread();
//		initCommunication();
//		initAgentCreator();
	}
	
	private void initAgentCreator() {
		final WarehouseEnv env = this;
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	  new AddAgentThread(env);
		      }
		    });
	}

	private void initCommunication() {
		try {
			TCPClient.init(this, SERVER_HOST, SERVER_PORT);
			this.client = TCPClient.getInstance();
			this.client.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}

	public void addNewAgentWorker(String agentName) throws Exception {
		System.out.println("Add new agent " + agentName);
		getEnvironmentInfraTier().getRuntimeServices().createAgent(agentName,
				"src/asl/worker.asl", null, null, null, null);
		getEnvironmentInfraTier().getRuntimeServices().startAgent(agentName);
	}
	
	private void initThread() {
		final WarehouseEnv x = this;
		TCPClient.init(this);
		this.client = TCPClient.getInstance();
		
		class ThreadExample extends Thread {
			@Override
			public void run() {
				int i =0;
				while(true && i < 3) {
					System.out.println("Add percept to master.");
					x.addPercept("m", Literal.parseLiteral("task("+ (i++) +",1,2)"));
					//x.executeAction("m", new Structure(Literal.parseLiteral("test(ALAAA)")));
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		ThreadExample exp = new ThreadExample();
		exp.start();
	}

}
