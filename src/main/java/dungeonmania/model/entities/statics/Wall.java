package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Position;

public class Wall extends Entity {

    public Wall(Position position) {
        super("wall", position);
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntity character) {
        // TODO Auto-generated method stub
        
    }
}
