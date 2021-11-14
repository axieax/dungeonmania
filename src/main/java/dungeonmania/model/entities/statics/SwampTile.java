package dungeonmania.model.entities.statics;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class SwampTile extends Entity {

    private int movementFactor;

    public SwampTile(Position position, int movementFactor) {
        super("swamp_tile", position, false, true);
        this.movementFactor = movementFactor;
    }

    /**
     * Get movement factor for a swamp tile
     *
     * @return movement factor
     */
    public int getMovementFactor() {
        return movementFactor;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("movementFactor", movementFactor);
        return info;
    }
}
