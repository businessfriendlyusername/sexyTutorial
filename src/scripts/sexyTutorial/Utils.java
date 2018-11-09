package scripts.sexyTutorial;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import java.util.function.BooleanSupplier;

public class Utils {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Location checks~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static boolean inSurvivalArea(){
        return PathFinding.canReach(new RSTile(3090, 3092, 0), false);
    }

    public static boolean isNearSurvivalExpert(){
        if(Player.getPosition().distanceTo(new RSTile(3103, 3096)) <= 5)
            return true;
        else
            return false;
    }

    public static boolean inFrontOfCooksHouse(){
        return PathFinding.canReach(new RSTile(3089, 3092, 0), false);
    }

    public static boolean inCooksHouse(){
        return PathFinding.canReach(new RSTile(3078, 3084, 0), false);
    }

    public static boolean inGuideHouse(){
        return PathFinding.canReach(new RSTile(3096, 3107, 0), false);
    }

    public static boolean behindCooksHouse(){
        return PathFinding.canReach(new RSTile(3071, 3090, 0), false);
    }

    public static boolean inQuestGuideHouse(){
        return PathFinding.canReach(new RSTile(3086, 3125, 0), false);
    }

    public static boolean behindChurch(){
        return PathFinding.canReach(new RSTile(3122, 3102, 0), false);
    }

    public static boolean inMiningArea(){
        return PathFinding.canReach(new RSTile(3088, 9520, 0), false);
    }

    public static boolean inCombatArea(){
        return PathFinding.canReach(new RSTile(3105, 9508, 0), false);
    }

    public static boolean inRatCage(){
        return PathFinding.canReach(new RSTile(3108, 9518, 0), false);
    }

    public static boolean isInBank(){
        return (Player.getPosition().getY() > 3118 && Player.getPosition().getY() < 3126 &&
                Player.getPosition().getX() < 3125 && Player.getPosition().getX() > 3117);//gross
    }

    public static boolean isOutsideBank(){
        return PathFinding.canReach(new RSTile(3121, 3118), false) && !isInBank();
    }

    public static boolean inAccountRoom(){
        return PathFinding.canReach(new RSTile(3125, 3124), false);
    }

    public static boolean isBehindBank(){
        return PathFinding.canReach(new RSTile(3130, 3124), false);
    }

    public static boolean isInChurch(){
        int x = Player.getPosition().getX();
        int y = Player.getPosition().getY();
        return (x < 3129 && x > 3119 && y < 3111 && y > 3102);
    }

    public static boolean isNearWizard(){
        return Player.getPosition().distanceTo(new RSTile(3141, 3088)) < 7;
    }

