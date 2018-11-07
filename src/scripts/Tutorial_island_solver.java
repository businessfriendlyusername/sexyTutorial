package scripts;
import com.sun.tools.hat.internal.model.StackTrace;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.api.DynamicClicking;
import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.api2007.Game;


import scripts.shrimp_fisher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Tutorial_island_solver extends Script{

    //tabs are children of 548
    private int getStep(){
        int progress_len = Interfaces.get(614,18).getWidth();//the width of the progress bar, used to track progress

        if (progress_len == 0){//we're still in the first house

        }
        else if (progress_len == 31){//we just left the first house talk to survival expert

        }
        else if (progress_len == 47){//we chopped a tree and lit a fire with the survival expert

        }
        else if (progress_len == 63){//we cooked shrimp and made it through the gate

        }
        else if (progress_len == 80){//we just cooked bread (bread may have been burnt)

        }
        else if (progress_len == 95){//we just left the cooks house

        }
        else if (progress_len == 111){//we just entered the quest guide's house

        }
        else if (progress_len == 127){//we just entered the mines

        }
        else if (progress_len == 143){//we just mined some copper and tin

        }
        else if (progress_len == 160){//we finished the smithing tutorial and went through the gate

        }
        else if (progress_len == 175){//we talked to the combat instructor and successfully opened the combat interface

        }
        else if (progress_len == 191){//we just recieved our bow and arrows from the combat instructor

        }
        else if (progress_len == 223){//we exited the mines and are on our way to the bank

        }
        else if (progress_len == 240){//we walked into the side room in the bank to talk to the account manager

        }
        else if (progress_len == 255){//we just exited the side room with the account manager

        }
        else if (progress_len == 271){//we just opened our friends list, we may still need to finish dialogue with the monk

        }
        else if (progress_len == 287){//we just left the church

        }
        else if (progress_len == 320){//we were just given runes by the wizard

        }
        return 0;
    }

    private boolean talkTo(RSNPC npc){
        flush();
        int attempts = 0;
        while(attempts < 5 && (NPCChat.getMessage() == null || !NPCChat.getName().equals(npc.getName()))){
            Camera.turnToTile(npc.getPosition());
            if(npc.isOnScreen())
                return DynamicClicking.clickRSNPC(npc, "Talk-to");//return true if we can click on a nigga
            attempts++;
            General.sleep(150, 300);
        }
        return false;//too many failed attempts to click
    }

    //TUTORIAL ISLAND PROGRESS INTERFACE MASTER 614 child 18
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
    }

    //break out of weird text that confuses the bot
    private void flush(){
        if(!Interfaces.get(162,45).isHidden() && Interfaces.get(162,45).getText().equals("Click to continue")){//flush annoying messages
            Clicking.click(Interfaces.get(162,45));
        }
    }

