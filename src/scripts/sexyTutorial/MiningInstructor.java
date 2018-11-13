package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.API.BInventory;
import scripts.API.Node;


import java.util.function.BooleanSupplier;

public class MiningInstructor extends Node {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final String[] dontDrop = {"Bronze pickaxe", "Tin ore", "Copper ore", "Bronze bar", "Hammer", "Bronze dagger"};

    private final String intro = "<col=0000ff>Mining and Smithing</col><br>Next let's get you a weapon, or more to " +
            "the point, you can make your first weapon yourself. Don't panic, the mining instructor will help you. " +
            "Talk to him and he'll tell you all about it.";

    private final String miningTin = "<col=0000ff>Mining</col><br>It's quite simple really. To mine a rock, all you " +
            "need to do is click on it. First up, try mining some tin. If you're unsure which rock is which, you " +
            "can right-click on one and select the prospect option.";

    private final String miningCopper = "<col=0000ff>Mining</col><br>Now that you have some tin ore, you just need " +
            "some copper. To mine a rock, all you need to do is click on it. If you're unsure which rock is which, " +
            "you can right-click on one and select the prospect option.";

    private final String smelting = "<col=0000ff>Smelting</col><br>You now have some tin ore and some copper ore. " +
            "You can smelt these into a bronze bar. To do so, just click on the indicated furnace. Try it now.";

    private final String smelting2 = "<col=0000ff>Smelting</col><br>You've made a bronze bar! Speak to the mining " +
            "instructor and he'll show you how to make it into a weapon.";

    private final String smithing = "<col=0000ff>Smithing a dagger</col><br>To smith you'll need a hammer and " +
            "enough metal bars to make the desired item, as well as a handy anvil. To start the process, " +
            "click on the anvil, or alternatively use the bar on it.";

    private final String movingOn = "<col=0000ff>Moving on</col><br>Congratulations, you've made your first weapon. " +
            "Now it's time to move on. Go through the gates shown by the arrow. Remember, you may need to move the " +
            "camera to see your surroundings. Speak to the mining instructor for a recap at any time.";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void intro(){
        Utils.walkToMiningInstructor();
        if(Inventory.getAll().length == 28)//inventory is full, drop some shit
            BInventory.dropAllExceptOne(dontDrop);
        Utils.talkTo("Mining Instructor");
    }

    private void miningTin(){
        if(Inventory.find("Tin ore").length > 0)
            return;
        Utils.walkToMiningInstructor();
        if(Inventory.getAll().length == 28)
            BInventory.dropAllExceptOne(dontDrop);
        RSObject[] tinRock = Objects.findNearest(15, 10080);
        if(tinRock.length < 1)
            return;

        for(RSObject rock : tinRock) {//iterate through the rocks incase they've already been mined
            while(!rock.click("Mine"))
                Camera.turnToTile(rock);
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Inventory.getCount("Tin ore") > 0;
                }
            }, General.random(4000, 5000));
            if (Inventory.getCount("Tin ore") > 0)//we got an ore, break the loop
                break;
        }
    }

    private void miningCopper(){
        if(Inventory.find("Copper ore").length > 0)
            return;
        Utils.walkToMiningInstructor();
        if(Inventory.getAll().length == 28)
            BInventory.dropAllExceptOne(dontDrop);
        RSObject[] copperRock = Objects.findNearest(15, 10079);
        if(copperRock.length < 1)
            return;

        for(RSObject rock : copperRock){//iterate through the rocks incase they've already been mined
            while(!rock.click("Mine"))
                Camera.turnToTile(rock);
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Inventory.getCount("Copper ore") > 0;
                }
            }, General.random(4000, 5000));
            if(Inventory.getCount("Copper ore") > 0)//we got an ore, break the loop
                break;
        }
    }

    private void smelting(){
        Utils.walkToMiningInstructor();
        if(Inventory.getCount("Copper ore") == 0)
            miningCopper();
        if(Inventory.getCount("Tin ore") == 0)
            miningTin();
        RSObject[] furnace = Objects.findNearest(15, "Furnace");
        if(furnace.length < 1)
            return;
        if(!furnace[0].click("Use")){
            Camera.turnToTile(furnace[0]);
            furnace[0].click("Use");
        }
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Inventory.getCount("Bronze bar") == 1;
            }
        }, General.random(8000, 10000));
    }

    private void smelting2(){
        Utils.walkToMiningInstructor();
        if(Inventory.getAll().length == 28)
            BInventory.dropAllExceptOne(dontDrop);
        Utils.talkTo("Mining Instructor");
    }

    private void smithing(){
        Utils.walkToMiningInstructor();
        if(Inventory.getCount("Bronze bar") == 0)
            smelting();
        if(Inventory.getCount("Hammer") == 0)
            smelting2();

//~~~~~~~~~~~~~~~~~~~Safety check complete~~~~~~~~~~~~~~~~~

        RSObject[] anvil = Objects.findNearest(10, "Anvil");
        if(anvil.length < 1)
            return;
        if(!anvil[0].click("Smith")) {
            Camera.turnToTile(anvil[0]);
            anvil[0].click("Smith");
        }
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Interfaces.get(312, 2, 2) != null;//the bronze dagger icon
            }
        }, General.random(8000, 12000));
        General.sleep(600, 900);
        Interfaces.get(312,2,2).click("Smith 1");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Inventory.getCount("Bronze dagger") == 1;
            }
        }, General.random(3000, 5000));
    }

    private void movingOn(){
        Utils.walkToCombatInstructor();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        System.out.println("Mining Instructor");
        if(Interfaces.get(263,1,0).getText().equals(intro)) {
            System.out.println("Introducing the mining instructor");
            intro();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(miningTin)) {
            System.out.println("Mining some tin");
            miningTin();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(miningCopper)) {
            System.out.println("Mining some copper");
            miningCopper();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(smelting)) {
            System.out.println("smelting a bronze bar");
            smelting();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(smelting2)) {
            System.out.println("talking to the instructor again");
            smelting2();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(smithing)) {
            System.out.println("making a bronze dagger");
            smithing();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(movingOn)) {
            System.out.println("going to the combat instructor");
            movingOn();
            System.out.println("Finished");
        }
        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 8 || Game.getSetting(406) == 9)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}