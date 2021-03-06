package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.API.AntiBan;
import scripts.API.BInventory;
import scripts.API.Node;

import java.util.function.BooleanSupplier;

public class CombatInstructor extends Node {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final String[] dontDrop = {"Bronze dagger", "Bronze arrow", "Wooden shield", "Bronze sword", "Shortbow"};

    private final String intro = "<col=0000ff>Combat</col><br>In this area you will find out about melee and " +
            "ranged combat. Speak to the guide and he will tell you all about it.";

    private final String equipedItems = "<col=0000ff>Equipping items</col><br>You now have access to a " +
            "new interface. Click on the flashing icon of a man, the one to the right of your backpack icon.";

    private final String wornInventory = "<col=0000ff>Worn inventory</col><br>This is your worn inventory. Here " +
            "you can see what items you have equipped. In the bottom left corner, you will notice a flashing " +
            "button with a shield and helmet on it. This button lets you view more details on what you have " +
            "equipped. Click on it now.";

    private final String equipDagger = "<col=0000ff>Equipment stats</col><br>You can see what items you are " +
            "wearing in the worn inventory to the left of the screen, with their combined statistics on the right. " +
            "Let's add something. Click your dagger to equip it.";

    private final String equipmentStats = "<col=0000ff>Equipment stats</col><br>You're now holding your dagger. " +
            "Clothes, armour, weapons and more are equipped like this. You can unequip items by clicking on them " +
            "in the worn inventory. Speak to the combat instructor to continue.";

    private final String unequippingItems = "<col=0000ff>Unequipping items</col><br>To unequip an item, go to your " +
            "worn inventory and click on the item. Alternatively, equipping a new item into the same slot will " +
            "unequip the old one. Try this out now by swapping your dagger for the sword and shield that the combat " +
            "instructor gave you.";

    private final String combatInterface = "<col=0000ff>Combat interface</col><br>Click on the flashing " +
            "crossed swords icon to open the combat interface.";

    private final String combatInterface2 = "<col=0000ff>Combat interface</col><br>This is your combat interface. " +
            "From here, you can select the attack style that you'll use in combat. Using different attack styles " +
            "will give different types of experience. As well as this, monsters are weak to specific attack styles. " +
            "Click on the gates to continue.";

    private final String attacking = "<col=0000ff>Attacking</col><br>It's time to slay some rats! To attack a rat, " +
            "all you have to do is click on it. This will cause you to walk over and start hitting it.";

    private final String returnToInstructor = "<col=0000ff>Well done, you've made your first kill!</col><br>Pass " +
            "through the gate and talk to the combat instructor. He will give you your next task.";

    private final String murderRatRanged = "<col=0000ff>Rat ranging</col><br>Now you have a bow and some arrows. " +
            "Before you can use them you'll need to equip them. Once equipped with the ranging gear, try killing " +
            "another rat. You don't need to enter the pen this time. To attack a rat, just click on it.";

    private final String movingOn = "<col=0000ff>Moving on</col><br>You have completed the tasks here. To move on, " +
            "click on the indicated ladder. If you need to go over any of what you learnt here, just talk to the " +
            "combat instructor and he'll tell you what he can.";
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void intro(){
        Utils.walkToCombatInstructor();
        Utils.talkTo("Combat Instructor");
    }

    private void equipedItems(){
        GameTab.open(GameTab.TABS.EQUIPMENT);
    }

    private void wornInventory(){
        GameTab.open(GameTab.TABS.EQUIPMENT);
        General.sleep(600, 800);
        RSInterfaceChild equipStats = Interfaces.get(387, 17);
        if(equipStats == null)
            return;
        equipStats.click("View equipment stats");
    }

    private void equipDagger(){
        Utils.walkToCombatInstructor();
        if(Inventory.getCount("Bronze dagger") == 0)
        {
            if(Inventory.getAll().length >= 20)
                BInventory.dropAllExceptOne(dontDrop);
            Utils.talkTo("Combat Instructor");
        }

        wornInventory();
        General.sleep(800, 1200);
        RSItem[] dagger = Inventory.find("Bronze dagger");
        if(dagger.length < 1)
            return;
        dagger[0].click("Equip");
    }

    private void equipmentStats(){
        Interfaces.closeAll();
        Utils.walkToCombatInstructor();
        if(Inventory.getAll().length > 20)
            BInventory.dropAllExceptOne(dontDrop);
        Utils.talkTo("Combat Instructor");
    }

    private void unequippingItems(){
        Utils.walkToCombatInstructor();
        if(Inventory.getAll().length > 20)
            BInventory.dropAllExceptOne(dontDrop);
        if(Inventory.getCount("Bronze sword") == 0 || Inventory.getCount("Wooden shield") == 0)
            Utils.talkTo("Combat Instructor");
        RSItem[] sword = Inventory.find("Bronze sword");
        RSItem[] shield = Inventory.find("Wooden shield");
        if(shield.length < 1 || sword.length < 1)
            return;
        sword[0].click("Wield");
        General.sleep(200, 350);
        shield[0].click("Wield");
    }

    private void combatInterface(){
        GameTab.open(GameTab.TABS.COMBAT);
    }

