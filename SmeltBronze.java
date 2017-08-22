package scripts;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import static org.powerbot.script.Condition.sleep;

@Script.Manifest(name= "Smelt Bronze", description = "Lumby bronze smelt")
public class SmeltBronze extends PollingScript<ClientContext>
{

    public static final Tile[] arrayPathToBank = {new Tile(3227, 3255, 0), new Tile(3225, 3250, 0), new Tile(3224, 3245, 0), new Tile(3223, 3240, 0), new Tile(3226, 3236, 0), new Tile(3230, 3232, 0), new Tile(3230, 3227, 0), new Tile(3231, 3222, 0), new Tile(3227, 3219, 0), new Tile(3222, 3219, 0), new Tile(3217, 3219, 0), new Tile(3215, 3214, 0), new Tile(3211, 3211, 0), new Tile(3206, 3209, 0)};
    public static final Tile[] arrayPathToSmelter = {new Tile(3205, 3209, 0), new Tile(3209, 3209, 0), new Tile(3213, 3209, 0), new Tile(3215, 3213, 0), new Tile(3215, 3217, 0), new Tile(3219, 3218, 0), new Tile(3223, 3219, 0), new Tile(3227, 3219, 0), new Tile(3231, 3219, 0), new Tile(3232, 3223, 0), new Tile(3232, 3227, 0), new Tile(3231, 3231, 0), new Tile(3227, 3234, 0), new Tile(3224, 3237, 0), new Tile(3224, 3241, 0), new Tile(3224, 3245, 0), new Tile(3224, 3249, 0), new Tile(3225, 3253, 0)};

    TilePath walkToBank = ctx.movement.newTilePath(arrayPathToBank);
    TilePath walkToSmelter = ctx.movement.newTilePath(arrayPathToSmelter);

    int smeltingAnimation = 899;
    int bronzeBar = 2349;
    int copperOre = 436;
    int tinOre = 438;
    int smelter = 24009;
    int firstFloorStairs = 16671; //first floor of lumby castle stairs id

    Tile stairs_tile = new Tile(3205, 3209); //lumby castle first floor stairs
    Tile bank_tile = new Tile(3209, 3220); //bank location
    Tile upstairs_tile = new Tile(3205, 3209); //lumby castle 3rd floor stairs
    Tile smeltroom_tile = new Tile(3227, 3255); //lumby smelter room

    Component smeltingAction = ctx.widgets.component(311, 16); //this component is used to click "Bronze" in the furnace



    @Override
    public void poll() {
        Random rand = new Random();

        if(ctx.inventory.select().id(tinOre).count() > 0 && ctx.inventory.select().id(copperOre).count() > 0){
            goSmelt();
        }
        else{
            goBank();
        }
        if(rand.nextInt(1,100) == 1){
            ctx.camera.angle(rand.nextInt(0, 359));
            ctx.camera.pitch(rand.nextInt(0, 100));
            System.out.println("The camera moved randomly");
        }
    }

    public void goBank(){
        GameObject stairs = ctx.objects.select(10).id(firstFloorStairs).nearest().poll();

        if(ctx.players.local().tile().floor() == 0) {

            if(stairs.valid() && stairs.inViewport()) {
                ctx.camera.turnTo(stairs_tile);
                ctx.objects.select(5).nearest().id(firstFloorStairs).poll().interact("climb-up");
                System.out.println("New stairs method succesfully called");
                sleep(1214);
            }
            else{
                walkToBank.traverse();
                System.out.println("New traverse method being called");
                sleep(1235);
            }
        }

        //DEBUGGING CHECK
        if(stairs_tile.matrix(ctx).inViewport()){
            System.out.println("The stairs tile is in the viewport");
        }

        //DEBUGGING CHECK
        if(ctx.objects.select(10).id(16671).viewable().poll().inViewport()){
            System.out.println("The stairs are viewable");
        }



        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(3).nearest().id(16672).poll().interact("climb-up");
            sleep(1115);
        }

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(bank_tile); //move to the center of the bank in castle
            bank();
        }
    }

    public void goSmelt(){
        GameObject furnace = ctx.objects.select(10).id(smelter).nearest().poll();

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

        if(ctx.players.local().tile().floor() == 0) {
            sleep(1200);
            if (!smeltroom_tile.matrix(ctx).inViewport()) {
                walkToSmelter.traverse();
            } else {
                //ctx.camera.turnTo(smeltroom_tile);
                smelt();
            }

            if (furnace.valid()) {
                ctx.camera.turnTo(furnace);
            }
        }
    }

    public void smelt(){
        ctx.objects.select(15).id(smelter).nearest().poll().interact("Smelt");
        sleep(1427);
        smeltingAction.interact("Bronze");
        sleep(1355);
    }

    public void bank(){
        if(bank_tile.matrix(ctx).inViewport()){
            ctx.bank.open();
            ctx.bank.deposit(bronzeBar, 14); //id, amount
            ctx.bank.withdraw(copperOre, 14);
            ctx.bank.withdraw(tinOre, 14);
        }
        else{
            ctx.movement.step(bank_tile);
        }
    }
}