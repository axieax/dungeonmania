package dungeonmania.model.entities.movings.older_player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.util.Position;
import java.util.List;

public class OlderPlayer extends Enemy {

    private static final int MAX_OLDER_PLAYER_HEALTH = 100;
    private static final int MAX_OLDER_PLAYER_ATTACK_DMG = 10;

    public OlderPlayer(Position position, List<Position> moves) {
        super("older_player", position, MAX_OLDER_PLAYER_HEALTH, MAX_OLDER_PLAYER_ATTACK_DMG, 1);
        this.setMovementState(new RewindOlderPlayerState(this, moves.iterator()));
    }

    public void tick(Game game) {
        this.getMovementState().move(game);
    }

    @Override
    public void update(SubjectPlayer player) {}

}
