package dungeonmania.model.entities.movings;

import org.json.JSONObject;

public interface PlayerState {
    public void battle(MovingEntity opponent);

    public void updateState(Player player);

    public int ticksLeft();
}
