package scripts;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import static org.powerbot.script.Condition.sleep;

@Script.Manifest(name = "Mine and Bank (Copper Tin)", properties = "author=BMoney; topic=1296203; client=4;", description = "Mines copper and tin in lumbridge and banks it" )
public class MineAndBank extends PollingScript<ClientContext> {

    Tile stairs_tile = new Tile(3205, 3209); //first floor
    Tile upstairs_tile = new Tile(3205, 3209); //stairs near bank
    Tile bank_tile = new Tile(3209, 3220); //bank location
    Tile mine_tile = new Tile(3227, 3147); //mining location
    public static final Tile[] arrayPathToMine = {new Tile(3206, 3208, 0), new Tile(3211, 3209, 0), new Tile(3215, 3212, 0), new Tile(3215, 3217, 0), new Tile(3220, 3219, 0), new Tile(3221, 3219, 0), new Tile(3226, 3219, 0), new Tile(3231, 3219, 0), new Tile(3234, 3215, 0), new Tile(3234, 3210, 0), new Tile(3234, 3205, 0), new Tile(3238, 3202, 0), new Tile(3242, 3199, 0), new Tile(3244, 3194, 0), new Tile(3244, 3189, 0), new Tile(3244, 3184, 0), new Tile(3243, 3179, 0), new Tile(3243, 3174, 0), new Tile(3239, 3170, 0), new Tile(3239, 3165, 0), new Tile(3239, 3160, 0), new Tile(3236, 3156, 0), new Tile(3233, 3152, 0), new Tile(3228, 3150, 0)};
    public static final Tile[] arrayPathToBank = {new Tile(3230, 3148, 0), new Tile(3231, 3153, 0), new Tile(3234, 3157, 0), new Tile(3238, 3161, 0), new Tile(3238, 3166, 0), new Tile(3238, 3171, 0), new Tile(3238, 3176, 0), new Tile(3240, 3181, 0), new Tile(3240, 3186, 0), new Tile(3244, 3190, 0), new Tile(3242, 3195, 0), new Tile(3239, 3200, 0), new Tile(3236, 3204, 0), new Tile(3234, 3209, 0), new Tile(3237, 3214, 0), new Tile(3234, 3218, 0), new Tile(3229, 3219, 0), new Tile(3224, 3219, 0), new Tile(3219, 3219, 0), new Tile(3215, 3216, 0), new Tile(3215, 3211, 0), new Tile(3210, 3209, 0), new Tile(3205, 3209, 0)};

    TilePath walkToMine = ctx.movement.newTilePath(arrayPathToMine);
    TilePath walkToBank = ctx.movement.newTilePath(arrayPathToBank);
    int mineCount = 0; //integer to decide which ore to mine
    int copperOre = 436;
    int tinOre = 438;
    int depositAll = -1;
    int firstFloorStairs = 16671; //first floor of lumby castle stairs id
    int[] miningAnimationIds = {625, 628, 629};



    @Override
    public void poll() {
        Random rand = new Random();

        if (ctx.inventory.select().count() == 28) {
            goBank(); //go banking
        }
        else{ //if inventory not full
            goMine();

            if (ctx.players.local().animation() != 628) { //if not mining
                mine();
                sleep(1000);
            }
        }

        if(rand.nextInt(1,100) == 1){
            ctx.camera.angle(rand.nextInt(0, 359));
            ctx.camera.pitch(rand.nextInt(0, 100));
            System.out.println("The camera moved randomly");
        }
    }

    //Method for going to the bank
    public void goBank(){
        GameObject stairs = ctx.objects.select(10).id(firstFloorStairs).nearest().poll();

        if(ctx.players.local().tile().floor() == 0) {

            if(stairs.valid() && stairs.inViewport()) {
                ctx.camera.turnTo(stairs_tile);
                ctx.objects.select(5).nearest().id(firstFloorStairs).poll().interact("climb-up");
                System.out.println("New stairs method successfully called");
                sleep(1214);
                mineCount++;
                System.out.println(mineCount);
            }
            else{
                walkToBank.traverse();
                System.out.println("New traverse method being called");
                sleep(1235);
            }
        }

        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(3).nearest().id(16672).poll().interact("climb-up");
            sleep(1115);
        }

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(bank_tile); //move to the center of the bank in castle
            ctx.bank.open();
            sleep(1785);
            ctx.bank.deposit(copperOre, 27); //id, amount
            ctx.bank.deposit(tinOre, 27);
        }
    }

    //Method for going back to mine
    public void goMine(){

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(upstairs_tile);
            ctx.objects.select(10).id(16673).nearest().poll().interact("Climb-down");
            System.out.println("Should climb down from 3rd floor");
            sleep(1200);
        }

        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(10).id(16672).nearest().poll().interact("Climb-down");
            sleep(1200);
        }

        if(ctx.players.local().tile().floor() == 0){
            sleep(1200);
            if(!mine_tile.matrix(ctx).inViewport()){
                walkToMine.traverse(); //this will take the player on a path to get to the mine
                System.out.println("The mine tile is not in the viewport, traversing");
            }
            else{
                System.out.println("The mine tile is in the viewport");
            }


        }
    }

    public void mine(){
        if(mineCount >= 10){ //once it increments to 2, it resets, so it mines both ores.
            mineCount = 0;
        }
        if(mineCount < 5){ //mine tin
            ctx.objects.select(10).id(7485, 7486).nearest().poll().interact("mine"); //select arg is 10 tiles, copper rock id.
        }
        else{ //mine copper
            ctx.objects.select(10).id(7484, 7453).nearest().poll().interact("mine"); //select arg is 10 tiles
        }
        System.out.println(mineCount);
    }

}