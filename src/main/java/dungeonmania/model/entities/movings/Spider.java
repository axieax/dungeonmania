package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.movement.CircularMovementState;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.util.Position;
import java.util.Collections;
import java.util.List;

public class Spider extends Enemy {

    public static final int MAX_SPIDER_HEALTH = 20;
    public static final int MAX_SPIDER_ATTACK_DMG = 2;
    public static final int MAX_SPIDERS = 4;

    public Spider(Position position, int damageMultiplier, SubjectPlayer player) {
        super("spider", position, MAX_SPIDER_HEALTH, MAX_SPIDER_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new CircularMovementState(this));
        player.attach(this);
    }

    /**
     * Moves the spider onto the next tile, as per its current movement type
     */
    @Override
    public void tick(Game game) {
        this.move(game);
    }

    /**
     * If a player drinks an invincibility potion, change the state
     * of the spider to make sure it runs away
     */
    // @Override
    // public void update(SubjectPlayer player) {
    //     if (!(player instanceof Player)) {
    //         return;
    //     }

    //     Player character = (Player) player;
    //     if (character.getState() instanceof PlayerInvincibleState) {
    //         this.setMovementState(new RunMovementState(this));
    //     } else {
    //         this.setMovementState(new CircularMovementState(this));
    //     }
    // }

    /**
     * Spider does not track player as it is assumed that the spider only moves in a circular motion
     */
    @Override
    public void update(SubjectPlayer player) {
        return;
    }

    /**
     * Determines if a spider can move onto a position that contains the given entities
     * @param entitiesAtPos list of entities on the new position
     * @return true if the spider is free to pass, else false
     */
    public static boolean canSpiderMoveOntoPosition(List<Entity> entitiesAtPos) {
        for (Entity e : entitiesAtPos) {
            if (e.getType().equals("boulder")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Spawns a spider on an entity depending on the tick rate
     */
    public static void spawnSpider(Game game, int damageMultiplier) {
        int numSpidersInGame = getNumSpiderInGame(game);
        if (numSpidersInGame == MAX_SPIDERS) {
            return;
        }

        int tick = game.getTick();
        int tickRate = game.getTickRate();
        if (tick != 0 && tick % tickRate == 0) {
            // choose a random entity and spawn on it
            List<Entity> entities = game.getEntities(); // all entities in the dungeon
            Collections.shuffle(entities); // random order

            boolean canSpawn = false;
            Position position = null;
            for (Entity e : entities) {
                position = e.getPosition();
                List<Entity> entitiesAtPos = game.getEntities(position);
                if (canSpiderMoveOntoPosition(entitiesAtPos)) {
                    canSpawn = true;
                    break;
                }
            }

            if (canSpawn) {
                game.addEntity(new Spider(position, damageMultiplier, game.getCharacter()));
            }
        }
    }

    /**
     * Determines the number of spiders that currently exist in the game
     * @param game Game reference
     * @return number of spiders
     */
    public static int getNumSpiderInGame(Game game) {
        List<Entity> entities = game.getEntities();
        int spiders = 0;
        for (Entity e : entities) {
            if (e.getType().equals("spider")) {
                spiders++;
            }
        }

        return spiders;
    }

    //////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean collision(Entity entity) {
        if (
            entity instanceof Wall ||
            entity instanceof Door ||
            entity instanceof Portal ||
            entity instanceof Exit
        ) return false;
        return !entity.isPassable();
    }
}
