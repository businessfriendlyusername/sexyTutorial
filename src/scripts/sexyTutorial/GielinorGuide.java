package scripts.sexyTutorial;

import org.jetbrains.annotations.NotNull;
import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.api.util.abc.ABCUtil;
import scripts.sexyTutorial.Node;

public class GielinorGuide extends Node{


    @Override
    public void execute(){
        //do some antiban~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Utils.talkTo("Gielinor Guide");
        Clicking.click(Interfaces.get(219,1,2));
        General.sleep(300,550);
        Clicking.click(NPCChat.getClickContinueInterface());
//        General.sleep(300,550); ~~~~~~~~~~~~~~~~~~~~WE MIGHT NEED TO UNCOMMENT THIS~~~~~~~~~~~~~~~~~~~~~~
//        Clicking.click(NPCChat.getClickContinueInterface());
        General.sleep(500, 750);
        GameTab.open(GameTab.TABS.OPTIONS);
        General.sleep(300,600);
        Utils.talkTo("Gielinor Guide");
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 0)
            return true;
        else
            return false;
    }
}
