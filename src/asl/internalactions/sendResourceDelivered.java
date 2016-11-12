package internalactions;

import communication.TCPClient;
import communication.model.request.ResourceDeliveredRequest;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class sendResourceDelivered extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	NumberTerm exit = (NumberTerm) args[0];
        NumberTerm resourceType = (NumberTerm) args[1];
        
        byte exitId = (byte) Math.round(exit.solve());
        byte resourceTypeId = (byte) Math.round(resourceType.solve());
        
        ResourceDeliveredRequest request = new ResourceDeliveredRequest(exitId, resourceTypeId);
        TCPClient.getInstance().sendMessage(request);
        return true;
    }
}
