package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.API.BInventory;
import scripts.API.Firemaking;

import java.util.function.BooleanSupplier;

public class SurvivalInstructor extends Node {

    SurvivalInstructor(ABCUtil a){
        abc = a;
    }//make all nodes share the same ABCUtil (My Java is a bit shaky, please correct me
    // if this isn't right, or if there's a better way to do this!!!)
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node specific variables~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ABCUtil abc;

    private final String[] dontDrop = {"Small fishing net", "Bronze axe", "Tinderbox", "Raw shrimps", "Logs"};

    private final String movingAround = "<col=0000ff>Moving around</col><br>Follow the path to find the next " +
            "instructor. Clicking on the ground will walk you to that point. You can also move around by clicking " +
            "a point on the minimap in the top right corner. Talk to the survival expert to continue the tutorial.";

    private final String openBackpack = "<col=0000ff>You've been given an item</col><br>To view the item you've been" +
            " given, you'll need to open your inventory. To do so, click on the flashing backpack icon to the right" +
            " hand side of your screen.";

    private final String fishing = "<col=0000ff>Fishing</col><br>This is your inventory. You can view all of your" +
            " items here, including the net you've just been given. Let's use it to catch some shrimp. To start " +
            "fishing, just click on the sparkling fishing spot, indicated by the flashing arrow.";

    private final String openStats = "<col=0000ff>You've gained some experience</col><br>Click on the flashing bar" +
            " graph icon near the inventory button to see your skills menu.";

    private final String skillsAndExp = "<col=0000ff>Skills and Experience</col><br>On this menu you can view your " +
            "skills. Your skills can be leveled up by earning experience, which is is gained by performing various " +
            "activites. As you level up your skills, you will earn new unlocks. Speak to the survival expert to continue.";

    private final String firemaking = "<col=0000ff>Firemaking</col><br>Now that you have some logs, it's time to " +
            "light a fire. First, click on the tinderbox in your inventory. Then, with the tinderbox highlighted, " +
            "click on the logs to use the tinderbox on them.";

    private final String woodcutting = "<col=0000ff>Woodcutting</col><br>It's time to cook your shrimp. However, " +
            "you require a fire to do that which means you need some logs. You can cut down trees using your " +
            "Woodcutting skill, all you need is an axe. Give it a go by clicking on one of the trees in the area.";

    private final String cooking = "<col=0000ff>Cooking</col><br>Now it's time to get cooking. To do so, click on " +
            "the shrimp in your inventory. Then, with the shrimp highlighted, click on a fire to cook them. If you " +
            "look at the top left of the screen, you'll see the instructions that you're giving to your character.";

    private final String movingOn = "<col=0000ff>Moving on</col><br>Well done, you've just cooked your first meal! " +
            "Speak to the survival expert if you want a recap, otherwise you can move on. Click on the gate shown " +
            "and follow the path. Remember, you can use your arrow keys to rotate the camera.";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Tutorial Step Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void movingAround(){
        Utils.walkToSurvivalExpert(abc);
        Utils.talkTo("Survival Expert");
    }

    private void openBackpack(){
        GameTab.open(GameTab.TABS.INVENTORY);
    }

    private void fishing(){
        if(Utils.isNearSurvivalExpert()) {//we are near the survival expert, continue the tutorial as normal
            if (Inventory.getCount("Small fishing net") == 0)//we lost our net somehow; talk to survival betch
                Utils.talkTo("Survival Expert");

            if (Inventory.isFull()) {//we filled up our inventory somehow; drop things.
                Inventory.dropAllExcept(dontDrop);
            }
            RSNPC[] fish = NPCs.findNearest("Fishing spot");
            if(fish.length < 1)//we couldn't find any nearby fishing spots
                return;
            while(!DynamicClicking.clickRSNPC(fish[0],"Net"))//try to click on the fishing spot, if we can't rotate the camera
                Camera.turnToTile(fish[0]);
            Timing.waitCondition(new BooleanSupplier() {//wait until we have some raw shrimps in our inventory
                @Override
                public boolean getAsBoolean() {
                    General.sleep(200);
                    return Inventory.getCount("Raw shrimps") > 0;
                }
            }, General.random(6000, 7000));
        }
        else//we aren't close to the survival expert, walk to her
            Utils.walkToSurvivalExpert(abc);//The node framework will call fishing() again, so we don't need to do anything else
    }

