// Internal action code for project warehouse

package internalactions;

import java.util.Iterator;

import communication.TCPClient;

import jason.asSemantics.*;
import jason.asSyntax.*;

@Deprecated
public class deleteBelief extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        ts.getAg().getLogger().info("executing internal action 'internalactions.deleteBelief'");
        
        Literal belief = Literal.parseLiteral(args[0].toString());
        System.out.println("%%%%%%%%%");
        Iterator<Literal> i = ts.getAg().getBB().getPercepts();
        while(i.hasNext()) {
        	Literal tmp = i.next();
        	System.out.println(tmp);
        }
        System.out.println("%%%%%%%%%");
        System.out.println(belief);
        System.out.println("%%%%%%%%%");
       // ts.getAg().delBel(belief);
        ts.getAg().abolish(belief, new Unifier());
       // TCPClient.getInstance().env.removePerceptsByUnif("a1", belief);
        i = ts.getAg().getBB().getPercepts();
        while(i.hasNext()) {
        	Literal tmp = i.next();
        	System.out.println(tmp);
        }
        System.out.println("%%%%%%%%%");
        
        return true;
    }
}
