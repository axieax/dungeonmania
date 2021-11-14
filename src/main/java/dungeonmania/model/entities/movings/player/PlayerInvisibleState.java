package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;

public class PlayerInvisibleState implements PlayerState {
    
    private Player player;
    private int timeLimit = 6;

    public PlayerInvisibleState(Player player) {
        this.player = player;
    }

    /**
     * Player immediately become "invisible" and can move past all other entities undetected.
     * This is implemented by simulating attacks from both parties as having no effect.
     */
    @Override
    public void battle(Game game, Enemy opponent) {}

    @Override
    public void updateState(Player player) {
        if (timeLimit <= 0) {
            this.player.setState(new PlayerDefaultState(player));
        }
        timeLimit--;
    }

    @Override
    public int ticksLeft() {
        return timeLimit;
    }
}
