package scripts.sexyTutorial;

import org.tribot.api.General;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;

public class Wizard extends Node {

    Wizard(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String intro = "<col=0000ff>Your final instructor!</col><br>Follow the path to the wizard's house, " +
            "where you will be shown how to cast spells. When you get there, just talk with the magic instructor.";

    private final String openMagic = "<col=0000ff>Open up your final menu</col><br>Open up the magic interface by " +
            "clicking on the flashing icon.";

    private final String magic = "<col=0000ff>Magic</col><br>This is your magic interface. All of your spells " +
            "are listed here. Talk to the instructor to learn more.";

    private final String magicCast = "<col=0000ff>Magic casting</col><br>You now have some runes. All spells require " +
            "runes to cast them. Look for the Wind Strike spell in your magic interface. Click on this spell to " +
            "select it and then click on a chicken to cast it. Talk to the instructor if you need more runes.";

    private final String toMainland = "<col=0000ff>To the mainland!</col><br>You're nearly finished with the " +
            "tutorial. All you need to do now is move on to the mainland. Just speak with the magic instructor " +
            "and he'll teleport you to Lumbridge.";
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void intro() {
        Utils.walkToWizard(abc);
        Utils.talkTo("Magic Instructor");
    }

    private void openMagic(){
        GameTab.open(GameTab.TABS.MAGIC);
    }

    private void magic(){
        Utils.walkToWizard(abc);
        Utils.talkTo("Magic Instructor");
    }

    private void magicCast(){
        Utils.walkToWizard(abc);
        RSNPC[] chicken = NPCs.findNearest("Chicken");
        if(chicken.length < 1)
            return;
        Magic.selectSpell("Wind Strike");
        chicken[0].click("Cast Wind Strike -> Chicken");
    }

    private void toMainland(){//TODO add null checks and timing waits
        Utils.walkToWizard(abc);
        Utils.talkTo("Magic Instructor");
        if(NPCChat.getOptions() == null)
            return;
        NPCChat.selectOption("Yes.", true);
        General.sleep(1000, 2000);
        NPCChat.getClickContinueInterface().click("Continue");
        General.sleep(1000, 2000);
        NPCChat.selectOption("No. I'm not planning to do that.", true);
        General.sleep(1000, 2000);
        NPCChat.getClickContinueInterface().click("Continue");
        General.sleep(1000, 2000);
        NPCChat.getClickContinueInterface().click("Continue");
        General.sleep(1000, 2000);
        NPCChat.getClickContinueInterface().click("Continue");
        General.sleep(1000, 2000);
        NPCChat.getClickContinueInterface().click("Continue");
        General.sleep(2000, 5000);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        if(Interfaces.get(263,1,0).getText().equals(intro)) {
            System.out.println("Getting magic intro");
            intro();
        }
        else if(Interfaces.get(263,1,0).getText().equals(openMagic)) {
            System.out.println("Opening magic tab");
            openMagic();
        }
        else if(Interfaces.get(263,1,0).getText().equals(magic)) {
            System.out.println("Getting the rundown on magic from this yung wiz!");
            magic();
        }
        else if(Interfaces.get(263,1,0).getText().equals(magicCast)) {
            System.out.println("Casting wind strike!");
            magicCast();
        }
        else if(Interfaces.get(263,1,0).getText().equals(toMainland)) {
            System.out.println("Travelling to mainland!");
            toMainland();
        }

        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) >= 18)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}