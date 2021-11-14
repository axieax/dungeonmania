package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public class TheOneRing extends Item implements Consumable {

    public TheOneRing(Position position) {
        super("one_ring", position);
    }

    public TheOneRing() {
        super("one_ring", null);
    }

    @Override
    public void consume(Game game, Player player) {
        player.removeInventoryItem(this.getId());
        player.setHealth(Player.maxCharacterHealth);
    }
}
