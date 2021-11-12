package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class SwampTile extends Entity {

    private int movementFactor;

    public int getMovementFactor() {
        return movementFactor;
    }

    public void setMovementFactor(int movementFactor) {
        this.movementFactor = movementFactor;
    }

    public SwampTile(Position position, int movementFactor) {
        super("swamp_tile", position, false, true);
        this.movementFactor = movementFactor;
    }

    @Override
    public void interact(Game game, Entity character) {
        // TODO
    }
}
