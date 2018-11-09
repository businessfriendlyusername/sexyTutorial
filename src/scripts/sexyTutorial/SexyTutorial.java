package scripts.sexyTutorial;

import obf.Wi;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Interfaces;
import org.tribot.script.Script;

import java.util.ArrayList;
import java.util.Collections;

public class SexyTutorial extends Script {

    public ABCUtil abc = new ABCUtil();

    public static ArrayList<Node> nodes = new ArrayList<>();

    @Override
    public void run(){
        Collections.addAll(nodes, new GielinorGuide(abc), new SurvivalInstructor(abc), new Cook(abc), new QuestGuide(abc),
                new MiningInstructor(abc), new CombatInstructor(abc), new Banker(abc), new Priest(abc), new Wizard(abc));
        while(Interfaces.get(263, 1, 0) != null){//while we are on tutorial island
            for(final Node node : nodes){
                if(node.validate())
                    node.execute();
            }
        }
    }
}
