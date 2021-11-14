package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.util.Position;
import java.util.List;

public class Bomb extends Item implements Consumable {

    private boolean isPlaced;

    public Bomb(Position position) {
        super("bomb", position);
        isPlaced = false;
    }

    public void consume(Game game, Player player) {
        // Place a bomb at the specified position on the dungeon.
        this.setPosition(player.getPosition());
        game.addEntity(this);
        player.removeInventoryItem(this.getId());
        isPlaced = true;
        this.setPassable(false);
    }

    /**
     * If the Player interacts with the Item, collect the item and put it in the
     * inventory.
     */
    @Override
    public void interact(Game game, Entity character) {
        if (!isPlaced) {
            super.interact(game, character);
        }
    }

    /**
     * Explodes the bomb destroying all entities in the bomb's blast radius,
     * except for portals and the player.
     *
     * @param game game state
     */
    public void explode(Game game) {
        List<Entity> entities = game.getAdjacentEntities(this.getPosition());
        game.removeEntity(this);
        entities.forEach(entity -> {
            // Recursively explode all bombs in the blast radius
            if (entity instanceof Bomb) ((Bomb) entity).explode(game);
            // Do not destroy portals or the player
            if (!(entity instanceof Player || entity instanceof Portal)) game.removeEntity(entity);
        });
    }
}
