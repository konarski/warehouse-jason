package internalactions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import communication.TCPClient;
import communication.model.request.BIDRequest;

public class sendMessageBid extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		
		Term agentId = args[0];
		NumberTerm task = (NumberTerm) args[1];
		NumberTerm destination = (NumberTerm) args[2];
		NumberTerm resourceType = (NumberTerm) args[3];
		
		byte aId = Byte.parseByte(agentId.toString().substring(1));
		
		int taskId = (int) Math.round(task.solve());
		byte destinationId = (byte) Math.round(destination.solve());
		byte resourceTypeId = (byte) Math.round(resourceType.solve());
		
		BIDRequest request = new BIDRequest(aId, taskId, resourceTypeId, destinationId);
		TCPClient.getInstance().sendMessage(request);
		return true;
	}
}
