package internalactions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import communication.TCPClient;
import communication.model.request.GOTORequest;

public class sendMessageGoto extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        
    	Term agentId = args[0];
        NumberTerm node = (NumberTerm) args[1];
        
        byte agentNumberId = Byte.parseByte(agentId.toString().substring(1));
        byte nodeId = (byte) Math.round(node.solve());
        
        GOTORequest request = new GOTORequest(agentNumberId, nodeId);
        TCPClient.getInstance().sendMessage(request);
        return true;
    }
}
