package scripts.sexyTutorial;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.script.Script;

public class SexyTutorial extends Script {

    public ABCUtil abc = new ABCUtil();

    @Override
    public void run(){

        Node n = new SurvivalInstructor(abc);
        if(n.validate())
            n.execute();
    }
}
