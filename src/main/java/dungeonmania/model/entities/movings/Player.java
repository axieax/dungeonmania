package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingEntity {

    public Player(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    private List<Item> inventory = new ArrayList<>();

    public void battle(MovingEntity opponent) {}

    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub

    }

    // Collects a Collectable entity and put it in the player's inventory if exists
    // on the current player position
    public void collect(Item item) {}

    public Item getItem(String entityId) {
        return null;
    }

    public boolean hasKey() {
        return false;
    }

    public Direction getDirection() {
        return null;
    }

    public Key getKey() {
        return null;
    }

    public void consume(String itemId) {
        return;
    }

    public boolean hasWeapon() {
        return false;
    }
}
