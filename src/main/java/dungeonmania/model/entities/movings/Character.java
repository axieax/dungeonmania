package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.util.Direction;

public interface Character {
    public void battle(Dungeon dungeon, MovingEntity opponent);

    public void collect();

    public void build(String itemId);

    public void consume(Item item);

    public Item getInventoryItem(String itemId);

    public void move(Dungeon dungeon, Direction direction);
}
