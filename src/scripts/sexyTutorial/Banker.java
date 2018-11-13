package scripts.sexyTutorial;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.API.AntiBan;
import scripts.API.Node;


import java.util.function.BooleanSupplier;

public class Banker extends Node {

    private final String movingOn = "<col=0000ff>Banking</col><br>Follow the path and you will come to the front of " +
            "the building. This is the Bank of Gielinor, where you can store all your most valued items. To open " +
            "your bank, just click on the indicated booth.";

    private final String banking = "<col=0000ff>Banking</col><br>This is your bank. You can store things here for " +
            "safekeeping. To deposit something from your inventory, just click on it. You can withdraw things in " +
            "the same way. To continue, close the bank and click on the indicated poll booth.";

    private final String walkToAccountMan = "<col=0000ff>Moving on</col><br>Polls are run periodically to let the " +
            "Old School RuneScape community vote on how the game should - or shouldn't - change. When you're ready, " +
            "move on through the door indicated.";

    private final String talkToAccountMan = "<col=0000ff>Account Management</col><br>The guide here will tell you " +
            "all about your account. Just click on him to hear what he's got to say.";

    private final String openAccountTab = "<col=0000ff>Account Management</col><br>Click on the flashing icon " +
            "to open your Account Management menu.";

    private final String explainAccount = "<col=0000ff>Account Management</col><br>This is your Account Management " +
            "menu where you can control various aspects of your account. Talk to the Account Guide to learn more.";

    private final String leaving = "<col=0000ff>Moving on</col><br>Continue through the next door.";
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void movingOn(){
        Utils.walkToBank();
        if(AntiBan.getABCUtil().generateOpenBankPreference() == OpenBankPreference.BANKER){//open bank with the banker
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
        General.sleep(500, 1000);
        //TODO null pointer exception thrown below
        Interfaces.get(345,2,11).click("Close");//Interfaces.closeall() doesn't work here for some reason
    }

    private void walkToAccountMan(){
        Utils.walkToAccountGuide();
    }

    private void talkToAccountMan(){
        Utils.walkToAccountGuide();
        Utils.talkTo("Account Guide");
    }

    private void openAccountTab(){
        GameTab.open(GameTab.TABS.ACCOUNT);
    }

    private void explainAccount(){
        Utils.walkToAccountGuide();
        Utils.talkTo("Account Guide");
    }

    private void leaving(){
        Utils.walkToPriest();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        System.out.println("Banker");
        if(Interfaces.get(263,1,0).getText().equals(movingOn)) {
            System.out.println("Opening da bank");
            movingOn();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(banking)) {
            System.out.println("Closing bank and doing poll stuff");
            banking();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(walkToAccountMan)) {
            System.out.println("Walking to the account guide");
            walkToAccountMan();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(talkToAccountMan)) {
            System.out.println("Talking to the account guide");
            talkToAccountMan();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(openAccountTab)) {
            System.out.println("Opening account tab");
            openAccountTab();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(explainAccount)) {
            System.out.println("Listening to this dumb fuck tell me about mtxscape");
            explainAccount();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(leaving)) {
            System.out.println("Going to the rapist priest");
            leaving();
            System.out.println("Finished");
        }

        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){//TODO~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if(Game.getSetting(406) == 14 || Game.getSetting(406) == 15)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}