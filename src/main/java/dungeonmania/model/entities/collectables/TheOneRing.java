package dungeonmania.model.entities.collectables;

import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public class TheOneRing extends Item implements Consumable {

    public TheOneRing(Position position) {
        super(position);
    }

    @Override
    public void consume(Player player) {
        player.removeInventoryItem(this.getId());
        player.setHealth(Player.MAX_CHARACTER_HEALTH);
    }
}
