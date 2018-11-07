package scripts.sexyTutorial;

import org.jetbrains.annotations.NotNull;
import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import scripts.API.BInventory;
import scripts.API.Firemaking;

import java.util.function.BooleanSupplier;

import static org.tribot.api2007.Inventory.find;

public class sexyTutorialDeprecated extends Script{

    private boolean inSurvivalArea(){
        return PathFinding.canReach(new RSTile(3090, 3092, 0), false);
    }

    private boolean inFrontOfCooksHouse(){
        return PathFinding.canReach(new RSTile(3089, 3092, 0), false);
    }

    private boolean inCooksHouse(){
        return PathFinding.canReach(new RSTile(3078, 3084, 0), false);
    }

    private boolean inGuideHouse(){
        return PathFinding.canReach(new RSTile(3096, 3107, 0), false);
    }

    private boolean behindCooksHouse(){
        return PathFinding.canReach(new RSTile(3071, 3090, 0), false);
    }

    private boolean inQuestGuideHouse(){
        return PathFinding.canReach(new RSTile(3086, 3125, 0), false);
    }

    private boolean behindChurch(){
        return PathFinding.canReach(new RSTile(3122, 3102, 0), false);
    }

    private ABCUtil abc = new ABCUtil();

    private void flush(){
        if(!Interfaces.get(162,45).isHidden() && Interfaces.get(162,45).getText().equals("Click to continue")){//flush annoying messages
            Clicking.click(Interfaces.get(162,45));
        }
    }//this function gets rid of annoying game notifications that take up the chat box on Tut Isle

