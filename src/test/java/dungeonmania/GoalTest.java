package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Character;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.AndComposite;
import dungeonmania.model.goal.DestroyEnemies;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.goal.GoalComposite;
import dungeonmania.model.goal.ToggleSwitch;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.List;
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
        String exitGoal = ":exit(1)";
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
    }

    /**
     * Tests goal (exit) behaviour for the maze dungeon
     */
    @Test
    public final void bouldersSwitchTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "standard");
        // navigate the maze
        String switchGoal = ":switch(1)";
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
    }

    /**
     * Tests goal (exit) behaviour for the bombs dungeon
     */
    @Test
    public final void bombsSwitchTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "standard");
        // navigate the maze
        String switchGoal = ":switch(1)";
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
    }

    @Test
    public final void testSimpleExit() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Exit(new Position(0, 2))
        );
        Game game = new Game("test", entities, new ExitCondition(), null);
        assertEquals(":exit(1)", game.tick("", Direction.UP).getGoals());
        assertEquals("", game.tick("", Direction.UP).getGoals());
    }

    @Test
    public final void testSimpleBoulders() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            // setup boulder to be pushed onto switch
            new Boulder(new Position(0, 2)),
            new FloorSwitch(new Position(0, 3)),
            // boulder already on switch
            new Boulder(new Position(0, 1)),
            new FloorSwitch(new Position(0, 1))
        );
        Game game = new Game("test", entities, new ToggleSwitch(), null);
        assertEquals(":switch(1)", game.tick("", Direction.UP).getGoals());
        assertEquals("", game.tick("", Direction.UP).getGoals());
    }

    @Test
    public final void testSimpleEnemiesMercenaryKilled() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Mercenary(new Position(0, 3))
        );
        Game game = new Game("test", entities, new DestroyEnemies(), null);
        assertEquals(":enemies(1)", game.tick("", Direction.UP).getGoals());
        assertEquals("", game.tick("", Direction.UP).getGoals());
    }

    @Test
    public final void testSimpleEnemiesMercenaryBribed() {
        Entity mercenary = new Mercenary(new Position(0, 4));
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(0, 1)),
            mercenary
        );
        Game game = new Game("test", entities, new DestroyEnemies(), null);
        // pick up treasure
        assertEquals(":enemies(1)", game.tick("", Direction.UP).getGoals());
        // bribe mercenary
        assertEquals("", game.interact(mercenary.getId()).getGoals());
    }

    @Test
    public final void testSimpleEnemiesSpider() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Spider(new Position(0, -2))
        );
        Game game = new Game("test", entities, new DestroyEnemies(), null);
        assertEquals(":enemies(1)", game.tick("", Direction.DOWN).getGoals());
        // spider moves up
        assertEquals("", game.interact("", Direction.DOWN).getGoals());
    }

    @Test
    public final void testSimpleEnemiesZombie() {
        Entity player = new Player(new Position(0, 0));
        List<Entity> entities = Arrays.asList(
            player,
            new ZombieToast(new Position(0, -2), (Character) player),
            // walls to force direction
            new Wall(new Position(-1, 0)),
            new Wall(new Position(-1, -1)),
            new Wall(new Position(-1, -2)),
            new Wall(new Position(-1, -3)),
            new Wall(new Position(0, -3)),
            new Wall(new Position(1, -3)),
            new Wall(new Position(1, -2)),
            new Wall(new Position(1, -1)),
            new Wall(new Position(1, 0))
        );
        Game game = new Game("test", entities, new DestroyEnemies(), null);
        assertEquals(":enemies(1)", game.tick("", Direction.DOWN).getGoals());
        // zombie toast moves up
        assertEquals("", game.tick("", Direction.DOWN).getGoals());
    }

    @Test
    public final void testSimpleEnemiesZombieSpawner() {
        Entity spawner = new ZombieToastSpawner(new Position(0, 2));
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Sword(new Position(0, 1)),
            spawner
        );
        Game game = new Game("test", entities, new ToggleSwitch(), null);
        assertEquals(":enemies(1)", game.tick("", Direction.UP).getGoals());
        // destroy spawner
        assertEquals("", game.interact(spawner.getId()).getGoals());
    }

    @Test
    public final void testAndComposite() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            // boulder can be pushed onto the switch
            new Boulder(new Position(0, 4)),
            new FloorSwitch(new Position(0, 5)),
            // exit is nearby
            new Exit(new Position(0, 2))
        );
        // setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new ToggleSwitch());
        // check goal reached first
        Game game = new Game("test", entities, and, null);
        List<String> expected = Arrays.asList(":exit(1) AND :switch(1)", ":switch(1) AND :exit(1)");
        assertTrue(expected.contains(game.tick("", Direction.UP).getGoals()));
        // move onto exit
        assertEquals(":switch(1)", game.tick("", Direction.UP).getGoals());
        // move off exit
        assertTrue(expected.contains(game.tick("", Direction.UP).getGoals()));
        // push boulder onto switch
        assertEquals(":exit(1)", game.tick("", Direction.UP).getGoals());
        // move down
        assertEquals(":exit(1)", game.tick("", Direction.DOWN).getGoals());
        // all goals satisfied
        assertEquals("", game.tick("", Direction.DOWN).getGoals());
    }

    @Test
    public final void testOrComposite1() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)), // go left to get Treasure
            new Exit(new Position(1, -1))
        );
        // setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new ToggleSwitch());
        // check goal reached first
        Game game = new Game("test", entities, and, null);
        List<String> expected = Arrays.asList(
            ":treasure(1) AND :exit(1)",
            ":exit(1) AND :treasure(1)"
        );
        assertTrue(expected.contains(game.tick("", Direction.UP).getGoals()));
        // take treasure
        assertEquals("", game.tick("", Direction.LEFT).getGoals());
    }

    @Test
    public final void testOrComposite2() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)),
            new Exit(new Position(1, -1)) // go right to reach exit
        );
        // setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new ToggleSwitch());
        // check goal reached first
        Game game = new Game("test", entities, and, null);
        List<String> expected = Arrays.asList(
            ":treasure(1) AND :exit(1)",
            ":exit(1) AND :treasure(1)"
        );
        assertTrue(expected.contains(game.tick("", Direction.UP).getGoals()));
        // reach exit
        assertEquals("", game.tick("", Direction.RIGHT).getGoals());
    }


    /**
     * Test that Portals does not have a goal
     */
    @Test
    public void testPortalsGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("portals", "Standard"));
        DungeonResponse stateOne = controller.tick (null, Direction.NONE);
        assertEquals(stateOne.getGoals(), "");  
    }

        /**
     * This tests the completion of goals in the advanced dungeon
     */
    @Test
    public void testAdvancedGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        String treasureGoal = ":treasure";

        assertEquals(treasureGoal, move(controller, Direction.DOWN, 3).getGoals());
        assertEquals(treasureGoal, move(controller, Direction.RIGHT, 6).getGoals());
        assertEquals(treasureGoal, move(controller, Direction.RIGHT, 5).getGoals());
        assertEquals("", move(controller, Direction.RIGHT, 1).getGoals());
    }

}
