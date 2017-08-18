package scripts;
//import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Inventory;
import org.powerbot.script.rt4.Item;

import static org.powerbot.script.Condition.sleep;

@Script.Manifest(name = "Mine and Bank (Copper Tin)", properties = "author=BMoney; topic=1296203; client=4;", description = "Mines copper and tin in lumbridge and banks it" )
public class MineAndBank extends PollingScript<ClientContext> {

    Tile stairs_tile = new Tile(3205, 3209); //first floor
    Tile upstairs_tile = new Tile(x, y); //stairs near bank
    Tile bank_tile = new Tile(x, y); //bank location
    Tile mine_tile = new Tile(x, y); //mining location


    @Override
    public void poll() {

        if (ctx.inventory.select().count() == 28) {
            goBank(); //go banking

        }
        else(ctx.inventory.select().count() != 28){ //if inventory not full
            goMine();

            if (ctx.players.local().animation() != 625) { //if not mining
                //ctx.objects.select(10).id(7485, 7486).nearest().poll().interact("mine"); //select arg is 10 tiles, copper rock id.
                ctx.objects.select(10).id(7484, 7453, 7469).nearest().poll().interact("mine"); //select arg is 10 tiles, copper rock id.
                sleep(1000);
            }
        }
    }

    //Method for going to the bank
    public void goBank(){
        ctx.movement.step(stairs_tile); //move to the stairs location in lumbridge castle
        if(ctx.players.local().tile().floor() == 0) {
            ctx.objects.select(3).nearest().id(16671).poll().interact("climb-up", "stairs");
            sleep(1235);
        }
        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(3).nearest().id(16672).poll().interact("climb-up", "stairs");
            sleep(1115);
        }

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(bank_tile); //move to the center of the bank in castle
            ctx.bank.open();
            sleep(1785);
            ctx.bank.deposit(436, 27); //id, amount
        }
    }

    //Method for going back to mine
    public void goMine(){

        if(ctx.players.local().tile().floor() == 2){
            ctx.movement.step(upstairs_tile);
            ctx.objects.select(3).nearest().id(16672).poll().interact("climb-down", "stairs");
            sleep(1200);
        }

        if(ctx.players.local().tile().floor() == 1){
            ctx.objects.select(3).nearest().id(16671).poll().interact("climb-down", "stairs");
            sleep(1200);
        }

        if(ctx.players.local().tile().floor() == 0){
            sleep(1200);
            if(!mine_tile.matrix(ctx).inViewport()){
                ctx.movement.step(mine_tile); //move to mine
            } 
        }
    }

}