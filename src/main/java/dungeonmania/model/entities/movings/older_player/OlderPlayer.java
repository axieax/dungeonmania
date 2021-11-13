package dungeonmania.model.entities.movings.older_player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Observer;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvisibleState;
import dungeonmania.util.Position;
import java.util.List;

public class OlderPlayer extends Enemy {

    private MovementState state;

    private static final int MAX_OLDER_PLAYER_HEALTH = 100;
    private static final int MAX_OLDER_PLAYER_ATTACK_DMG = 10;

    public OlderPlayer(Position position, List<Position> moves, SubjectPlayer player) {
        super("older_player", position, MAX_OLDER_PLAYER_HEALTH, MAX_OLDER_PLAYER_ATTACK_DMG, 1);
        state = new RewindOlderPlayerState(this, moves.iterator());
        player.attach(this);
    }

    public void tick(Game game) {
        state.move(game);
    }

    public void update(SubjectPlayer player) {
        if (!(player instanceof Player)) return;
        Player character = (Player) player;

        if (
            // encounter
            character.getPosition().equals(this.getPosition()) &&
            !(
                // player invisible, has sun stone or has midnight armour
                character.getState() instanceof PlayerInvisibleState ||
                character.findInventoryItem("sun_stone") != null ||
                character.findInventoryItem("midnight_armour") != null
            )
        ) {
            // transition to battle state
            state = new BattleOlderPlayerState(character);
        }
    }
}
