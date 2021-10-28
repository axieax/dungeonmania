package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {

    public MovingEntity(String entityId, Position position) {
        super(entityId, position, true, true);
        //TODO Auto-generated constructor stub
    }
    
    public abstract void move(Direction direction);

    public abstract void moveTo(Position position);
}