    private void openStats(){
        GameTab.open(GameTab.TABS.STATS);
    }

    private void skillsAndExp(){
        if(Inventory.getAll().length > 26)//we don't have enough inventory space for the axe and tinderbox
            BInventory.dropAllExceptOne(dontDrop);
        if(!Utils.isNearSurvivalExpert())//we aren't close to the instructor
            Utils.walkToSurvivalExpert(abc);
        Utils.talkTo("Survival Expert");
    }

    private void woodcutting(){
        if(!Utils.isNearSurvivalExpert())
            Utils.walkToSurvivalExpert(abc);
        if(!(Inventory.getCount("Bronze axe") == 0)) {//we have no axe in our inventory
            if (Inventory.getAll().length >= 26)//our inventory is too full, drop some shit
                BInventory.dropAllExceptOne(dontDrop);
            Utils.talkTo("Survival Expert");
        }
        else{//we already have an axe in our inventory
            if(Inventory.getAll().length == 28)//our inventory is too full to hold logs
                BInventory.dropAllExceptOne(dontDrop);
        }

        //~~~~~~~~~Safety checks complete, continue script normally~~~~~~~~~

        RSObject[] temp = Objects.findNearest(5, "Tree");
        if(temp.length < 1)
            return;
        RSObject tree = temp[0];//get a nearby normal tree
        while(!DynamicClicking.clickRSObject(tree, "Chop down"))//try to chop down the tree, if we fail...
            Camera.turnToTile(tree);//rotate the camera to face the tree
        Timing.waitCondition(new BooleanSupplier() {//while chopping, wait until we have logs in our inventory
            @Override
            public boolean getAsBoolean() {
                General.sleep(200);//sleep to save CPU
                return Inventory.getCount("Logs") > 0;//break waiting when we have logs
            }
        }, General.random(6000, 7000));//timeout after 6-7 seconds
    }