//Gamesetting = 0
    private boolean Task0(){//random character design

        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>Setting " +
                "your appearance</col><br>Before you get started, you'll need to set the appearance" +
                " of your character. Please use the open interface to set your appearance."))
        {
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
                    int rand = General.random(0, Math.max(i - 1, 0));
                    randomCharFeature(leftArrows[rand], rightArrows[rand], options[rand]);
                }
            }
            int extra_changes = General.randomSD(3, 1);
            for (int i = 0; i <= extra_changes; i++) {
                int rand = General.random(0, Math.max(i - 1, 0));
                randomCharFeature(leftArrows[rand], rightArrows[rand], options[rand]);
            }
            Interfaces.get(269,100).click("Accept");
            General.sleep(500,1500);
        }
        while(Interfaces.get(263,1,0).getText().equals
                ("<col=0000ff>Getting started</col><br>To start the tutorial use your" +
                        " left mouse button to click on the Gielinor Guide in this room. He is" +
                        " indicated by a flashing yellow arrow above his head. If you can't" +
                        " see him, use your keyboard's arrow keys to rotate the view."))
        {
            RSNPC[] temp = NPCs.findNearest("Gielinor Guide");
            if(temp.length < 1)
                return false;
            RSNPC guide = temp[0];
            if (!talkTo(guide))
                return false;//failed to talk to the Gielinor guide :(

            for (int i = 0; i < 6; i++){
                Clicking.click(NPCChat.getClickContinueInterface());
                General.sleep(2000, 4000);
            }

            Clicking.click(Interfaces.get(219,1,2));
            General.sleep(300,550);
        }

        while(Interfaces.get(263,1,0).getText().equals
                ("<col=0000ff>Player controls</col><br>Please click on the " +
                        "flashing spanner icon found at the bottom right of your screen. " +
                        "This will display your player controls."))
        {
            if (!GameTab.open(GameTab.TABS.OPTIONS))
                return false;
        }

        while(Interfaces.get(263,1,0).getText().equals
                ("<col=0000ff>Player controls</col><br>On the side panel, " +
                        "you can now see a variety of options from changing the brightness" +
                        " of the screen and the volume of music, to selecting whether your " +
                        "player should accept help from other players. Don't worry about " +
                        "these too much for now, they will become clearer as you explore " +
                        "the game. Talk to the Gielinor Guide to continue."))
        {
            RSNPC[] temp = NPCs.findNearest("Gielinor Guide");
            if(temp.length < 1)
                return false;
            RSNPC guide = temp[0];
            if (!talkTo(guide))
                return false;

            Clicking.click(NPCChat.getClickContinueInterface());


            while(!Options.isRunEnabled()) {
                General.sleep(10, 100);
                Options.setRunEnabled(true);
            }
            if (!talkTo(guide))
                return false;
            Clicking.click(NPCChat.getClickContinueInterface());
            //Exit building through the door
            RSObject[] door = Objects.findNearest(10, 9398);//The ID of the door
            if (door.length < 1)//couldn't find the door fam
                return false;
            Camera.turnToTile(door[0].getPosition());
            DynamicClicking.clickRSObject(door[0],1);
        }
        return true;
    }

    //Gamesetting = 2
    private void Task2Recover(){
        RSTile inside = new RSTile(3097,3107);//the tile infront of the door inside the first building
        WebWalking.walkTo(inside);
        General.sleep(750, 1500);
        if(Player.getPosition() == inside)//the player is still inside the first building, get him out
        {
            RSObject[] door = Objects.findNearest(10, 9398);//The ID of the door
            if (door.length < 1)//couldn't find the door fam
                return;
            Camera.turnToTile(door[0].getPosition());
            DynamicClicking.clickRSObject(door[0], "Open");
            General.sleep(300, 500);//sleep while we walk through the door
        }
    }

    private boolean Task2(){
        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>" +
                "Moving around</col><br>Follow the path to find the next instructor. " +
                "Clicking on the ground will walk you to that point. Talk to the Survival " +
                "Expert by the pond to continue the tutorial. Remember you can rotate the view by pressing the arrow keys."))
        {
            RSTile nearInstructor = new RSTile(3102,3095);
            WebWalking.walkTo(nearInstructor);
            RSNPC[] temp = NPCs.findNearest("Survival Expert");
            if (temp.length < 1)
                return false;
            RSNPC instructor = temp[0];
            Camera.turnToTile(instructor);
            DynamicClicking.clickRSNPC(instructor, "Talk-to");
            for (int i = 0; i < 2; i++){
                Clicking.click(NPCChat.getClickContinueInterface());
                General.sleep(500, 1000);
            }
        }

        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>Viewing the items " +
                "that you were given.</col><br>Click on the flashing backpack icon to the right hand side of " +
                "the main window to view your inventory. Your inventory is a list of everything you have in your backpack."))
        {
            Interfaces.get(548,51).click("Inventory");//the inventory interface
            sleep(250, 350);
        }

        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>Cut down a tree<br>You can click " +
                "on the backpack icon at any time to view the items that you currently have in your inventory. You will see " +
                "that you now have an axe in your inventory. Use this to get some logs by clicking on one of the trees in the area."))
        {
            RSTile byTree = new RSTile(3101,3095);
            WebWalking.walkTo(byTree);
            RSObject[] temp = Objects.findNearest(3, "Tree");
            if(temp.length < 1)
                return false;
            RSObject tree = temp[0];
            if(!tree.isOnScreen())
                Camera.turnToTile(tree.getPosition());
            if(!tree.isClickable())
                return false;
            Clicking.click(tree);
        }

        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>Please wait.</col><br><br>Your" +
                " character is now attempting to cut down the tree. Sit back for a moment while he does all the hard work."))
        {
            General.sleep(300,700);
        }

        while(Interfaces.get(263,1,0).getText().equals("<col=0000ff>Making a fire</col><br>Well " +
                "done! You managed to cut some logs from the tree! Next, use the tinderbox in your inventory to light " +
                "the logs.<br>First click on the tinderbox to 'use' it.<br>Then click on the logs in your inventory " +
                "to light them."))
        {
            break;
        }

        while(Interfaces.get(263,1,0).getText().equals(""))
        {

        }

        while(Interfaces.get(263,1,0).getText().equals(""))
        {

        }

        return true;
    }


    private void combatTask(){
        while (Interfaces.get(548,52).isHidden()){//instructor hasnt shown use worn interface yet
            RSTile by_instructor = new RSTile(3105,9508);
            WebWalking.walkTo(by_instructor);
            final RSNPC[] temp = NPCs.findNearest("Combat Instructor");
            if (temp.length < 1)//we couldn't find the instructor
                continue;
            RSNPC instructor = temp[0];
            talkTo(instructor);
            for (int i = 0; i < 3; i++){
                Clicking.click(NPCChat.getClickContinueInterface());
            }
        }


    }

    private void text_collector()//collects step messages for classification
    {
        String previous_text = "None";
        int i = 0;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/belvis/.tribot/logs/tutorial-island/Tutorial_steps.dat", true));

            while (true) {
                sleep(100);
                if (Interfaces.get(263, 1, 0) == null) {
                    System.out.println("Interface is null!");
                    writer.write('\n');
                    writer.close();
                    break;
                }
                if (!Interfaces.get(263, 1, 0).getText().equals(previous_text))//the text changed
                {
                    previous_text = Interfaces.get(263, 1, 0).getText();
                    System.out.println(previous_text);
                    i++;
                    writer.write(previous_text + "<step>" + i + "</step>" + "@");
                    System.out.println(i);
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private boolean running = true;


    @Override
    public void run(){
        while(running)
        {
            General.sleep(1000);
            System.out.println(Game.getSetting(406));
        }
    }
}
