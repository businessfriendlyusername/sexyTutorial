package scripts.sexyTutorial;

import org.tribot.api.util.abc.ABCUtil;

public abstract class Node {

    Node(){}

    Node(ABCUtil a){
        abc = a;
    }

    protected ABCUtil abc;

    public abstract void execute();

    public abstract boolean validate();

}
