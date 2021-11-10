package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.AndComposite;
import dungeonmania.model.goal.CollectTreasure;
import dungeonmania.model.goal.DestroyEnemies;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.goal.GoalComposite;
import dungeonmania.model.goal.OrComposite;
import dungeonmania.model.goal.ToggleSwitch;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
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
        return move(dmc, direction, null, ticks);
    }

    /**
     * Tests goal (exit) behaviour for the maze dungeon
     */
    @Test
    public final void mazeExitTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("maze", "Standard");
        // Navigate the maze
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
        dmc.newGame("boulders", "Standard");
        // Navigate the maze
        String switchGoal = ":switch(6)";
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        switchGoal = switchGoal.replace("6", "5");
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        switchGoal = switchGoal.replace("5", "6");
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        switchGoal = switchGoal.replace("6", "5");
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        switchGoal = switchGoal.replace("5", "4");
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        switchGoal = switchGoal.replace("4", "3");
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        switchGoal = switchGoal.replace("3", "2");
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        switchGoal = switchGoal.replace("2", "1");
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
        dmc.newGame("bombs", "Standard");
        // Navigate the maze
        String switchGoal = ":switch(6)";
        // Pick up the bomb
        DungeonResponse resp = move(dmc, Direction.RIGHT, 1);
        assertEquals(switchGoal, resp.getGoals());
        assertEquals("bomb", resp.getInventory().get(0).getType());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        switchGoal = switchGoal.replace("6", "5");
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        switchGoal = switchGoal.replace("5", "6");
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        switchGoal = switchGoal.replace("6", "5");
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        switchGoal = switchGoal.replace("5", "4");
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 4).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        switchGoal = switchGoal.replace("4", "3");
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        switchGoal = switchGoal.replace("3", "2");
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        switchGoal = switchGoal.replace("2", "3");
        assertEquals(switchGoal, move(dmc, Direction.UP, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        switchGoal = switchGoal.replace("3", "2");
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.RIGHT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.DOWN, 3).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 1).getGoals());

        // place the bomb
        resp = move(dmc, Direction.NONE, resp.getInventory().get(0).getId(), 1);
        assertEquals(switchGoal, resp.getGoals());
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("bomb")));

        assertEquals(switchGoal, move(dmc, Direction.DOWN, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.LEFT, 2).getGoals());
        assertEquals(switchGoal, move(dmc, Direction.UP, 1).getGoals());
        assertEquals("", move(dmc, Direction.RIGHT, 1).getGoals());
    }

    /**
     * Test exit condition is satisfied when player moves through exit
     */
    @Test
    public final void testSimpleExit() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Exit(new Position(0, 2))
        );
        Game game = new Game("test", entities, new ExitCondition(), new Standard());
        assertEquals(":exit(1)", game.tick(null, Direction.DOWN).getGoals());
        assertEquals("", game.tick(null, Direction.DOWN).getGoals());
    }

    /**
     * Test switch condition is satisfied when boulder is moved onto a switch
     */
    @Test
    public final void testSimpleBoulders() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            // Setup boulder to be pushed onto switch
            new Boulder(new Position(0, 2)),
            new FloorSwitch(new Position(0, 3)),
            // Boulder already on switch
            new Boulder(new Position(1, 0)),
            new FloorSwitch(new Position(1, 0))
        );
        Game game = new Game("test", entities, new ToggleSwitch(), new Standard());
        assertEquals(":switch(1)", game.tick(null, Direction.DOWN).getGoals());
        assertEquals("", game.tick(null, Direction.DOWN).getGoals());
    }

    /**
     * Test enemy condition is satisfied when player engages in battle with mercenary
     * and wins
     */
    @Test
    public final void testSimpleEnemiesMercenaryKilled() {
        Mode mode = new Standard();
        Player player = new Player(new Position(0, 0));
        List<Entity> entities = Arrays.asList(
            player,
            new Mercenary(new Position(0, 3), mode.damageMultiplier(), player)
        );
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        assertEquals(":enemies(1)", game.tick(null, Direction.DOWN).getGoals());
        assertEquals("", game.tick(null, Direction.DOWN).getGoals());
    }

    /**
     * Test enemy condition is satisfied when a player bribes a mercenary
     */
    @Test
    public final void testSimpleEnemiesMercenaryBribed() {
        Mode mode = new Standard();
        Player player = new Player(new Position(0, 0));
        Entity mercenary = new Mercenary(new Position(0, 4), mode.damageMultiplier(), player);
        List<Entity> entities = Arrays.asList(player, new Treasure(new Position(0, 1)), mercenary);
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        // Pick up treasure
        assertEquals(":enemies(1)", game.tick(null, Direction.DOWN).getGoals());
        // Bribe mercenary
        assertEquals("", game.interact(mercenary.getId()).getGoals());
    }

    /**
     * Test enemy goal is satisfied when zombie is killed
     */
    @Test
    public final void testSimpleEnemiesZombie() {
        Mode mode = new Standard();
        Player player = new Player(new Position(0, 0));
        ZombieToast zombie = new ZombieToast(new Position(0, -1), mode.damageMultiplier(), player);
        List<Entity> entities = new ArrayList<>();
        entities.add(player);
        entities.add(zombie);
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        DungeonResponse resp = game.getDungeonResponse();
        assertEquals(":enemies(1)", resp.getGoals());
        // Zombie toast moves up
        assertEquals("", game.tick(null, Direction.UP).getGoals());
    }

    /**
     * Test enemy goal is satisfied when zombie spawner is killed
     */
    @Test
    public final void testSimpleEnemiesZombieSpawner() {
        Mode mode = new Standard();
        Entity spawner = new ZombieToastSpawner(new Position(0, 2), mode.tickRate());
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Sword(new Position(0, 1)),
            spawner
        );
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        assertEquals(":enemies(1)", game.tick(null, Direction.DOWN).getGoals());
        // Destroy Spawner
        assertEquals("", game.interact(spawner.getId()).getGoals());
    }

    /**
     * Test composite AND goals require both goals to be satisfied
     */
    @Test
    public final void testAndComposite() {
        Mode mode = new Standard();
        // Setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            // Boulder can be pushed onto the switch
            new Boulder(new Position(0, 4)),
            new FloorSwitch(new Position(0, 5)),
            // Exit is nearby
            new Exit(new Position(0, 2))
        );
        // Setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new ToggleSwitch());
        // Check goal reached first
        Game game = new Game("test", entities, and, mode);
        List<String> expected = Arrays.asList(":exit(1) AND :switch(1)", ":switch(1) AND :exit(1)");
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // Move onto exit
        assertEquals(":switch(1)", game.tick(null, Direction.DOWN).getGoals());
        // Move off exit
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // Push boulder onto switch
        assertEquals(":exit(1)", game.tick(null, Direction.DOWN).getGoals());
        // Move down
        assertEquals(":exit(1)", game.tick(null, Direction.UP).getGoals());
        // All goals satisfied
        assertEquals("", game.tick(null, Direction.UP).getGoals());
    }

    /**
     * Test that composite and goals require a certain order.
     * You must collect the treasure before you exit the dungeon to
     * satisfy the goal.
     */
    @Test
    public final void testAndCompositeOrder() {
        Mode mode = new Standard();
        // Setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)),
            new Exit(new Position(1, 1))
        );
        // Setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new CollectTreasure());
        // Check goal reached first
        Game game = new Game("test", entities, and, mode);
        List<String> expected = Arrays.asList(
            ":treasure(1) AND :exit(1)",
            ":exit(1) AND :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // Take treasure
        assertEquals(":exit(1)", game.tick(null, Direction.LEFT).getGoals());
        // Take exit
        assertEquals(":exit(1)", game.tick(null, Direction.RIGHT).getGoals());
        assertEquals("", game.tick(null, Direction.RIGHT).getGoals());
    }

    /**
     * Test composite or goal is satisfied when one of the child goals is
     * satisfied.
     * You do not have to collect the treasure to exit the dungeon and complete
     * the game.
     */
    @Test
    public final void testOrComposite1() {
        Mode mode = new Standard();
        // Setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)), // Go left to get Treasure
            new Exit(new Position(1, 1))
        );
        // Setup goals
        GoalComposite or = new OrComposite();
        or.addGoal(new ExitCondition());
        or.addGoal(new CollectTreasure());
        // Check goal reached first
        Game game = new Game("test", entities, or, mode);
        List<String> expected = Arrays.asList(
            ":treasure(1) OR :exit(1)",
            ":exit(1) OR :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // Take treasure
        assertEquals("", game.tick(null, Direction.LEFT).getGoals());
    }

    /**
     * Test composite or goal is satisfied when one of the child goals is
     * satisfied.
     *
     * Once you take the treasure you have won the game and the dungeon is completed
     * without the player having to reach the exit.
     */
    @Test
    public final void testOrComposite2() {
        Mode mode = new Standard();
        // Setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)),
            new Exit(new Position(1, 1)) //Go right to reach exit
        );
        // Setup goals
        GoalComposite or = new OrComposite();
        or.addGoal(new ExitCondition());
        or.addGoal(new CollectTreasure());
        // Check goal reached first
        Game game = new Game("test", entities, or, mode);
        List<String> expected = Arrays.asList(
            ":treasure(1) OR :exit(1)",
            ":exit(1) OR :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // Reach exit
        assertEquals("", game.tick(null, Direction.RIGHT).getGoals());
    }

    /**
     * Test that Portals does not have a goal
     */
    @Test
    public void testPortalsGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("portals", "Standard"));
        DungeonResponse stateOne = controller.tick(null, Direction.NONE);
        assertEquals(stateOne.getGoals(), "");
    }

    /**
     * This tests the completion of goals in the advanced dungeon
     */
    @Test
    public void testAdvancedGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        assertEquals(":treasure(1)", move(controller, Direction.DOWN, 3).getGoals());
        assertEquals(":treasure(1)", move(controller, Direction.RIGHT, 6).getGoals());
        assertEquals(":treasure(1)", move(controller, Direction.DOWN, 5).getGoals());
        assertEquals("", move(controller, Direction.DOWN, 1).getGoals());
    }

    /**
     * This tests the completion of boulders goal in the boulders dungeon
     */
    @Test
    public void testBouldersGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("boulders", "Standard"));
        assertEquals(":switch(6)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.UP, 1).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.RIGHT, 2).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.DOWN, 4).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.LEFT, 1).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.LEFT, 1).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.LEFT, 2).getGoals());
        assertEquals(":switch(6)", move(controller, Direction.UP, 1).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.UP, 1).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.DOWN, 2).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.RIGHT, 2).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.UP, 1).getGoals());
        assertEquals(":switch(5)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(4)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(4)", move(controller, Direction.UP, 4).getGoals());
        assertEquals(":switch(4)", move(controller, Direction.LEFT, 2).getGoals());
        assertEquals(":switch(3)", move(controller, Direction.LEFT, 1).getGoals());
        assertEquals(":switch(3)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(3)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":switch(2)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.RIGHT, 1).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.DOWN, 2).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.LEFT, 2).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.LEFT, 2).getGoals());
        assertEquals(":switch(1)", move(controller, Direction.UP, 1).getGoals());
        assertEquals("", move(controller, Direction.RIGHT, 1).getGoals());
    }

    /**
     * Checks the exit goal is satisfied in the maze dungeon
     */
    @Test
    public void testMazeGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("maze", "Standard"));
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 2).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 5).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 5).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 1).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 5).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 6).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 2).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 5).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 4).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.UP, 10).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.RIGHT, 4).getGoals());
        assertEquals(":exit(1)", move(controller, Direction.DOWN, 9).getGoals());
        assertEquals("", move(controller, Direction.DOWN, 1).getGoals());
    }
}
