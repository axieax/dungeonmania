package dungeonmania.model.entities.movings;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.mode.Hard;
import dungeonmania.util.Position;

public class Hydra extends Enemy implements Boss {
    private static final int MAX_HYDRA_HEALTH = 50;
    private static final int MAX_HYDRA_ATTACK_DMG = 5;
    private static final int HYDRA_TICK_RATE = 50;
    
    boolean preventHeadRespawn;

    public Hydra(Position position, int damageMultiplier, SubjectPlayer player) {
        super("hydra", position, MAX_HYDRA_HEALTH, MAX_HYDRA_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new RandomMovementState(this));
        preventHeadRespawn = false;
        player.attach(this);
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
        if(character.getInBattle()) {
            MovingEntity opponent = character.getCurrentBattleOpponent();
            // character battling this hydra and has an anduril
            if(opponent.equals(this) && character.hasItemQuantity("anduril", 1)) {
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
        if(!(game.getMode() instanceof Hard)) {
            return;
        }

        int tick = game.getTick();
        int tickRate = HYDRA_TICK_RATE;
        if (tick != 0 && tick % tickRate == 0) {
            // choose a random entity and spawn on it
            List<Entity> entities = game.getEntities(); // all entities in the dungeon
            Collections.shuffle(entities); // random order

            boolean canSpawn = false;
            Position position = null;
            for (Entity e : entities) {
                position = e.getPosition();
                List<Entity> entitiesAtPos = game.getEntities(position);
                if (canHydraMoveOntoPosition(entitiesAtPos)) {
                    canSpawn = true;
                    break;
                }
            }

            if (canSpawn) {
                game.addEntity(new Hydra(position, damageMultiplier, game.getCharacter()));
            }
        }

    }

	private static boolean canHydraMoveOntoPosition(List<Entity> entitiesAtPos) {
        for (Entity e : entitiesAtPos) {
            if (!e.isPassable()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean collision(Entity entity) {
        if(
            entity instanceof Portal
        ) return false;
        return !entity.isPassable();
    }

    /**
     * 0.5 chance that when a hydra is attacked by the character, or allies,
     * its health will increase rather than decrease by the attacking amount
     */
    @Override
    public void reduceHealthFromBattle(int amount) {
        Random rand = new Random();
        if(!preventHeadRespawn && rand.nextInt(100) % 2 == 0) {
            super.reduceHealthFromBattle(-amount);
        } else {
            preventHeadRespawn = false;
            super.reduceHealthFromBattle(amount);
        }
    }
}