    public static boolean isNearMiningInstructor(){
        return Player.getPosition().distanceTo(new RSTile(3080, 9505)) < 8;
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Navigation~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void walkToSurvivalExpert(ABCUtil abc){
        while (Utils.inGuideHouse())//we are inside of the guide house
        {
            Walking.walkTo(new RSTile(3096, 3107,0));
            RSObject[] exit = Objects.findNearest(15,"Door");
            if(exit.length < 1)
                return;
            Camera.turnToTile(exit[0]);
            //General.sleep(400, 600);
            exit[0].click("Open");
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Player.getPosition().distanceTo(new RSTile(3098, 3107,0)) == 0;
                }
            }, General.random(6000,7000));
        }


        RSTile nearInstructor = new RSTile(3102,3095);
        if(!Walking.walkTo(nearInstructor))//We are too far to walk to the instructor in 1 step (WebWalking is broken in this loc)
        {
            Walking.walkTo(new RSTile(3099, 3107));
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return (Player.getPosition().distanceTo(new RSTile(3099, 3107)) <= 1);
                }
            }, General.random(7000, 15000));
            Walking.walkTo(nearInstructor);
            General.sleep(150, 300);
            if (abc.shouldExamineEntity()) {
                abc.examineEntity();
                General.sleep(1000, 1700);
                Utils.flush();
            }
        }
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return (Player.getPosition().distanceTo(nearInstructor) <= 1);
            }
        }, General.random(7000, 15000));
    }

    public static void walkToCook(ABCUtil abc){
        if(inCooksHouse())//we are already in the cooks house
            return;
        if(inSurvivalArea() || inGuideHouse()){//We're still in the starting area, walk through the gate to the cook
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

        if(inFrontOfCooksHouse()){
            if(!Walking.walkTo(new RSTile(3080, 3084, 0))) {//we can't walk in front of the cooks door
                Walking.walkTo(new RSTile(3076, 3071, 0));//central area in front of cooks house
                Timing.waitCondition(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() {
                        General.sleep(100);
                        return Player.getPosition().distanceTo(new RSTile(3076, 3071)) <= 2;
                    }
                }, General.random(6000, 8000));

                Walking.walkTo(new RSTile(3080, 3084, 0));//try to walk in front of the cooks door again
            }
            Timing.waitCondition(new BooleanSupplier() {//wait until we are in front of the cooks door
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Player.getPosition().distanceTo(new RSTile(3080, 3084)) <= 1;//central area in front of cooks hosue
                }
            }, General.random(6000, 8000));

            RSObject[] door = Objects.findNearest(5, "Door");//the door to the cooks house
            if(door.length < 1)//kill yourself now
                return;

            if(!door[0].click("Open")) {
                Camera.turnToTile(door[0]);
                door[0].click("Open");
            }
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    General.sleep(100);
                    return Utils.inCooksHouse();//wait until we are in front of the cooks house
                }
            }, General.random(4000, 6000));
        }
        General.sleep(400, 600);
    }

    //please for the love of god fix webwalking on tutorial isle...
    public static void walkToQuestGuide(ABCUtil abc) {
        if(inQuestGuideHouse())
            return;
        if(!behindCooksHouse()){
            walkToCook(abc);
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
        RSTile step1 = new RSTile(3069, 3099);
        RSTile step2 = new RSTile(3069, 3113);
        RSTile step3 = new RSTile(3079, 3126);
        for (int i = 0; i < 3 && Player.getPosition().distanceTo(step3) > 1; i++) {//loop while we are too far from step3, or 3 times
            if (!Walking.walkTo(step3)) {//try to walk to step3...
                if (!Walking.walkTo(step2)) {//try to walk to step2...
                    Walking.walkTo(step1);//walk to step1...
                    Timing.waitCondition(new BooleanSupplier() {
                        @Override
                        public boolean getAsBoolean() {
                            General.sleep(100);
                            return Player.getPosition().distanceTo(step1) <= 1;
                        }
                    }, General.random(8000, 12000));
                }
                else{//we were able to walk to step 2, now wait until we get there
                    Timing.waitCondition(new BooleanSupplier() {
                        @Override
                        public boolean getAsBoolean() {
                            General.sleep(100);
                            return Player.getPosition().distanceTo(step2) <= 1;
                        }
                    }, General.random(8000, 12000));
                }
            }
            else{//we were able to walk to step 3, now wait until we get there
                Timing.waitCondition(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() {
                        General.sleep(100);
                        return Player.getPosition().distanceTo(step3) <= 1;
                    }
                }, General.random(8000, 12000));
            }
        }
        RSObject[] Qdoor = Objects.findNearest(10, "Door");
        if(Qdoor.length < 1)
            return;
        if(!DynamicClicking.clickRSObject(Qdoor[0], "Open")){
            Camera.turnToTile(Qdoor[0]);
            DynamicClicking.clickRSObject(Qdoor[0], "Open");
        }
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return inQuestGuideHouse();
            }
        }, General.random(8000, 10000));
        //well that was a complete fucking nightmare...
    }

    public static void walkToMiningInstructor(ABCUtil abc){
        if(isNearMiningInstructor())
            return;
        if(!inMiningArea()) {
            walkToQuestGuide(abc);
            RSObject[] ladder = Objects.findNearest(10, "Ladder");
            if(ladder.length < 1)
                return;
            while(!ladder[0].click("Climb-down"))
                Camera.turnToTile(ladder[0]);
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return Utils.inMiningArea();
                }
            }, General.random(8000, 10000));
        }
        WebWalking.walkTo(new RSTile(3080, 9505));
    }

    public static void walkToCombatInstructor(ABCUtil abc){
        if(inCombatArea())
            return;
        else if(inRatCage())
            WebWalking.walkTo(new RSTile(3105, 9508));
        else {
            walkToMiningInstructor(abc);
            WebWalking.walkTo(new RSTile(3105, 9508));
        }

    }

    public static void walkToRatCage(ABCUtil abc){
        if(!inRatCage()){
            walkToCombatInstructor(abc);
            WebWalking.walkTo(new RSTile(3109, 9518));
        }
    }

    public static void walkToOutsideBank(ABCUtil abc){
        if(isOutsideBank())
            return;
        walkToCombatInstructor(abc);
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
                return isOutsideBank();
            }
        }, General.random(3000, 4000));
    }

    public static void walkToBank(ABCUtil abc){
        if(isInBank())
            return;
        walkToOutsideBank(abc);
        RSObject[] door = Objects.findNearest(20, "Large door");//the door infront of the bank can be closed, this is a problem
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
    }

    public static void walkToAccountGuide(ABCUtil abc){
        if(inAccountRoom())
            return;
        walkToBank(abc);
        RSObject[] door = Objects.findNearest(10, 9721);
        if(door.length < 1)
            return;
        Camera.turnToTile(door[0]);
        door[0].click("Open");
        Timing.waitCondition(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                General.sleep(100);
                return inAccountRoom();
            }
        }, General.random(7000, 10000));
    }

    public static void walkToPriest(ABCUtil abc){
        if(isInChurch())
            return;
        if(!isBehindBank()){
            walkToAccountGuide(abc);
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

        if(!Walking.walkTo(new RSTile(3126, 3107))){
            Walking.walkTo(new RSTile(3135, 3118));
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return Player.getPosition().distanceTo(new RSTile(3135, 3118)) <= 1;
                }
            }, General.random(8000, 12000));
            Walking.walkTo(new RSTile(3126, 3107));
            Timing.waitCondition(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return isInChurch();
                }
            }, General.random(8000, 12000));
        }
    }

    public static void walkToWizard(ABCUtil abc){
        if(isNearWizard())
            return;
        if(!behindChurch()){
            walkToPriest(abc);
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
        }

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
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Other~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void flush(){
        if(!Interfaces.get(162,45).isHidden() && Interfaces.get(162,45).getText().equals("Click to continue")){//flush annoying messages
            Clicking.click(Interfaces.get(162,45));
        }
    }//this function gets rid of annoying game notifications that take up the chat box on Tut Isle

    public static boolean talkTo(String npcName){//I honestly have no idea what @NotNull does, it was recommended by my IDE LOL
        flush();
        RSNPC[] npcs = NPCs.findNearest(npcName);
        if(npcs.length < 1)
            return false;
        RSNPC npc = npcs[0];
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
        return true;
    }//talks to an NPC and presses continue until additional input comes up or convo ends
}
