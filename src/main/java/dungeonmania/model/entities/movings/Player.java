package dungeonmania.model.entities.movings;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends MovingEntity {

    public Player(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    private List<Item> inventory = new ArrayList<>();

    public void battle(MovingEntity opponent) {

    }

    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub

        // Collects a Collectable entity and put it in the player's inventory if exists 
        // on the new current player position
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }

    public Item getItem(String entityId) {
        return null;
    }

}
