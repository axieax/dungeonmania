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
    private boolean isInitialMove;
    private List<Direction> spiderMovementPath;
    private Direction nextMoveInPath;

    public Spider(Position position, int damageMultiplier) {
        super("spider", position, MAX_SPIDER_HEALTH, MAX_SPIDER_ATTACK_DMG, true, damageMultiplier);
        this.isInitialMove = true;
        // default "circling" movement of spider
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
        this.nextMoveInPath = spiderMovementPath.get(0);
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
                }
            }

            if(canSpawn) {
                game.addEntity(new Spider(position, game.getMode().damageMultiplier()));
            }
        }
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
        int indexOf = spiderMovementPath.indexOf(nextMoveInPath);
        
        Position newPos = currentPos.translateBy(nextMoveInPath);
        List<Entity> entitiesNewPos = game.getEntities(newPos);
        if(entitiesNewPos == null || canSpiderMoveOntoPosition(entitiesNewPos)) {
            this.setPosition(newPos);
        } else { // reverse direction
            Collections.reverse(spiderMovementPath);
        }
        
        if(indexOf == spiderMovementPath.size() - 1) { // end of movement path
            nextMoveInPath = spiderMovementPath.get(0);
        } else {
            nextMoveInPath = spiderMovementPath.get(indexOf += 1);
        }
    }

    public boolean getIsInitialMove() {
        return this.isInitialMove;
    }

    public void setIsInitialMove(boolean isInitialMove) {
        this.isInitialMove = isInitialMove;
    }

    @Override
    public Direction getDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean collision(Entity entity) {
        if (entity instanceof Wall) return false;
        return !entity.isPassable();
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isEnemy() {
        return true;
    }
}
