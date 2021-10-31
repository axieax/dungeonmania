package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;

public class GoalTest {

    /**
     * Helper function for moving the Player
     *
     * @param dmc DungeonManiaController
     * @param direction to move the player
     * @param itemUsed item used by Player
     * @param ticks number of moves in the specified direction
     *
     * @return DungeonResponse of the last tick
     */
    private final DungeonResponse move(
        DungeonManiaController dmc,
        Direction direction,
        String itemUsed,
        int ticks
    ) {
        DungeonResponse resp = null;
        for (int i = 0; i < ticks; ++i) resp = dmc.tick(itemUsed, direction);
        return resp;
    }

    /**
     * Helper function for moving the Player
     *
     * @param dmc DungeonManiaController
     * @param direction to move the player
     * @param ticks number of moves in the specified direction
     *
     * @return DungeonResponse of the last tick
     */
    private final DungeonResponse move(DungeonManiaController dmc, Direction direction, int ticks) {
        return move(dmc, direction, "", ticks);
    }

    /**
     * Tests goal (exit) behaviour for the maze dungeon
     */
    @Test
    public final void mazeExitTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("maze", "standard");
        // navigate the maze
        String exitGoal = ":exit";
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 5).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 6).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 5).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 4).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.UP, 10).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.RIGHT, 4).getGoals());
        assertEquals(exitGoal, move(dmc, Direction.DOWN, 9).getGoals());
        assertEquals("", move(dmc, Direction.DOWN, 1).getGoals());
        // TODO: test moving onto and off a goal
    }

    /**
     * Tests goal (exit) behaviour for the maze dungeon
     */
    @Test
    public final void bouldersSwitchTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "standard");
        // navigate the maze
        String switchGoal = ":switch";
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals("", move(dmc, Direction.RIGHT, 1).getGoals());
        // TODO: test quantity as well?
    }

    /**
     * Tests goal (exit) behaviour for the bombs dungeon
     */
    @Test
    public final void bombsSwitchTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "standard");
        // navigate the maze
        String switchGoal = ":switch";
        // pick up the bomb
        DungeonResponse resp = move(dmc, Direction.RIGHT, 1);
        assertEquals(switchGoal, resp.getGoals());
        assertEquals("bomb", resp.getInventory().get(0).getPrefix());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());

        // place the bomb
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 3).getGoals());
        resp = move(dmc, Direction.LEFT, "bomb", 1);
        assertEquals(switchGoal, resp.getGoals());
        assertEquals(0, resp.getInventory().size());

        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals("", move(dmc, Direction.RIGHT, 1).getGoals());
        // TODO: test quantity as well?
    }
}