    private void combatInterface2(){
        Utils.walkToRatCage();
    }

    private void attacking(){
        Utils.walkToRatCage();
        RSNPC[] rat = NPCs.findNearest("Giant rat");
        if(rat.length < 1)
            return;
        RSNPC ratToMurder = null;
        for(RSNPC r : rat){
            if(!r.isInCombat()) {//the rat is not in combat
                if(!r.click("Attack")){
                    Camera.turnToTile(r);
                    r.click("Attack");
                }
                Timing.waitCondition(new BooleanSupplier() {//wait until the rat we clicked on is in combat
                    @Override
                    public boolean getAsBoolean() {
                        General.sleep(100);
                        return r.isInCombat();
                    }
                }, General.random(4000, 6000));
                if(Combat.isUnderAttack() && r.isInteractingWithMe() && r.isInCombat()){//if we are in combat with the rat break out of the loop
                    ratToMurder = r;
                    break;
                }
            }
        }
        if(ratToMurder == null)
            return;
        long startTime = Timing.currentTimeMillis();
        while(ratToMurder.getAnimation() != 4935 && Timing.timeFromMark(startTime) < 20000){//wait for rat death animation or timeout after 20 seconds
            General.sleep(300);
        }
    }

    private void returnToInstructor(){
        Utils.walkToCombatInstructor();
        if(Inventory.getAll().length > 20)
            BInventory.dropAllExceptOne(dontDrop);
        Utils.talkTo("Combat Instructor");
    }

    private void murderRatRanged(){
        Utils.walkToCombatInstructor();
        if(Inventory.getAll().length > 20)
            BInventory.dropAllExceptOne(dontDrop);
        if(Inventory.getCount("Bronze arrow") == 0 && !Equipment.isEquipped("Bronze arrow"))//we don't have arrows and we haven't equipped arrows
            Utils.talkTo("Combat Instructor");
        if(Inventory.getCount("Shortbow") == 0 && !Equipment.isEquipped("Shortbow"))
            Utils.talkTo("Combat Instructor");
        General.sleep(400, 600);
        RSItem[] bow = Inventory.find("Shortbow");
        RSItem[] arrows = Inventory.find("Bronze arrow");
        if(bow.length < 1 && !Equipment.isEquipped("Shortbow")|| arrows.length < 1 && !Equipment.isEquipped("Bronze arrow"))
            return;
        bow[0].click("Wield");
        arrows[0].click("Wield");
        General.sleep(200, 500);
        RSNPC[] rat = NPCs.findNearest("Giant rat");
        if(rat.length < 1)
            return;
        RSNPC ratToMurder = null;
        for(RSNPC r : rat){
            if(!r.isInCombat()) {//the rat is not in combat
                if(!r.click("Attack")) {
                    Camera.turnToTile(r);
                    r.click("Attack");//try to attack the rat
                }
                Timing.waitCondition(new BooleanSupplier() {//wait until the rat we clicked on is in combat
                    @Override
                    public boolean getAsBoolean() {
                        General.sleep(100);
                        return r.isInCombat();
                    }
                }, General.random(4000, 6000));
                if(r.isInteractingWithMe() && r.isInCombat()){//if we are in combat with the rat break out of the loop
                    ratToMurder = r;
                    break;
                }
            }
        }
        if(ratToMurder == null)
            return;
        long startTime = Timing.currentTimeMillis();
        while(ratToMurder.getAnimation() != 4935 && Timing.timeFromMark(startTime) < 20000){//wait for rat death animation or timeout after 20 seconds
            General.sleep(300);
        }
    }

    private void movingOn(){
        Utils.walkToOutsideBank();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        System.out.println("Combat Instructor");
        if(Interfaces.get(263,1,0).getText().equals(intro)) {
            System.out.println("Introducing the combat instructor");
            intro();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(equipedItems)) {
            System.out.println("Viewing equiped Items");
            equipedItems();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(wornInventory)) {
            System.out.println("Opening worn inventory");
            wornInventory();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(equipDagger)) {
            System.out.println("Equipping my fukin dagger");
            equipDagger();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(equipmentStats)) {
            System.out.println("Closing interface and talking to the instructor");
            equipmentStats();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(unequippingItems)) {
            System.out.println("Equipping sword and shield");
            unequippingItems();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(combatInterface)) {
            System.out.println("Opening combat interface");
            combatInterface();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(combatInterface2)) {
            System.out.println("Walking to rat cage");
            combatInterface2();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(attacking)) {
            System.out.println("attempting to murder a rat");
            attacking();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(returnToInstructor)) {
            System.out.println("going back to the instructor");
            returnToInstructor();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(murderRatRanged)) {
            System.out.println("shooting this fukin rat dead");
            murderRatRanged();
            System.out.println("Finished");
        }
        else if(Interfaces.get(263,1,0).getText().equals(movingOn)) {
            System.out.println("going to the next instructor");
            movingOn();
            System.out.println("Finished");
        }

        General.sleep(800, 1200);
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 10 || Game.getSetting(406) == 11 || Game.getSetting(406) == 12)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}