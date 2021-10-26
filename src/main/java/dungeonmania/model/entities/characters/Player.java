package dungeonmania.model.entities.characters;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.monster.Monster;
import dungeonmania.model.entities.item.Collectable;
import dungeonmania.model.entities.item.Storable;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Character implements Movement {

    public Player(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    private List<Storable> inventory = new ArrayList<>();

    public void battle(Monster opponent) {

    }

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
    public void collect(Dungeon dungeon) {
        
    }

    public Collectable getItem(String entityId) {
        return null;
    }

}
