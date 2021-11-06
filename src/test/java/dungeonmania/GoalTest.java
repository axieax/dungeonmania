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
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
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
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        dmc.newGame("boulders", "Standard");
        // navigate the maze
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
        // navigate the maze
        String switchGoal = ":switch(6)";
        // pick up the bomb
        DungeonResponse resp = move(dmc, Direction.RIGHT, 1);
        assertEquals(switchGoal, resp.getGoals());
        assertEquals("bomb", resp.getInventory().get(0).getPrefix());
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
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getPrefix().equals("bomb")));

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
        Game game = new Game("test", entities, new ExitCondition(), new Standard());
        assertEquals(":exit(1)", game.tick(null, Direction.DOWN).getGoals());
        assertEquals("", game.tick(null, Direction.DOWN).getGoals());
    }

    @Test
    public final void testSimpleBoulders() {
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            // setup boulder to be pushed onto switch
            new Boulder(new Position(0, 2)),
            new FloorSwitch(new Position(0, 3)),
            // boulder already on switch
            new Boulder(new Position(1, 0)),
            new FloorSwitch(new Position(1, 0))
        );
        Game game = new Game("test", entities, new ToggleSwitch(), new Standard());
        assertEquals(":switch(1)", game.tick(null, Direction.DOWN).getGoals());
        assertEquals("", game.tick(null, Direction.DOWN).getGoals());
    }

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

    @Test
    public final void testSimpleEnemiesMercenaryBribed() {
        Mode mode = new Standard();
        Player player = new Player(new Position(0, 0));
        Entity mercenary = new Mercenary(new Position(0, 4), mode.damageMultiplier(), player);
        List<Entity> entities = Arrays.asList(player, new Treasure(new Position(0, 1)), mercenary);
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        // pick up treasure
        assertEquals(":enemies(1)", game.tick(null, Direction.DOWN).getGoals());
        // bribe mercenary
        assertEquals("", game.interact(mercenary.getId()).getGoals());
    }

    @Test
    public final void testSimpleEnemiesSpider() {
        Mode mode = new Standard();
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Spider(new Position(0, -2))
        );
        Game game = new Game("test", entities, new DestroyEnemies(), mode);
        assertEquals(":enemies(1)", game.tick(null, Direction.UP).getGoals());
        // spider moves up
        assertEquals("", game.tick(null, Direction.UP).getGoals());
    }

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
        // zombie toast moves up
        assertEquals("", game.tick(null, Direction.UP).getGoals());
    }

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
        Game game = new Game("test", entities, and, new Standard());
        List<String> expected = Arrays.asList(":exit(1) AND :switch(1)", ":switch(1) AND :exit(1)");
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // move onto exit
        assertEquals(":switch(1)", game.tick(null, Direction.DOWN).getGoals());
        // move off exit
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // push boulder onto switch
        assertEquals(":exit(1)", game.tick(null, Direction.DOWN).getGoals());
        // move down
        assertEquals(":exit(1)", game.tick(null, Direction.UP).getGoals());
        // all goals satisfied
        assertEquals("", game.tick(null, Direction.UP).getGoals());
    }

    @Test
    public final void testAndCompositeOrder() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)),
            new Exit(new Position(1, 1))
        );
        // setup goals
        GoalComposite and = new AndComposite();
        and.addGoal(new ExitCondition());
        and.addGoal(new CollectTreasure());
        // check goal reached first
        Game game = new Game("test", entities, and, new Standard());
        List<String> expected = Arrays.asList(
            ":treasure(1) AND :exit(1)",
            ":exit(1) AND :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // take treasure
        assertEquals(":exit(1)", game.tick(null, Direction.LEFT).getGoals());
        // take exit
        assertEquals(":exit(1)", game.tick(null, Direction.RIGHT).getGoals());
        assertEquals("", game.tick(null, Direction.RIGHT).getGoals());
    }

    @Test
    public final void testOrComposite1() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)), // go left to get Treasure
            new Exit(new Position(1, 1))
        );
        // setup goals
        GoalComposite or = new OrComposite();
        or.addGoal(new ExitCondition());
        or.addGoal(new CollectTreasure());
        // check goal reached first
        Game game = new Game("test", entities, or, new Standard());
        List<String> expected = Arrays.asList(
            ":treasure(1) OR :exit(1)",
            ":exit(1) OR :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // take treasure
        assertEquals("", game.tick(null, Direction.LEFT).getGoals());
    }

    @Test
    public final void testOrComposite2() {
        // setup entities
        List<Entity> entities = Arrays.asList(
            new Player(new Position(0, 0)),
            new Treasure(new Position(-1, 1)),
            new Exit(new Position(1, 1)) // go right to reach exit
        );
        // setup goals
        GoalComposite or = new OrComposite();
        or.addGoal(new ExitCondition());
        or.addGoal(new CollectTreasure());
        // check goal reached first
        Game game = new Game("test", entities, or, new Standard());
        List<String> expected = Arrays.asList(
            ":treasure(1) OR :exit(1)",
            ":exit(1) OR :treasure(1)"
        );
        assertTrue(expected.contains(game.tick(null, Direction.DOWN).getGoals()));
        // reach exit
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

    /*
    @Test
    public void testAdvancedGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        List<String> expected = Arrays.asList(
            ":enemies(1) AND :treasure(1)",
            ":treasure(1) AND :enemies(1)"
        );
        assertTrue(expected.contains(move(controller, Direction.DOWN, 1).getGoals()));
        // mercenary should have died
        assertEquals(":treasure(1)", move(controller, Direction.DOWN, 7).getGoals());
        assertEquals(":treasure(1)", move(controller, Direction.RIGHT, 6).getGoals());
        assertEquals("", move(controller, Direction.DOWN, 1).getGoals());
    } */

    /**
     * This tests the completion of goals in the advanced dungeon
     */
    @Test
    public void testAdvancedGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        DungeonResponse vanquished = controller.tick (null, Direction.DOWN);
        assertEquals(":treasure(1)", vanquished.getGoals());

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        
        DungeonResponse secondState = controller.tick (null, Direction.DOWN);
        assertEquals(":treasure(1)", secondState.getGoals());
        
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN); //Got treasure
    
        DungeonResponse thirdState = controller.tick (null, Direction.DOWN);
        assertEquals(thirdState.getGoals(), ""); 
    }

    /**
     * This tests the completion of boulders goal in the boulders dungeon
     */
    @Test 
    public void testBouldersGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("boulders", "Standard"));
    

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);

        DungeonResponse stateOne = controller.tick (null, Direction.UP);
        assertEquals(":switch(1)", stateOne.getGoals());    


        DungeonResponse stateTwo = controller.tick (null, Direction.RIGHT);
        assertEquals(stateTwo.getGoals(), ""); // game is won   
    }

    /**
     * Checks the exit goal is satisfied in the maze dungeon
     */
    @Test 
    public void testMazeGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("maze", "Standard"));

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
    
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
    
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        DungeonResponse stateOne = controller.tick (null, Direction.DOWN);
        assertEquals(":exit(1)", stateOne.getGoals());  

        DungeonResponse stateTwo = controller.tick (null, Direction.DOWN);
        assertEquals(stateTwo.getGoals(), "");  
    }

}
