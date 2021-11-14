package dungeonmania.model.entities;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public abstract class Item extends Entity {

    public Item(String prefix, Position position) {
        super(prefix, position, false, true);
    }

    @Override
    public void interact(Game game, Entity character) {
        // If the Player interacts with the Item,
        // collect the item and put it in the inventory.
        if (!(character instanceof Player)) return;

        ((Player) character).collect(this);
        game.removeEntity(this);
    }
}
