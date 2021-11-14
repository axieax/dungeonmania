package dungeonmania.model.entities.statics;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Exit extends Entity {

    public Exit(Position position) {
        super("exit", position, false, true);
    }
}
