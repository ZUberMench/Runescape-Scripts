package scripts;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.MessageEvent;
import sun.plugin2.message.Message;


//26254 is bank deposit id
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import static org.powerbot.script.Condition.sleep;


@Script.Manifest(name= "Rimmington Gold Miner", description= "Mines and banks gold in Rimmington")
public class RimmingtonGold extends PollingScript<ClientContext> implements PaintListener, MessageListener{
    Tile goldRock1 = new Tile(2975, 3235);
    Tile goldRock2 = new Tile(2977, 3234);

    private long start_time;
    private int start_XP;
    private int current_XP;
    private int goldMined = 0;

    private Point mouse_point = new Point(0, 0);
    private String status = "Initiating";
    private int theta = 0;

    @Override
    public void start(){
        super.start();
        start_time = System.currentTimeMillis();
        start_XP = ctx.skills.experience(Constants.SKILLS_MINING);
    }

    public static final Tile[] arrayToBank = {new Tile(2976, 3234, 0), new Tile(2980, 3234, 0), new Tile(2984, 3235, 0), new Tile(2988, 3232, 0), new Tile(2991, 3235, 0), new Tile(2995, 3236, 0), new Tile(2999, 3236, 0), new Tile(3003, 3238, 0), new Tile(3006, 3241, 0), new Tile(3010, 3240, 0), new Tile(3014, 3241, 0), new Tile(3018, 3241, 0), new Tile(3022, 3241, 0), new Tile(3026, 3241, 0), new Tile(3029, 3238, 0), new Tile(3033, 3237, 0), new Tile(3037, 3237, 0), new Tile(3041, 3237, 0), new Tile(3045, 3236, 0)};
    public static final Tile[] arrayToMine = {new Tile(3045, 3235, 0), new Tile(3041, 3236, 0), new Tile(3037, 3236, 0), new Tile(3033, 3236, 0), new Tile(3029, 3236, 0), new Tile(3027, 3240, 0), new Tile(3023, 3241, 0), new Tile(3019, 3241, 0), new Tile(3015, 3241, 0), new Tile(3011, 3241, 0), new Tile(3007, 3240, 0), new Tile(3003, 3240, 0), new Tile(3000, 3237, 0), new Tile(2997, 3234, 0), new Tile(2993, 3231, 0), new Tile(2989, 3232, 0), new Tile(2985, 3234, 0), new Tile(2981, 3236, 0), new Tile(2977, 3234, 0)};
    TilePath walkToMine = ctx.movement.newTilePath(arrayToMine);
    TilePath walkToBank = ctx.movement.newTilePath(arrayToBank);
    @Override
    public void poll() {
        Random rand = new Random();
        current_XP = ctx.skills.experience(Constants.SKILLS_MINING);


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
    public void repaint(Graphics g) {
        long timeElapsed = System.currentTimeMillis() - start_time;
        int secs = (int) (timeElapsed / 1000);
        int mins = secs / 60;
        int hrs = mins / 60;
        secs %= 60;
        mins %= 60;

        float percent = percentToNextLevel(Constants.SKILLS_MINING);
        int lvl = ctx.skills.level(Constants.SKILLS_MINING);
        int x = 650;
        int y = 179;
        int bar_width = 20;
        int bar_height = 160;
        int xp = (current_XP - start_XP);
        int xphr = (int) ((xp * 3600000f) / timeElapsed);

        float xphrd = xphr / 1000f;

        g.setColor(Color.GRAY);
        g.fillRect(x, y, bar_width + 96, bar_height);
        // g.fillRect(x+96, y, bar_width, bar_height);
        g.setColor(Color.BLUE);
        g.fillRect(x + 96, (int) (y + bar_height - percent * bar_height), bar_width, (int) (bar_height * percent));
        g.setColor(Color.BLACK);
        g.drawRect(x, y, bar_width + 96, bar_height);
        g.drawRect(x + 96, y, bar_width, bar_height - 1);
        g.setColor(Color.BLACK);
        g.setFont(new Font("helvetica", Font.BOLD, 16));
        g.drawString(lvl + "", x + 96, y + 80);
        g.drawString("Gold Miner", x + 3, y + 18);
        y += 24;
        int i = 0;
        g.setFont(new Font("helvetica", Font.BOLD, 12));

        g.drawString("Elapsed Time", x + 5, y + 13 + 15 * i++);
        g.drawString(String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs),
                x + 15, y + 13 + 15 * i++);

        g.drawString("XP Gained", x + 5, y + 13 + 15 * i++);
        g.drawString("" + xp, x + 15, y + 13 + 15 * i++);

        g.drawString("XP Per Hour", x + 5, y + 13 + 15 * i++);
        g.drawString("" + String.format("%.1f", xphrd) + "K", x + 15, y + 13 + 15 * i++);

        g.drawString("Status", x + 5, y + 13 + 15 * i++);
        g.drawString(status, x + 15, y + 13 + 15 * i++);
        g.drawString(goldMined + " Gold Mined", x + 5, y + 13 + 15 * i++);
        mouse_point = ctx.input.getLocation();
        theta += 30;

        g.drawArc(mouse_point.x - 5, mouse_point.y - 5, 10, 10, theta, 60);
        g.drawArc(mouse_point.x - 5, mouse_point.y - 5, 10, 10, theta + 180, 60);
        g.fillOval(mouse_point.x - 3, mouse_point.y - 3, 6, 6);

    }

    @Override
    public void messaged(MessageEvent e) {
        String msg = e.text();
        if(msg.equals("You manage to mine some gold.")){
            goldMined++;
            System.out.println("Total gold mined: " + goldMined);
            //System.out.println("Gold per Hour: " + goldMined / 60);
        }

    }

    public float percentToNextLevel(int skill){
        return (float) (ctx.skills.experience(skill) - Constants.SKILLS_XP[ctx.skills.realLevel(skill)])
                / (float) (Constants.SKILLS_XP[ctx.skills.realLevel(skill) + 1]
                    - Constants.SKILLS_XP[ctx.skills.realLevel(skill)]);
    }
}