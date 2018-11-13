package scripts.sexyTutorial;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Interfaces;
import org.tribot.script.Script;
import scripts.API.Node;

import java.util.ArrayList;
import java.util.Collections;

public class SexyTutorial extends Script {

    public static ArrayList<Node> nodes = new ArrayList<>();

    @Override
    public void run(){
        Collections.addAll(nodes, new GielinorGuide(), new SurvivalInstructor(), new Cook(), new QuestGuide(),
                new MiningInstructor(), new CombatInstructor(), new Banker(), new Priest(), new Wizard());
        while(Interfaces.get(263, 1, 0) != null){//while we are on tutorial island
            for(final Node node : nodes){
                if(node.validate())
                    node.execute();
            }
        }
    }
}
