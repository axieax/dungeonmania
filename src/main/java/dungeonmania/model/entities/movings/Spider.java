package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    
    public static final int MAX_SPIDER_HEALTH = 20;
    public static final int MAX_SPIDER_ATTACK_DMG = 2;
    public static final int MAX_SPIDERS = 4;
    private boolean isInitialMove;
    private List<Direction> spiderMovementPath;
    private boolean isMovementReverse;
    private int indexOfNextMove;

    public Spider(Position position) {
        this(position, MAX_SPIDER_HEALTH, MAX_SPIDER_ATTACK_DMG);
    }

    public Spider(Position position, int health, int attackDamage) {
        super("spider", position, health, attackDamage, true);
        this.isInitialMove = true;
        // Default "circling" movement of spider
        this.spiderMovementPath = Arrays.asList(
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN,
            Direction.LEFT,
            Direction.LEFT,
            Direction.UP,
            Direction.UP,
            Direction.RIGHT
        );

        // index of spiderMovementPath
        this.indexOfNextMove = 0;
        this.isMovementReverse = false;
    }

    /**
     * Moves the spider onto the next tile, maintaining a "circular" path
     */
    @Override
    public void tick(Game game) {
        Position currentPos = this.getPosition();
        
        if(getIsInitialMove()) {
            doInitialSpiderMove(game, currentPos);
        } else {
            moveSpider(game, currentPos);
        }
    }

    @Override
    public void interact(Game game, MovingEntity character) { }

    /**
     * Determines if a spider can move onto a position that contains the given entities
     * @param entitiesAtPos list of entities on the new position
     * @return true if the spider is free to pass, else false
     */
    public static boolean canSpiderMoveOntoPosition(List<Entity> entitiesAtPos) {
        for(Entity e: entitiesAtPos) {
            if(e.getPrefix().equals("boulder")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Spawns a spider on an entity depending on the tick rate
     */
    public static void spawnSpider(Game game) {
        int numSpidersInGame = getNumSpiderInGame(game);
        if(numSpidersInGame == MAX_SPIDERS) {
            return;
        }
        
        int tick = game.getTick();
        int tickRate = game.getTickRate();
        if(tick != 0 && tick % tickRate == 0) {
            // choose a random entity and spawn on it
            List<Entity> entities = game.getEntities(); // all entities in the dungeon
            Collections.shuffle(entities); // random order

            boolean canSpawn = false;
            Position position = null;
            for(Entity e: entities) {
                position = e.getPosition();
                List<Entity> entitiesAtPos = game.getEntities(position);
                if(canSpiderMoveOntoPosition(entitiesAtPos)) {
                    canSpawn = true;
                    break;
                }
            }

            if(canSpawn) {
                game.addEntity(new Spider(position));
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
        for(Entity e: entities) {
            if(e.getPrefix() == "spider") {
                spiders++;
            }
        }

        return spiders;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param game dungeon the spider is contained in
     * @param currentPos of spider
     */
    private void doInitialSpiderMove(Game game, Position currentPos) {
        // initially always move up if possible, else stay in spot
        Position newPos = currentPos.translateBy(Direction.UP);
        List<Entity> entitiesNewPos = game.getEntities(newPos);
        if(entitiesNewPos == null || canSpiderMoveOntoPosition(entitiesNewPos)) {
            this.setPosition(newPos);
            setIsInitialMove(false);
        }
    }

    /**
     * Given the current position of a spider, moves a spider onto the next tile,
     * ensuring that the spider maintains a "circular path" and reverses direction
     * if necessary.
     * @param game dungeon the spider is contained in
     * @param currentPos of spider
     */
    private void moveSpider(Game game, Position currentPos) {
        Direction nextMoveInPath = spiderMovementPath.get(indexOfNextMove);

        Position newPos = currentPos.translateBy(nextMoveInPath);
        List<Entity> entitiesNewPos = game.getEntities(newPos);
        if(entitiesNewPos == null || canSpiderMoveOntoPosition(entitiesNewPos)) {
            this.setPosition(newPos);
        } else { // reverse direction
            if(this.isMovementReverse) {
                this.isMovementReverse = false;
                indexOfNextMove = (indexOfNextMove + 5) % 8;
            } else {
                this.isMovementReverse = true;
                indexOfNextMove = (indexOfNextMove + 4) % 8;
            }
            Collections.reverse(this.spiderMovementPath);
            return; // no movement occurs if blocked
        }
        
        if(indexOfNextMove >= spiderMovementPath.size() - 1) { // end of movement path
            indexOfNextMove = 0;
        } else {
            indexOfNextMove += 1;
        }
    }

    public boolean getIsInitialMove() {
        return this.isInitialMove;
    }

    public void setIsInitialMove(boolean isInitialMove) {
        this.isInitialMove = isInitialMove;
    }

    @Override
    public boolean collision(Entity entity) {
        if (entity instanceof Wall) return false;
        return !entity.isPassable();
    }

}
