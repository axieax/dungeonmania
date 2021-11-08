package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class SwampTile extends Entity {

    public SwampTile(Position position) {
        // TODO: double check prefix
        super("swamp_tile", position, false, true);
    }

    @Override
    public void interact(Game game, Entity character) {
        // TODO
    }
}
