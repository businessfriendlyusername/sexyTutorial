package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.API.BInventory;
import scripts.API.Firemaking;

import java.util.function.BooleanSupplier;

public class GielinorGuide extends Node {

    GielinorGuide(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String createChar = "<col=0000ff>Setting your appearance</col><br>Before you get started, you'll " +
            "need to set the appearance of your character. Please use the open interface to set your appearance.";

    private final String guideIntro = "<col=0000ff>Getting started</col><br>Before you begin, have a read through " +
            "the controls guide in the top left of the screen. When you're ready to get started, click on the " +
            "Gielinor Guide. He is indicated by a flashing yellow arrow.";

    private final String openOptions = "<col=0000ff>Options menu</col><br>Please click on the flashing spanner " +
            "icon found at the bottom right of your screen. This will display your options menu.";

    private final String guideOutro = "<col=0000ff>Options menu</col><br>On the side panel, you can now see a " +
            "variety of game options such as screen brightness and music volume. Don't worry about these too " +
            "much for now, they will become clearer as you explore the game. Talk to the Gielinor Guide to continue.";

    private final String leave = "<col=0000ff>Moving on</col><br>It's time to meet your first instructor. To " +
            "continue, all you need to do is click on the door. It's indicated by a flashing yellow arrow. " +
            "Remember, you can use your arrow keys to rotate the camera.";


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void createChar(){
        int male = 136;//male/female button IDS
        int female = 137;

        if (General.random(0,1) == 1)
            Interfaces.get(269,male).click("Male");
        else
            Interfaces.get(269,female).click("Female");
        General.sleep(100, 200);
        final int[] leftArrows = {106,107,108,109,110,111,112,105,123,122,124,125};
        final int[] rightArrows = {113,114,115,116,117,118,119,121,127,129,130,131};
        final String[] options = {
                "Change head",
                "Change jaw",
                "Change torso",
                "Change arms",
                "Change hands",
                "Change legs",
                "Change feet",
                "Recolour hair",
                "Recolour torso",
                "Recolour legs",
                "Recolour feet",
                "Recolour skin"
        };
        for (int i = 0; i < 12; i++) {
            randomCharFeature(leftArrows[i], rightArrows[i], options[i]);
            if (General.random(0, 5) == 5) {
                int rand = General.random(0, Math.max(i - 1, 0));//0 <= rand <= 11
                randomCharFeature(leftArrows[rand], rightArrows[rand], options[rand]);
            }
        }
        int extra_changes = General.randomSD(3, 1);
        for (int i = 0; i <= extra_changes; i++) {
            int rand = General.random(0, Math.max(i - 1, 0));
            randomCharFeature(leftArrows[rand], rightArrows[rand], options[rand]);
        }
        Interfaces.get(269,100).click("Accept");
    }

    public void guideIntro(){
        Utils.talkTo("Gielinor Guide");
        Clicking.click(Interfaces.get(219,1,2));
        General.sleep(300,550);
        Clicking.click(NPCChat.getClickContinueInterface());
    }

    public void openOptions(){
        GameTab.open(GameTab.TABS.OPTIONS);
    }

    public void guideOutro(){
        Utils.talkTo("Gielinor Guide");
    }

    public void leave(){
        Utils.walkToSurvivalExpert(abc);
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific helper functions~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void randomCharFeature(int l, int r, String option){//l is the interface child ID of the left arrow r is the ID of the right
        RSInterfaceChild left = Interfaces.get(269,l);
        RSInterfaceChild right = Interfaces.get(269,r);
        int max_shifts = General.randomSD(0,40,10);
        int set_shifts = General.random(0, max_shifts);
        boolean swap = true;
        int shifts = 0;
        while(shifts < max_shifts) {//loop for randomizing charachter initialization
            for (int i = 0; i < set_shifts; i++) {
                if (swap)
                    left.click(option);
                else
                    right.click(option);
                shifts++;
                General.sleep(5, 20);
            }
            if (General.random(0, 1) == 1)
                swap = true;
            else
                swap = false;
            set_shifts = General.random(0, max_shifts - set_shifts);
        }
    }//used in character creation


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        //the interface for tutorial island chatbox instructions
        if(Interfaces.get(263,1,0).getText().equals(createChar)) {
            System.out.println("Randomly creating our character");
            createChar();
        }
        else if(Interfaces.get(263, 1, 0).getText().equals(guideIntro)) {
            System.out.println("Introduction to the gielinor guide");
            guideIntro();
        }
        else if(Interfaces.get(263, 1, 0).getText().equals(openOptions)) {
            System.out.println("Opening options tab");
            openOptions();
        }
        else if(Interfaces.get(263, 1, 0).getText().equals(guideOutro)) {
            System.out.println("Talking to the guide");
            guideOutro();
        }
        else if(Interfaces.get(263, 1, 0).getText().equals(leave)) {
            System.out.println("Walking to the survival expert");
            leave();
        }
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 0)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}
