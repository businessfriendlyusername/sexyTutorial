package scripts.sexyTutorial;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;


import java.util.function.BooleanSupplier;

public class Banker extends Node {

    Banker(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String movingOn = "<col=0000ff>Banking</col><br>Follow the path and you will come to the front of " +
            "the building. This is the Bank of Gielinor, where you can store all your most valued items. To open " +
            "your bank, just click on the indicated booth.";

    private final String Banking = "<col=0000ff>Banking</col><br>This is your bank. You can store things here for " +
            "safekeeping. To deposit something from your inventory, just click on it. You can withdraw things in " +
            "the same way. To continue, close the bank and click on the indicated poll booth.";

    private final String walkToAccountMan = "<col=0000ff>Moving on</col><br>Polls are run periodically to let the " +
            "Old School RuneScape community vote on how the game should - or shouldn't - change. When you're ready, " +
            "move on through the door indicated.";
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void movingOn(){
        Utils.walkToBank(abc);
        if(abc.generateOpenBankPreference() == OpenBankPreference.BANKER){//open bank with the banker
            RSNPC[] banker = NPCs.findNearest("Banker");
            if(banker.length < 1)
                return;
            banker[0].click("Talk-to");
        }
        else{//open bank with the booth
            RSObject[] booth = Objects.findNearest(10, "Bank booth");
            if(booth.length < 1)
                return;
            booth[0].click("Use");
        }
        General.sleep(1200, 2000);
    }

    private void banking(){
        Interfaces.closeAll();
        Utils.flush();
        RSObject[] poll = Objects.findNearest(10, "Poll booth");
        if(poll.length < 1)
            return;
        Camera.turnToTile(poll[0]);
        poll[0].click("Use");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return NPCChat.getClickContinueInterface() != null;
            }
        }, General.random(8000, 15000));

        while (NPCChat.getClickContinueInterface() != null) {
            NPCChat.getClickContinueInterface().click("Continue");
            General.sleep(330, 1500);
        }
        General.sleep(1000);
        Interfaces.get(310,2,11).click("Close");//Interfaces.closeall() doesn't work here for some reason
    }

    private void walkToAccountMan(){

    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific helper functions~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
//        if(Interfaces.get(263,1,0).getText().equals(intro))
//            intro();

        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 10 || Game.getSetting(406) == 11)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}