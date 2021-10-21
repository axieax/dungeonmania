package dungeonmania.model.entities.movingEntity;

import dungeonmania.model.entities.GameEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends GameEntity implements Movement {
    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }

    // We will either need a battle interface or abstract method that will
    // decrease the health of the player and opponent appropriately
}