    private void talkTo(@NotNull RSNPC npc){//I honestly have no idea what @NotNull does, it was recommended by my IDE LOL
        flush();
        Camera.turnToTile(npc.getPosition());
        if(npc.isOnScreen())
            DynamicClicking.clickRSNPC(npc, "Talk-to");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active(){
                General.sleep(100);
                return NPCChat.getMessage() != null;
            }}, General.random(8000, 12000));
        while (NPCChat.getClickContinueInterface() != null){
            NPCChat.getClickContinueInterface().click("Continue");
            General.sleep(150, 1500);
        }
    }//talks to an NPC and presses continue until additional input comes up or convo ends

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

    private boolean onIsland(){
        return Interfaces.get(263, 1, 0) != null;//This is the interface of the instruction text
    }

    private void createCharacter(){
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
        General.sleep(500,1500);
    }

    private void gielinorGuideIntro(){
        RSNPC[] temp = NPCs.findNearest("Gielinor Guide");
        if(temp.length < 1)
            return;//failed to find the Gielinor guide
        RSNPC guide = temp[0];
        talkTo(guide);

        Clicking.click(Interfaces.get(219,1,2));
        General.sleep(300,550);
        Clicking.click(NPCChat.getClickContinueInterface());
    }

    private void openOptionsTab(){

    }

    private void gielinorGuideOutro() {
        RSNPC[] temp = NPCs.findNearest("Gielinor Guide");
        if (temp.length < 1)
            return;
        RSNPC guide = temp[0];
        talkTo(guide);

        General.sleep(100);
        while (!Options.isRunEnabled()) {
            General.sleep(10, 100);
            Options.setRunEnabled(true);
        }
        talkTo(guide);
    }

    private void walkToSurvivalInstructor(){
        while (inGuideHouse())//we are inside of the guide house
        {
            Walking.walkTo(new RSTile(3096, 3107,0));
            RSObject[] exit = Objects.findNearest(15,"Door");
            if(exit.length < 1)
                return;
            Camera.turnToTile(exit[0]);
            //General.sleep(400, 600);
            exit[0].click("Open");
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getPosition().distanceTo(new RSTile(3098, 3107,0)) == 0;
                }
            }, General.random(6000,7000));
        }


        RSTile nearInstructor = new RSTile(3102,3095);
        if(!Walking.walkTo(nearInstructor))//We are too far to walk to the instructor in 1 step (WebWalking is broken in this loc)
        {
            Walking.walkTo(new RSTile(3099, 3107));
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return (Player.getPosition().distanceTo(new RSTile(3099, 3107)) <= 1);
                }
            }, General.random(7000, 15000));
            Walking.walkTo(nearInstructor);
            General.sleep(150, 300);
            if (abc.shouldExamineEntity()) {
                abc.examineEntity();
                General.sleep(1000, 1700);
                flush();
            }
        }
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return (Player.getPosition().distanceTo(nearInstructor) <= 1);
            }
        }, General.random(7000, 15000));
    }

    private void talkToSurvivalInstructor(boolean recover){
        if(recover)
            walkToSurvivalInstructor();
        RSNPC[] temp = NPCs.findNearest("Survival Expert");
        if (temp.length < 1)
            return;
        RSNPC instructor = temp[0];
        talkTo(instructor);
    }

    private void openBackpack(){
        GameTab.open(GameTab.TABS.INVENTORY);
        //General.sleep(250, 350);
    }

    private void chopTree(boolean recover){
        if(recover)
            walkToSurvivalInstructor();
        RSObject[] temp = Objects.findNearest(3, "Tree");
        if(temp.length < 1)
            return;
        RSObject tree = temp[0];
        if(!tree.isOnScreen())
            Camera.turnToTile(tree.getPosition());
        if(!tree.isClickable())
            return;
        tree.click("Chop down");
        General.sleep(250, 400);
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Inventory.getCount("Logs") > 0;
            }
        }, General.random(20000, 25000));
    }

    private void lightFire(boolean recover){
        if(recover){
            walkToSurvivalInstructor();
            if(Inventory.getCount("Logs") == 0){//we don't have logs for some reason
                chopTree(false);
                return;
            }

        }
        RSItem[] tinderbox = Inventory.find("Tinderbox");
        if(tinderbox.length < 1){
            talkToSurvivalInstructor(true);
            return;
        }
        RSItem[] logs = Inventory.find("Logs");
        if(logs.length < 1){
            chopTree(true);
            return;
        }
        if (Firemaking.standingOnFire()){
            walkToSurvivalInstructor();
            int X = Player.getPosition().getX();
            int Y = Player.getPosition().getY();
            while (Firemaking.standingOnFire()){//walk around in a very predictable way until you can light a fire
                Walking.clickTileMS(new RSTile(X, ++Y), 1);
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(200);
                        return !Firemaking.standingOnFire();
                    }
                }, General.random(7000, 12000));
            }
        }
        tinderbox[0].click("Use");
        logs[0].click("Use Tinderbox -> Logs");
        General.sleep(200);
        while(Player.getAnimation() != 733)
            General.sleep(100);
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return (Player.getAnimation() != 733);
            }
        }, General.random(6000, 9000));
        General.sleep(800, 1400);
    }

    private void cookShrimp(){
        RSObject[] fires = Objects.findNearest(5, "Fire");
        if(fires.length < 1)
            return;
        RSItem[] shrimp = Inventory.find("Raw shrimps");
        if (shrimp.length < 1)
            return;
        shrimp[0].click("Use");
        fires[0].click("Use Raw shrimps -> Fire");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Inventory.getCount("Shrimps") > 0;
            }
        }, General.random(5000, 7000));
    }

    private void talkToCook(){
        RSNPC[] cook = NPCs.findNearest("Master Chef");
        if(cook.length < 1)
            return;
        talkTo(cook[0]);
    }

    private void makeDough(){
        RSItem[] flour = Inventory.find(2516);
        RSItem[] water = Inventory.find(1929);
        if(flour.length < 1 || water.length < 1)
            return;
        flour[0].click("Use");
        Clicking.click(water[0]);
        General.sleep(400, 900);
    }

    private void cookDough(){
        RSObject[] range = Objects.findNearest(10, 9736);
        if (range.length < 1)
            return;
        range[0].click("Cook");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                return Inventory.getCount("Bread") > 0;
            }
        }, General.random(5000, 6000));
    }

    private void talkToQuestGuide(){
        Camera.turnToTile(new RSTile(3086, 3122));
        RSNPC[] guide = NPCs.findNearest("Quest Guide");
        if(guide.length < 1)
            return;
        talkTo(guide[0]);
    }//this is sloppy AF and if it gets stuck here we're fucked

    private void openQuestJournal(){
        GameTab.open(GameTab.TABS.QUESTS);
        General.sleep(400, 700);
    }

    private void walkToQuestGuide(){
        RSObject[] door = Objects.findNearest(10, 9710);
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        General.sleep(500, 700);
        door[0].click("Open");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return behindCooksHouse();
            }
        }, General.random(4000,5000));
        Walking.walkTo(new RSTile(3071, 3106));
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3071, 3106)) <= 1;
            }
        }, General.random(7000,9000));

        Walking.walkTo(new RSTile(3072, 3119));
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3072, 3119)) <= 1;
            }
        }, General.random(7000,9000));

        Walking.walkTo(new RSTile(3086, 3126));
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3086, 3126)) <= 1;
            }
        }, General.random(7000,9000));
        General.sleep(300, 600);
        RSObject[] Qdoor = Objects.findNearest(5, "Door");
        if(Qdoor.length < 1)
            return;
        Qdoor[0].click("Open");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return inQuestGuideHouse();
            }
        }, General.random(1500, 3000));
    }

    private void fish(){
        if(!PathFinding.canReach(new RSTile(3101, 3093,0), false))
            walkToSurvivalInstructor();
        RSNPC[] fish = NPCs.findNearest("Fishing spot");
        if(fish.length < 1){
            walkToSurvivalInstructor();
        }
        while(!fish[0].click("Net")){
            if(!fish[0].isOnScreen()){
                Camera.turnToTile(fish[0]);
                General.sleep(600, 800);
            }
            walkToSurvivalInstructor();
        }
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Inventory.getCount("Raw shrimps") > 0;
            }
        }, General.random(12000, 20000));
    }

    private void openStats(){
        GameTab.open(GameTab.TABS.STATS);
    }

    private void walkToCook(boolean recover){
        if(recover){
            if(inSurvivalArea() || inGuideHouse()){
                walkToSurvivalInstructor();
                Walking.walkTo(new RSTile(3091, 3092, 0));//infront of gate in survival area
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Player.getPosition().distanceTo(new RSTile(3091, 3092)) <= 1;//infront of gate in survival area
                    }
                }, General.random(12000, 18000));
                RSObject[] gate = Objects.findNearest(5, "Gate");
                if(gate.length < 1)//give up on life
                    return;
                Camera.turnToTile(gate[0]);
                gate[0].click("Open");
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return inFrontOfCooksHouse();
                    }
                }, General.random(2000, 4000));

            }
            else if(inFrontOfCooksHouse()) {
                Walking.walkTo(new RSTile(3076, 3071, 0));//central area infront of cooks house
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Player.getPosition().distanceTo(new RSTile(3076, 3071)) <= 1;
                    }
                }, General.random(5000, 8000));
            }
        }
        else{//we are not recovering
            Walking.walkTo(new RSTile(3091, 3092, 0));//infront of gate in survival area
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getPosition().distanceTo(new RSTile(3091, 3092)) <= 1;//infront of gate in survival area
                }
            }, General.random(7000, 12000));
            RSObject[] gate = Objects.findNearest(5, "Gate");
            if(gate.length < 1)//give up on life
                return;
            Camera.turnToTile(gate[0]);
            gate[0].click("Open");
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return inFrontOfCooksHouse();
                }
            }, General.random(2000, 4000));
        }
        Walking.walkTo(new RSTile(3080, 3084, 0));//infront of cooks door
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3080, 3084)) <= 1;
            }
        }, General.random(6000, 8000));
        RSObject[] door = Objects.findNearest(5, "Door");
        if(door.length < 1)//kill yourself now
            return;
        Camera.turnToTile(door[0]);
        door[0].click("Open");
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return inCooksHouse();
            }
        }, General.random(4000, 6000));
    }

    private void walkToSmith(boolean recover){
        if(recover){

        }
        else{
            RSObject[] ladder = Objects.findNearest(10, "Ladder");
            if(ladder.length < 1)
                return;
            ladder[0].click("Climb-down");
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return PathFinding.canReach(new RSTile(3088, 9520), false);
                }
            }, General.random(6000, 8000));

            Walking.walkTo(new RSTile(3081, 9507));
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return Player.getPosition().distanceTo(new RSTile(3081, 9507)) <= 1;
                }
            }, General.random(8000, 12000));
        }
    }

    private void talkToSmith(){
        Camera.turnToTile(new RSTile(3080, 9504));
        RSNPC[] miner = NPCs.findNearest("MiningInstructor Instructor");
        if(miner.length < 1)
            return;
        talkTo(miner[0]);
    }

    private void mine(){
        if(Inventory.getCount("Tin ore") == 0){//mine some tin
            RSObject[] tinRock = Objects.findNearest(10, 10080);
            if(tinRock.length < 1)
                return;
            tinRock[0].click("Mine");
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Inventory.getCount("Tin ore") > 0;
                }
            }, General.random(9000, 12000));
        }
        if(Inventory.getCount("Copper ore") == 0){
            RSObject[] copperRock = Objects.findNearest(10, 10079);
            if(copperRock.length < 1)
                return;
            copperRock[0].click("Mine");
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Inventory.getCount("Copper ore") > 0;
                }
            }, General.random(9000, 12000));
        }
    }

    private void smelt(){
        RSObject[] furnace = Objects.findNearest(15, "Furnace");
        if(furnace.length < 1)
            return;
        Camera.turnToTile(furnace[0]);
        furnace[0].click("Use");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Inventory.getCount("Bronze bar") == 1;
            }
        }, General.random(12000, 15000));
    }

    private void smith(){
        RSObject[] anvil = Objects.findNearest(10, "Anvil");
        if(anvil.length < 1)
            return;
        if(!anvil[0].isOnScreen())
            Camera.turnToTile(anvil[0]);
        anvil[0].click("Smith");
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

    private void walkToCombatInstructor(){
        WebWalking.walkTo(new RSTile(3106, 9506));
    }

    private void talkToCombatInstructor(){
        RSNPC[] combatMan = NPCs.findNearest("Combat Instructor");
        if(combatMan.length < 1)
            return;
        talkTo(combatMan[0]);
    }

    private void equipmentStats(){
        GameTab.open(GameTab.TABS.EQUIPMENT);
        General.sleep(500, 1000);
        RSInterfaceChild equipStats = Interfaces.get(387, 17);
        if(equipStats == null)
            return;
        equipStats.click("View equipment stats");
        General.sleep(900, 1500);
        RSItem[] dagger = Inventory.find("Bronze dagger");
        if (dagger.length < 1)
            return;
        dagger[0].click("Equip");
        General.sleep(500, 1000);
        Interfaces.closeAll();
        General.sleep(1000, 1590);
    }

    private void equipSwordAndShield(){
        GameTab.open(GameTab.TABS.INVENTORY);
        RSItem[] sword = Inventory.find("Bronze sword");
        RSItem[] shield = Inventory.find("Wooden shield");
        if(sword.length < 1 || shield.length < 1)
            return;
        sword[0].click("Wield");
        General.sleep(500, 790);
        shield[0].click("Wield");
        General.sleep(700, 1200);
        GameTab.open(GameTab.TABS.COMBAT);
        General.sleep(1000, 1500);
    }

    private void walkToRatCage(){
        WebWalking.walkTo(new RSTile(3108, 9518));
    }

    private void murderRatMelle(){
        RSNPC[] rat = NPCs.findNearest("Giant rat");
        if(rat.length < 1)
            return;
        RSNPC ratToMurder = null;
        for(RSNPC r : rat){
            if(!r.isInCombat()) {//the rat is not in combat
                if(!r.isOnScreen())
                    Camera.turnToTile(r);
                r.click("Attack");//try to attack the rat
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
        if(abc.shouldCheckXP())
            abc.checkXP();
        while(ratToMurder.getAnimation() != 4935 && Timing.timeFromMark(startTime) < 20000){//wait for rat death animation or timeout after 20 seconds
            General.sleep(300);
        }
    }

    private void murderRatRanged(){
        RSItem[] shortbow = Inventory.find("Shortbow");
        RSItem[] arrows = Inventory.find("Bronze arrow");
        if(shortbow.length < 1)
            return;
        if(arrows.length == 1)
            arrows[0].click("Wield");
        shortbow[0].click("Wield");
        General.sleep(400, 600);
        RSNPC[] rat = NPCs.findNearest("Giant rat");
        if(rat.length < 1)
            return;
        RSNPC ratToMurder = null;
        for(RSNPC r : rat){
            if(!r.isInCombat()) {//the rat is not in combat
                if(!r.isOnScreen())
                    Camera.turnToTile(r);
                r.click("Attack");//try to attack the rat
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

    private void walkToBanker(){

        WebWalking.walkTo(new RSTile(3111, 9525));
        RSObject[] ladder = Objects.findNearest(5, "Ladder");
        if(ladder.length < 1)
            return;
        General.sleep(200, 400);
        ladder[0].click("Climb-up");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return PathFinding.canReach(new RSTile(3110, 3125), false);
            }
        }, General.random(3000, 4000));
        General.sleep(100);
        RSObject[] door = Objects.findNearest(20, "Large door");
        if(door.length < 1)
            return;
        String[] options = door[0].getDefinition().getActions();
        for(String option : options){
            if(option.equals("Open")){
                Walking.walkTo(new RSTile(3122, 3118));
                Timing.waitCondition(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() {
                        General.sleep(100);
                        return Player.getPosition().distanceTo(new RSTile(3122, 3118)) <= 1;
                    }
                }, General.random(8000, 12000));
                door[0].click("Open");
                General.sleep(400, 700);
            }
        }

        Walking.walkTo(new RSTile(3121, 3123));
        General.sleep(700, 1400);
        if(abc.shouldExamineEntity())
            abc.examineEntity();
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3121, 3123)) <= 1;
            }
        }, General.random(10000, 15000));
    }//THIS FUNCTION NEEDS TO START IN THE CAVE

    private void openBank(){
        if(abc.generateOpenBankPreference() == OpenBankPreference.BANKER){//open bank with the banker
            RSNPC[] banker = NPCs.findNearest("Banker");
            if(banker.length < 1)
                return;
            banker[0].click("Talk-to");
        }
        else{//open bank with the booth
            RSObject[] booth = Objects.findNearest(10, "Bank booth");
            if(booth.length < 1)
                return;
            booth[0].click("Use");
        }
        General.sleep(1200, 2000);
        Interfaces.closeAll();
    }

    private void usePollBooth(){
        flush();
        RSObject[] poll = Objects.findNearest(10, "Poll booth");
        if(poll.length < 1)
            return;
        Camera.turnToTile(poll[0]);
        poll[0].click("Use");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return NPCChat.getClickContinueInterface() != null;
            }
        }, General.random(8000, 15000));

        while (NPCChat.getClickContinueInterface() != null) {
            NPCChat.getClickContinueInterface().click("Continue");
            General.sleep(330, 1500);
        }
        General.sleep(1000);
        Interfaces.get(310,2,11).click("Close");//Interfaces.closeall() doesn't work here for some reason
    }

    private void walkToAccountGuide(){
        RSObject[] door = Objects.findNearest(10, 9721);
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        door[0].click("Open");
    }

    private void talkToAccountGuide(){
        flush();
        RSNPC[] guide = NPCs.findNearest("Account Guide");
        if(guide.length < 1)
            return;
        talkTo(guide[0]);
        General.sleep(800, 1000);
        GameTab.open(GameTab.TABS.ACCOUNT);
        talkTo(guide[0]);
    }

    private void leaveAccountRoom(){
        RSObject[] door = Objects.findNearest(5, 9722);
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        door[0].click("Open");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3130, 3124)) == 0;
            }
        }, General.random(5000, 6000));
    }

    private void walkToPriest(){
        Camera.setCameraRotation(General.random(112, 140));
        Camera.setCameraAngle(General.random(65, 80));
        Walking.clickTileMS(new RSTile(3126, 3107), "Walk here");
    }

    private void talkToPriest(){
        RSNPC[] priest = NPCs.findNearest("Brother Brace");
        if(priest.length < 1)
            return;
        talkTo(priest[0]);
        GameTab.open(GameTab.TABS.PRAYERS);
        General.sleep(1000, 2000);
        talkTo(priest[0]);
        GameTab.open(GameTab.TABS.FRIENDS);
        General.sleep(1000, 2000);
        talkTo(priest[0]);
    }

    private void walkToWizard(){
        RSObject[] door = Objects.findNearest(10, "Door");
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        door[0].click("Open");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return behindChurch();
            }
        }, General.random(8000, 10000));

        Walking.walkTo(new RSTile(3126, 3087));
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3126, 3087)) <= 1;
            }
        }, General.random(10000, 12000));

        Walking.walkTo(new RSTile(3139, 3087));
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return Player.getPosition().distanceTo(new RSTile(3139, 3087)) <= 1;
            }
        }, General.random(10000, 12000));

    }

    private void talkToWizard(){
        RSNPC[] wiz = NPCs.findNearest("Magic Instructor");
        if(wiz.length < 1)
            return;
        talkTo(wiz[0]);
        GameTab.open(GameTab.TABS.MAGIC);
        General.sleep(1000, 3000);
        talkTo(wiz[0]);
        RSNPC[] chicken = NPCs.findNearest("Chicken");
        if(chicken.length < 1)
            return;
        Magic.selectSpell("Wind Strike");
        chicken[0].click("Cast Wind Strike -> Chicken");
        talkTo(wiz[0]);
    }

    private void goToMainland(){

    }

    @Override
    public void run(){
        openBank();
    }
}
