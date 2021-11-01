package dungeonmania.model.entities.collectables;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class Bomb extends Item {

    private boolean isPlaced;

    public Bomb(Position position) {
        super("bomb", position);
        isPlaced = false;
    }

    /**
     * Place a bomb at the specified position on the dungeon.
     * @param position
     */
    public void place(Game game, Position position) {
        this.setPosition(position);
        game.addEntity(this);
        Player player = game.getCharacter();
        player.removeInventoryItem(this.getId());
        isPlaced = true;
    }

    /**
     * If the Player interacts with the Item, collect the item and put it in the
     * inventory.
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        if (!isPlaced) {
            super.interact(game, character);
        }
    }

    /**
     * Explodes the bomb destroying all entities in the bomb's blast radius, 
     * except for the character.
     * @param game
     */
    public void explode(Game game) {
        List<Entity> entities = game.getAdjacentEntities(this.getPosition());
        game.removeEntity(this);
        entities.forEach(entity -> {
            if (entity instanceof Bomb) ((Bomb)entity).explode(game);
            if (!(entity instanceof Player)) game.removeEntity(entity);
        });
    }
}
