package scripts.sexyTutorial;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;

import java.util.function.BooleanSupplier;

public class QuestGuide extends Node {

    QuestGuide(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String running = "<col=0000ff>Fancy a run?</col><br>When navigating the world, you can either run " +
            "or walk. Running is faster but you can't run for long as you'll soon run out of energy. You can use the " +
            "flashing orb next to the minimap to toggle running. Why not try it as you head to the next section?";

    private final String movingOn = "<col=0000ff>Moving on</col><br>Follow the path to the next guide. When you get " +
            "there, click on the door to pass through it. Remember, you can use your arrow keys to rotate the camera.";

    private final String quests = "<col=0000ff>Quests</col><br>It's time to learn about quests! Just talk to the " +
            "quest guide to get started.";

    private final String questJournal = "<col=0000ff>Quest journal</col><br>Click on the flashing icon to the left " +
            "of your inventory.";

    private final String questJournal2 = "<col=0000ff>Quest journal</col><br>This is your quest journal. It lists " +
            "every quest in the game. Talk to the quest guide again for an explanation on how it works.";

    private final String movingOn2 = "<col=0000ff>Moving on</col><br>It's time to enter some caves. Click on the " +
            "ladder to go down to the next area.";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void running(){

    }//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FINISH~~~~~~~~~~~~~~~~~~~~~~~~~

    private void movingOn(){
        Utils.walkToQuestGuide(abc);
    }

    private void quests(){
        Utils.walkToQuestGuide(abc);
        Utils.talkTo("Quest Guide");
    }

    private void questJournal(){
        Utils.walkToQuestGuide(abc);
        GameTab.open(GameTab.TABS.QUESTS);
    }

    private void questJournal2(){
        Utils.walkToQuestGuide(abc);
        Utils.talkTo("Quest Guide");
    }

    private void movingOn2(){
        Utils.walkToQuestGuide(abc);
        RSObject[] ladder = Objects.findNearest(10, "Ladder");
        if(ladder.length < 1)
            return;
        ladder[0].click("Climb-down");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Utils.inMiningArea();
            }
        }, General.random(8000, 10000));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific helper functions~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        if(Interfaces.get(263,1,0).getText().equals(running)) {
            System.out.println("Toggling run");
            running();
        }
        else if(Interfaces.get(263,1,0).getText().equals(movingOn)) {
            System.out.println("walking to the quest guide");
            movingOn();
        }
        else if(Interfaces.get(263,1,0).getText().equals(quests)) {
            System.out.println("Talking to the quest guide");
            quests();
        }
        else if(Interfaces.get(263,1,0).getText().equals(questJournal)) {
            System.out.println("Opening the quest journal");
            questJournal();
        }
        else if(Interfaces.get(263,1,0).getText().equals(questJournal2)) {
            System.out.println("Talking to the quest guide again");
            questJournal2();
        }
        else if(Interfaces.get(263,1,0).getText().equals(movingOn2)) {
            System.out.println("Going to the mine");
            movingOn2();
        }
        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 6 || Game.getSetting(406) == 7)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}