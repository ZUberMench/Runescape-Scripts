package scripts;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.MessageEvent;
import sun.plugin2.message.Message;


//26254 is bank deposit id
import java.awt.*;

import static org.powerbot.script.Condition.sleep;


@Script.Manifest(name= "Rimmington Gold Miner", description= "Mines and banks gold in Rimmington")
public class RimmingtonGold extends PollingScript<ClientContext> implements PaintListener, MessageListener{
    Tile goldRock1 = new Tile(2975, 3235);
    Tile goldRock2 = new Tile(2977, 3234);
    public int goldMined = 0;

    public static final Tile[] arrayToBank = {new Tile(2976, 3234, 0), new Tile(2980, 3234, 0), new Tile(2984, 3235, 0), new Tile(2988, 3232, 0), new Tile(2991, 3235, 0), new Tile(2995, 3236, 0), new Tile(2999, 3236, 0), new Tile(3003, 3238, 0), new Tile(3006, 3241, 0), new Tile(3010, 3240, 0), new Tile(3014, 3241, 0), new Tile(3018, 3241, 0), new Tile(3022, 3241, 0), new Tile(3026, 3241, 0), new Tile(3029, 3238, 0), new Tile(3033, 3237, 0), new Tile(3037, 3237, 0), new Tile(3041, 3237, 0), new Tile(3045, 3236, 0)};
    public static final Tile[] arrayToMine = {new Tile(3045, 3235, 0), new Tile(3041, 3236, 0), new Tile(3037, 3236, 0), new Tile(3033, 3236, 0), new Tile(3029, 3236, 0), new Tile(3027, 3240, 0), new Tile(3023, 3241, 0), new Tile(3019, 3241, 0), new Tile(3015, 3241, 0), new Tile(3011, 3241, 0), new Tile(3007, 3240, 0), new Tile(3003, 3240, 0), new Tile(3000, 3237, 0), new Tile(2997, 3234, 0), new Tile(2993, 3231, 0), new Tile(2989, 3232, 0), new Tile(2985, 3234, 0), new Tile(2981, 3236, 0), new Tile(2977, 3234, 0)};
    TilePath walkToMine = ctx.movement.newTilePath(arrayToMine);
    TilePath walkToBank = ctx.movement.newTilePath(arrayToBank);
    @Override
    public void poll() {
        Random rand = new Random();


        if(ctx.inventory.select().count() == 28){
            goBank();
        }
        else{
            goMine();

            if (ctx.players.local().animation() != 624) { //if not mining
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

    public void goMine(){
        walkToMine.traverse();
    }

    public void goBank(){
        Random rand = new Random();
        int randomSleep = rand.nextInt(1000, 1500);
        GameObject depositBoxObject = ctx.objects.select(10).id(26254).nearest().poll();
        sleep(1235);
        walkToBank.traverse();
        if(depositBoxObject.valid() && depositBoxObject.inViewport()){
            ctx.objects.select(5).id(26254).nearest().poll().interact("Deposit");
            ctx.depositBox.deposit(444, DepositBox.Amount.ALL);
            sleep(randomSleep);
            System.out.println(randomSleep);
        }


    }

    public void mine(){
        ctx.objects.select(10).id(7491, 7458).nearest().poll().interact("mine");
    }

    public void bank(){

    }

    @Override
    public void repaint(Graphics graphics) {

    }

    @Override
    public void messaged(MessageEvent e) {
        String msg = e.text();
        if(msg.equals("You manage to mine some gold.")){
            goldMined++;
            System.out.println("Total gold mined: " + goldMined);
            System.out.println("Gold per Hour: " + goldMined / 60);
        }

    }
}
