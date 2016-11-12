package warehouse;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import communication.TCPClient;
import communication.model.request.NewAgentRequest;

public class AddAgentThread extends JFrame {

	private WarehouseEnv env;
	private static byte agentCounter = 1;

	public AddAgentThread(WarehouseEnv env) {
		this.env = env;
		setLayout(new FlowLayout());
		Container cp = getContentPane();
		JButton buttonAdd = new JButton("Add Agent");

		buttonAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				NewAgentRequest request = new NewAgentRequest();
				try {
					TCPClient.getInstance().sendMessage(request);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		cp.add(buttonAdd);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
