package dungeonmania.model.entities.movings;

import org.json.JSONObject;

public interface PlayerState {
    public void battle(Game game, MovingEntity opponent);

    public void updateState(Player player);

    public int ticksLeft();
}
