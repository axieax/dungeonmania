package dungeonmania.model.entities.movings.older_player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.FollowPlayerMovementState;
import dungeonmania.model.entities.movings.player.Player;

public class BattleOlderPlayerState extends FollowPlayerMovementState implements OlderPlayerState {

    public BattleOlderPlayerState(Player player) {
        super(player);
    }

    public void move(Game game, OlderPlayer ilName) {
        this.move(game);
    }
}
