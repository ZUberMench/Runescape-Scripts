package scripts;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

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

    Tile stairs_tile = new Tile(3205, 3209); //lumby castle first floor stairs
    Tile bank_tile = new Tile(3209, 3220); //bank location
    Tile upstairs_tile = new Tile(3205, 3209); //lumby castle 3rd floor stairs
    Tile smeltroom_tile = new Tile(3227, 3255); //lumby smelter room

    Component smeltingAction = ctx.widgets.component(311, 16);


    @Override
    public void poll() {
        if(ctx.inventory.select().id(tinOre).count() > 0 && ctx.inventory.select().id(copperOre).count() > 0){
            goSmelt();
        }
        else{
            goBank();
        }
       /* if(ctx.inventory.select().id(bronzeBar).count() == 10){
            goBank();

        }
        if(ctx.inventory.select().id(tinOre).count() > 0 && ctx.inventory.select().id(copperOre).count() > 0 ){
            goSmelt();

            if (ctx.players.local().animation() != smeltingAnimation) {
                smelt();
            }
        }

*/
    }


    public void goBank(){
        if(!stairs_tile.matrix(ctx).inViewport()){
            walkToBank.traverse();
        }

        if(ctx.players.local().tile().floor() == 0) {
            ctx.objects.select(10).nearest().id(16671).poll().interact("climb-up");
            sleep(1235);
        }

        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(3).nearest().id(16672).poll().interact("climb-up");
            sleep(1115);
        }

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(bank_tile); //move to the center of the bank in castle
            bank();
            //ctx.bank.open();
            //sleep(1785);
            //ctx.bank.deposit(bronzeBar, 10); //id, amount
        }

    }

    public void goSmelt(){
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
            if(!smeltroom_tile.matrix(ctx).inViewport()){
                walkToSmelter.traverse();
            }
            else{
                ctx.movement.step(smeltroom_tile);
                smelt();
            }

        }
    }

    public void smelt(){
       // ctx.inventory.select().id(copperOre).poll().interact("Use");
       // ctx.objects.select(10).id(smelter).poll().click();
        ctx.objects.select(10).id(smelter).nearest().poll().interact("Smelt");
        //ctx.objects.select(10).id(smelter).nearest().poll().click("Bronze");
        sleep(1240);
        //ctx.widgets.component(162, 16);
        smeltingAction.interact("Bronze");
        //ctx.objects.select(10).id(smelter).nearest().poll().interact("Smelt");
        //ctx.objects.select(10).id(smelter).nearest().poll().interact("Bronze");

    }

    public void bank(){
        if(bank_tile.matrix(ctx).inViewport()){
                ctx.bank.open();
                ctx.bank.deposit(bronzeBar, 10); //id, amount
                ctx.bank.withdraw(copperOre, 10);
                ctx.bank.withdraw(tinOre, 10);
        }
        else{
            ctx.movement.step(bank_tile);
        }
    }
}
