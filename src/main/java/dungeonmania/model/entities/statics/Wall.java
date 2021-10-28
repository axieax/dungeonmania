package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.util.Position;

public class Wall extends Entity {

    public Wall(String entityId, Position position) {
        super(entityId, position);
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        // TODO Auto-generated method stub
        
    }
}
