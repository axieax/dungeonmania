package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends Entity {

    public TimeTravellingPortal(Position position) {
        super("time_travelling_portal", position);
        this.setPassable(true);
    }

    public void interact(Game game, Entity character) {}
}
