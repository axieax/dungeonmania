package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public class Treasure extends Item implements Consumable {

    public Treasure(Position position) {
        super("treasure", position);
    }

    @Override
    public void consume(Game game, Player player) {
        player.removeInventoryItem(this.getId());
    }

}
