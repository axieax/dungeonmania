package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.mode.Hard;
import dungeonmania.util.Position;
import java.util.Random;
import org.json.JSONObject;

public class Hydra extends Enemy implements Boss {

    public static final int MAX_HYDRA_HEALTH = 50;
    public static final int MAX_HYDRA_ATTACK_DMG = 5;
    public static final int HYDRA_TICK_RATE = 50;

    private boolean preventHeadRespawn;

    public Hydra(Position position, int damageMultiplier, SubjectPlayer player) {
        super("hydra", position, MAX_HYDRA_HEALTH, MAX_HYDRA_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new RandomMovementState(this));
        preventHeadRespawn = false;
        player.attach(this);
    }

    public void setPreventHeadRespawn(Boolean respawn) {
        this.preventHeadRespawn = respawn;
    }

    @Override
    public void update(SubjectPlayer player) {
        if (!(player instanceof Player)) {
            return;
        }

        // If a player drinks an invincibility potion, change the state
        // of the hydra to make sure it runs away
        Player character = (Player) player;
        if (character.getState() instanceof PlayerInvincibleState) {
            this.setMovementState(new RunMovementState(this));
        } else {
            this.setMovementState(new RandomMovementState(this));
        }

        // If an anduril is going to be used in battle by a player against
        // the hydra, it will not be able to spawn another head
        if (character.getInBattle()) {
            MovingEntity opponent = character.getCurrentBattleOpponent();
            // character battling this hydra and has an anduril
            if (opponent.equals(this) && character.hasItemQuantity("anduril", 1)) {
                this.preventHeadRespawn = true;
            }
        }
    }

    @Override
    public void tick(Game game) {
        this.move(game);
    }

    public static void spawnHydra(Game game, int damageMultiplier) {
        // Hydra only spawns in hard mode
        if (!(game.getMode() instanceof Hard)) return;

        int tick = game.getTick();
        if (tick != 0 && tick % HYDRA_TICK_RATE == 0) {
            Position spawnPos = game.getPlayerSpawnLocation();
            if (spawnPos.equals(game.getCharacterPosition())) {
                // don't spawn
                return;
            }
            game.addEntity(new Hydra(spawnPos, damageMultiplier, game.getPlayer()));
        }
    }

    /**
     * Hydra is allowed to pass through portals
     */
    @Override
    public boolean collision(Entity entity) {
        if (entity instanceof Portal) return false;
        return !entity.isPassable();
    }

    /**
     * 0.5 chance that when a hydra is attacked by the character, or allies,
     * its health will increase rather than decrease by the attacking amount
     */
    @Override
    public void reduceHealthFromBattle(int amount) {
        Random rand = new Random();
        if (!preventHeadRespawn && rand.nextInt(2) == 0) {
            super.reduceHealthFromBattle(-amount);
        } else {
            preventHeadRespawn = false;
            super.reduceHealthFromBattle(amount);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("preventHeadRespawn", preventHeadRespawn);
        return info;
    }
}
