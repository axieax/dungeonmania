package dungeonmania.model;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class Game {
    private Mode mode;
    private Dungeon dungeon;
    private Goal goal;
    
    public Game(Mode mode, Dungeon dungeon, Goal goal) {
        this.mode = mode;
        this.dungeon = dungeon;
        this.goal = goal;
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse interact(String entityId)
        throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable)
        throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public Goal getRemainingGoals() {
        return goal;
    }
};