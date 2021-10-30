package dungeonmania.model.entities.collectables;

import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class Bomb extends Item {

    public Bomb(Position position) {
        super("bomb", position);
    }

    /**
     * Place a bomb at the specified position on the dungeon.
     * @param position
     */
    public void place(Dungeon dungeon, Position position) {
        this.setPosition(position);
        dungeon.addEntity(this);
    }

    /**
     * Explodes the bomb destroying all entities in the bomb's blast radius, 
     * except for the character.
     * @param dungeon
     */
    public void explode(Dungeon dungeon) {
        List<Entity> entities = dungeon.getAdjacentEntities(this.getPosition());
        dungeon.removeEntity(this);
        entities.forEach(entity -> {
            if (entity instanceof Bomb) ((Bomb)entity).explode(dungeon);
            if (!(entity instanceof Player)) dungeon.removeEntity(entity);
        });
    }
}
