package scripts;
import org.tribot.api.DynamicClicking;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api.General;

import java.util.function.BooleanSupplier;

//fishing spot in lumbridge swamp. pos:(3241, 3149)
//lumbridge castle south staircase pos:(3207, 3209)


public class shrimp_fisher extends Script{

    private RSObject last_fishing_spot;//the last spot being fished
    private RSTile last_fishing_tile;//the location of the last spot being fished


    private boolean isAtFish(){
        final RSNPC[] fishing_spots = NPCs.findNearest("Fishing spot");
        if (fishing_spots.length < 1){
            return false;
        }
        return fishing_spots[0].isOnScreen();
    }

    private boolean isInBank(){
        final RSObject[] bank_booths = Objects.findNearest(10, "Bank booth");
        if (bank_booths.length >= 1){
            if (bank_booths[0].isOnScreen())
                return true;
        }

        final  RSNPC[] bankers = NPCs.findNearest("Banker");
        if (bankers.length < 1){
            return false;
        }
        return bankers[0].isOnScreen();
    }

    private boolean walkToBank(){
        if (!WebWalking.walkToBank(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Interfaces.get(595, 37) != null;//if we accidentaly open the world map
            }
        }, 500)){
            if(Interfaces.get(595, 37) != null)
                Interfaces.get(595, 37).click("Close");
            return false;
        }

        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(200, 300);
                return isInBank();
            }
        }, General.random(8000,9000));
    }

    private boolean bank(){
        if (!Banking.isBankScreenOpen()){
            if(!Banking.openBank())//we were unable to open the bank
                return false;
        }
        final String tool = "Small fishing net";
        if (Inventory.find(tool).length < 1){//we do not have a net in our inventory
            if (Banking.depositAll() < 1)//we failed to deposit
                return false;
            sleep(General.random(5, 30));
            if (!Banking.withdraw(1, tool))//we failed to withdraw our fishing net
                return false;
        }
        else{//we have our tool in our inventory
            if (Banking.depositAllExcept(tool) < 1)//we failed to deposit
                return false;
        }
        return Timing.waitCondition(new Condition() { // Since we can only enter the bank method if our inventory is full,
            // let's wait until our inventory is not full. If it is not full
            // before the timeout, return true. Otherwise, return false.
            @Override
            public boolean active() {
                return !Inventory.isFull();
            }
        }, General.random(3000, 4000));
    }

    private boolean walkToFish(){
        final RSTile fishing_spot = new RSTile(3241,3149,0);
        if(!WebWalking.walkTo(fishing_spot, new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Interfaces.get(595, 37) != null;//webwalking opened the world map
            }
        }, 500))
        {
            if(Interfaces.get(595, 37) != null)
                Interfaces.get(595, 37).click("Close");
            return false;
        }
        return Timing.waitCondition(new Condition() { // If we reach the trees before the timeout, this method will return
            // true. Otherwise, it will return false.
            @Override public boolean active() {
                General.sleep(200, 300); // Reduces CPU usage.
                return isAtFish();
            }
        }, General.random(8000, 9000));
    }

    private boolean isFishing(){

        return Player.getAnimation() == 621;//621 is the small net fishing animation
    }

    private boolean fish(){
        final long timeout = System.currentTimeMillis() + General.random(60000, 90000);

        while (isFishing() && System.currentTimeMillis() < timeout){
            General.sleep(100,150);
            //anti ban goes here
        }
        RSNPC[] fishing_spots = NPCs.findNearest("Fishing spot");
        if (fishing_spots.length < 1)
            return false;//no fishing spots found, cannot fish

        if (!fishing_spots[0].isOnScreen()){
            if (!Walking.walkPath(Walking.generateStraightPath(fishing_spots[0])))//we cannot path to the fishing spot
                return false;

            if (!Timing.waitCondition(new Condition() { // We will now use the Timing API to wait until the tree is on
                // the screen (we are probably walking to the tree right now).
                @Override
                public boolean active() {
                    General.sleep(100); // Sleep to reduce CPU usage.
                    return fishing_spots[0].isOnScreen();
                }
            }, General.random(8000, 9300)))
                // A tree could not be found before the timeout of 8-9.3
                // seconds. Let's exit the method and return false. we don't
                // want to end up trying to click a tree which isn't on the
                // screen.
                return false;
        }
        if (!DynamicClicking.clickRSNPC(fishing_spots[0], "Net"))//we could not click the fishing spot
            return false;

        Timing.waitCondition(new Condition() {//we just clicked the fishing spot, wait a second while we path to it
            @Override
            public boolean active() {
                return !isFishing();
            }
        }, General.random(1000, 1200));

        if (Timing.waitCondition(new Condition() {
            // Now let's wait until we are fishing.
            @Override
            public boolean active() {
                return isFishing();
            }
        }, General.random(8000, 9000))) {
            this.last_fishing_tile = fishing_spots[0].getPosition().clone();
            return true;
        }
        return false;//we failed to fish
    }
    @Override
    public void run(){
        while (true){
            sleep(50);
            if(isAtFish()){
                if (Inventory.isFull()){
                    walkToBank();
                }
                else
                    fish();
            } else if (isInBank()) {
                if (Inventory.isFull())
                    bank();
                else
                    walkToFish();
            } else {
                if (Inventory.isFull())
                    walkToBank();
                else
                    walkToFish();
            }
        }
    }
}
