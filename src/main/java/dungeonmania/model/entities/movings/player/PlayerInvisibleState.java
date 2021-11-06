package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;

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
    public void battle(Game game, MovingEntity opponent) {}

    @Override
    public void updateState(Player player) {
        timeLimit--;
        if (timeLimit == 0) {
            this.player.setState(new PlayerDefaultState(player));
        }
    }
    public int ticksLeft() {
        return timeLimit;
    }
}
