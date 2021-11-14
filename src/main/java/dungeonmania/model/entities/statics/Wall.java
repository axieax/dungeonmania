package dungeonmania.model.entities.statics;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Wall extends Entity {

    public Wall(Position position) {
        super("wall", position);
    }
}
