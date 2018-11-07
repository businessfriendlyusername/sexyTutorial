package scripts.sexyTutorial;

import org.jetbrains.annotations.NotNull;
import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;

public abstract class Node {

    Node(){}

    Node(ABCUtil a){
        abc = a;
    }

    protected ABCUtil abc;

    public abstract void execute();

    public abstract boolean validate();

}
