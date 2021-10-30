package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    public final static int MAX_SPIDER_HEALTH = 20;
    public final static int MAX_SPIDER_ATTACK_DMG = 2;
    private boolean isInitialMove;
    private List<Direction> spiderMovementPath;
    private Direction nextMoveInPath;

    public Spider(Position position) {
        this(position, MAX_SPIDER_HEALTH, MAX_SPIDER_ATTACK_DMG);
    }

    public Spider(Position position, int health, int attackDamage) {
        super("spider", position, health, attackDamage);
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
        this.nextMoveInPath = spiderMovementPath.get(0); // index of spiderMovementPath
    }

    /**
     * Moves the spider onto the next tile, maintaining a "circular" path
     */
    @Override
    public void tick(Dungeon dungeon) {

        Position currentPos = this.getPosition();
        
        if(getIsInitialMove()) {
            doInitialSpiderMove(dungeon, currentPos);
        } else {
            moveSpider(dungeon, currentPos);
        }
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntity character) { }

    /**
     * Determines if a spider can move onto a position that contains the given entities
     * @param entitiesAtPos list of entities on the new position
     * @return true if the spider is free to pass, else false
     */
    public boolean canSpiderMoveOntoPosition(List<Entity> entitiesAtPos) {
        for(Entity e: entitiesAtPos) {
            if(e.getId() == "boulder") {
                return false;
            }
        }

        return false;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param dungeon the spider is contained in
     * @param currentPos of spider
     */
    private void doInitialSpiderMove(Dungeon dungeon, Position currentPos) {
        // initially always move up if possible, else stay in spot
        Position newPos = currentPos.translateBy(Direction.UP);
        List<Entity> entitiesNewPos = dungeon.getEntitiesAtPosition(newPos);
        if(entitiesNewPos == null || canSpiderMoveOntoPosition(entitiesNewPos)) {
            this.setPosition(newPos);
            setIsInitialMove(false);
        }
    }

    /**
     * Given the current position of a spider, moves a spider onto the next tile,
     * ensuring that the spider maintains a "circular path" and reverses direction
     * if necessary.
     * @param dungeon the spider is contained in
     * @param currentPos of spider
     */
    private void moveSpider(Dungeon dungeon, Position currentPos) {
        int indexOf = spiderMovementPath.indexOf(nextMoveInPath);
        
        Position newPos = currentPos.translateBy(nextMoveInPath);
        List<Entity> entitiesNewPos = dungeon.getEntitiesAtPosition(newPos);
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
    public boolean isCollidable(Entity entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }
}