    private void firemaking(){
        if (Inventory.getCount("Logs") == 0){//we don't have logs in our inventory, chop them
            woodcutting();//woodcutting() checks if our inventory is full so we don't have to here
            General.sleep(800, 1200);
        }
        if (Inventory.getCount("Tinderbox") == 0) {//we don't have a tinderbox in our inventory
            if(Inventory.getAll().length == 28) {// if our inventory is full, we can't accept the tinderbox
                BInventory.dropAllExceptOne(dontDrop);
                General.sleep(800, 1200);
            }
            if(!Utils.isNearSurvivalExpert())
                Utils.walkToSurvivalExpert(abc);
            Utils.talkTo("Survival Expert");
        }

        //~~~~~~~~~~~~~~~~Safety checks complete~~~~~~~~~~~~~~~~

        RSItem[] tinderbox = Inventory.find("Tinderbox");
        if(tinderbox.length < 1)
            return;
        RSItem[] logs = Inventory.find("Logs");
        if(logs.length < 1)
            return;

        if (Firemaking.standingOnFire()) {//if we're already standing on a fire use my AMAZING algorethumb to find an open tile
            Utils.walkToSurvivalExpert(abc);
            int X = Player.getPosition().getX();
            int Y = Player.getPosition().getY();
            for(int i = 0; i < 10 && Firemaking.standingOnFire(); i++) {//loop 10 times and while we're on a fire
                Walking.clickTileMS(new RSTile(X, ++Y), 1);//walk 1 tile north
                General.sleep(800, 1000);
            }
        }
        tinderbox[0].click("Use");
        logs[0].click("Use Tinderbox -> Logs");
        General.sleep(200);
        while(Player.getAnimation() != 733)//loop sleep until we start the firelighting animation
            General.sleep(100);
        Timing.waitCondition(new BooleanSupplier() {//sleep until we finish the firelighting animation
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return (Player.getAnimation() != 733);
            }
        }, General.random(8000, 9000));
    }

    private void cooking(){
        if(!Utils.isNearSurvivalExpert())
            Utils.walkToSurvivalExpert(abc);
        if(Inventory.getCount("Raw shrimps") == 0){//we need shrimps to cook fam
            fishing();
            General.sleep(800, 1200);
        }
        RSObject[] fires = Objects.findNearest(10, "Fire");
        if(fires.length < 1){//there are no nearby fires, make one
            firemaking();
            return;
        }

        //~~~~~~~~~~~~~~~~~~~~~~Safety checks complete~~~~~~~~~~~~~~~~~~~~~~~

        fires[0].click("Walk here");
        General.sleep(200, 500);
        RSItem[] shrimp = Inventory.find("Raw shrimps");
        if(shrimp.length < 1)
            return;
        if(abc.shouldHover()){
            shrimp[0].click("Use");
            Clicking.hover(fires[0]);
        }
        Timing.waitCondition(new BooleanSupplier() {//sleep until we are near the fire
            @Override
            public boolean getAsBoolean() {
                return Player.getPosition().distanceTo(fires[0]) <= 1;
            }
        }, General.random(8000, 10000));
        General.sleep(300, 600);
        DynamicClicking.clickRSObject(fires[0], "Use Raw shrimps -> Fire");
        Clicking.click(NPCChat.getClickContinueInterface());
    }

    private void movingOn(){
        RSTile nearGate = new RSTile(3091, 3092);//the tile next to the exit gate
        //we're not close to the survival expert or next to the gate
        if(!Utils.isNearSurvivalExpert() && !(Player.getPosition().distanceTo(nearGate) <= 1))
            Utils.walkToSurvivalExpert(abc);

        if(!(Player.getPosition().distanceTo(nearGate) <= 1)){//only walk to the gate if we're not there already
            Walking.walkTo(nearGate);//infront of gate in survival area
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Player.getPosition().distanceTo(nearGate) <= 1;//infront of gate in survival area
                }
            }, General.random(12000, 18000));
        }

        RSObject[] gate = Objects.findNearest(5, "Gate");
        if(gate.length < 1)//give up on life
            return;
        Camera.turnToTile(gate[0]);
        gate[0].click("Open");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Utils.inFrontOfCooksHouse();
            }
        }, General.random(3000, 4000));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Node framework~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void execute(){
        //the interface for tutorial island chatbox instructions
        if(Interfaces.get(263,1,0).getText().equals(movingAround))
            movingAround();
        else if(Interfaces.get(263, 1, 0).getText().equals(openBackpack))
            openBackpack();
        else if(Interfaces.get(263, 1, 0).getText().equals(fishing))
            fishing();
        else if(Interfaces.get(263, 1, 0).getText().equals(openStats))
            openStats();
        else if(Interfaces.get(263, 1, 0).getText().equals(skillsAndExp))
            skillsAndExp();
        else if(Interfaces.get(263, 1, 0).getText().equals(woodcutting))
            woodcutting();
        else if(Interfaces.get(263, 1, 0).getText().equals(firemaking))
            firemaking();
        else if(Interfaces.get(263, 1, 0).getText().equals(cooking))
            cooking();
        else if(Interfaces.get(263, 1, 0).getText().equals(movingOn))
            movingOn();
    }

    @Override
    public boolean validate(){
        if(Game.getSetting(406) == 2 || Game.getSetting(406) == 3)//the setting for tutorial island progress
            return true;
        else
            return false;
    }
}
