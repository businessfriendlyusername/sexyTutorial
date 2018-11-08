package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.API.BInventory;


import java.util.function.BooleanSupplier;

public class Cook extends Node {

    Cook(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String[] dontDrop = {"Pot of flour", "Bucket of water", "Bread dough"};

    private final String movingOn = "<col=0000ff>Moving on</col><br>Follow the path until you get to the door " +
            "with the yellow arrow above it. Click on the door to open it. Remember that you can also move around by " +
            "clicking on the minimap in the top right.";

    private final String cooking = "<col=0000ff>Cooking</col><br>Talk to the chef indicated. He will teach you the " +
            "more advanced aspects of Cooking such as combining ingredients.";

    private final String makingDough = "<col=0000ff>Making dough</col><br>This is the base for many meals. To make " +
            "dough you must mix flour with water. To do so, click on the flour in your inventory. Then, with the " +
            "flour highlighted, click on the water to combine them into dough.";

    private final String cookingDough = "<col=0000ff>Cooking dough</col><br>Now you have made the dough, you can " +
            "bake it into some bread. To do so, just click on the indicated range.";

    private final String movingOn2 = "<col=0000ff>Moving on</col><br>Well done! You've baked your first loaf of " +
            "bread. As you gain experience in Cooking, you will be able to make other things like pies and cakes. " +
            "You can now use the next door to move on. If you need a recap on anything, talk to the master chef.";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void movingOn(){
        Utils.walkToCook(abc);
    }

    private void cooking(){
        Utils.walkToCook(abc);
        if(Inventory.getAll().length >= 26)
            BInventory.dropAllExceptOne(dontDrop);
        Utils.talkTo("Master Chef");
    }

    private void makingDough(){
        Utils.walkToCook(abc);
        if(Inventory.getAll().length >= 27)
            BInventory.dropAllExceptOne(dontDrop);
        RSItem[] flour = Inventory.find("Pot of flour");
        RSItem[] water = Inventory.find("Bucket of water");
        if(flour.length < 1 || water.length < 1)
            return;
        flour[0].click("Use");
        Clicking.click(water[0]);
        General.sleep(400, 700);
    }

    private void cookingDough(){
        Utils.walkToCook(abc);
        RSObject[] range = Objects.findNearest(10, "Range");
        if (range.length < 1)
            return;
        range[0].click("Cook");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Inventory.getCount("Bread") > 0;
            }
        }, General.random(5000, 6000));
    }

    private void movingOn2(){
        Utils.walkToCook(abc);
        RSObject[] door = Objects.findNearest(10, 9710);
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        General.sleep(400, 600);
        door[0].click("Open");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Utils.behindCooksHouse();
            }
        }, General.random(4000,5000));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        if(Interfaces.get(263,1,0).getText().equals(movingOn)) {
            System.out.println("Walking to the cook");
            movingOn();
        }
        else if(Interfaces.get(263,1,0).getText().equals(cooking)) {
            System.out.println("Talking to the cook");
            cooking();
        }
        else if(Interfaces.get(263,1,0).getText().equals(makingDough)) {
            System.out.println("Mixing some dough");
            makingDough();
        }
        else if(Interfaces.get(263,1,0).getText().equals(cookingDough)) {
            System.out.println("Cooking the bread");
            cookingDough();
        }
        else if(Interfaces.get(263,1,0).getText().equals(movingOn2)) {
            System.out.println("Moving on the to quest guide");
            movingOn2();
        }
        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 4 || Game.getSetting(406) == 5)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}