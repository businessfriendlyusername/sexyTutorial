package scripts.sexyTutorial;

import org.tribot.api.General;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;

public class Priest extends Node {

    Priest(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String intro = "<col=0000ff>Prayer</col><br>Follow the path to the chapel and enter it." +
            "<br>Once inside talk to the monk. He'll tell you all about the Prayer skill.";

    private final String prayerMenu = "<col=0000ff>Prayer menu</col><br>Click on the flashing icon " +
            "to open the Prayer menu.";

    private final String prayerExplain = "<col=0000ff>Prayer menu</col><br>Talk with Brother Brace " +
            "and he'll tell you about prayers.";

    private final String friends = "<col=0000ff>Friends and Ignore lists</col><br>You should now see another new " +
            "icon. Click on the flashing face icon to open your friends and ignore lists.";

    private final String friendsExplain = "<col=0000ff>Friends and Ignore lists</col><br>These two lists can be " +
            "very helpful for keeping track of your friends or for blocking people you don't like. You can swap " +
            "between the two lists using the button in the top right corner of the menu. Speak with Brother " +
            "Brace to learn more.";

    private final String leave = "<col=0000ff>Your final instructor!<br>You're almost finished on tutorial " +
            "island. Pass through the door to find the path leading to your final instructor.";
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void intro(){
        Utils.walkToPriest(abc);
        Utils.talkTo("Brother Brace");
    }

    private void prayerMenu(){
        GameTab.open(GameTab.TABS.PRAYERS);
    }

    private void prayerExplain(){
        Utils.walkToPriest(abc);
        Utils.talkTo("Brother Brace");
    }

    private void friends(){
        GameTab.open(GameTab.TABS.FRIENDS);
    }

    private void friendsExplain(){
        Utils.walkToPriest(abc);
        Utils.talkTo("Brother Brace");
    }

    private void leave(){
        Utils.walkToWizard(abc);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        System.out.println("Priest");
        if(Interfaces.get(263,1,0).getText().equals(intro)) {
            System.out.println("Introducing Brother Brace!");
            intro();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(prayerMenu)) {
            System.out.println("Opening prayer menu");
            prayerMenu();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(prayerExplain)) {
            System.out.println("gettin the rundown on prayers!");
            prayerExplain();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(friends)) {
            System.out.println("Opening friends list");
            friends();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(friendsExplain)) {
            System.out.println("leaning wtf a friend is");
            friendsExplain();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(leave)) {
            System.out.println("Getting the fuck away from this child molester priest");
            leave();
            System.out.println("Finished");
        }

        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 16 || Game.getSetting(406) == 17)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}