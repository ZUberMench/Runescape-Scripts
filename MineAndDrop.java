package scripts;
//import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Inventory;
import org.powerbot.script.rt4.Item;

import static org.powerbot.script.Condition.sleep;

@Script.Manifest(name = "First Script!", properties = "author=BMoney; topic=1296203; client=4;", description = "First of the first my guy" )
public class MineAndDrop extends PollingScript<ClientContext> {


    @Override
    public void poll() {

        if (ctx.inventory.select().count() == 28) {
            ctx.inventory.select().id(436).each(item -> {
                item.interact("Drop");
                sleep(150);
                return true;
            });
        }


        if (ctx.players.local().animation() != 625) {
            //ctx.objects.select(10).id(7485, 7486).nearest().poll().interact("mine"); //select arg is 10 tiles, copper rock id.
            ctx.objects.select(10).id(7484, 7453, 7469).nearest().poll().interact("mine"); //select arg is 10 tiles, copper rock id.
            sleep(1000);


        }

    }

}
