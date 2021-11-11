package dungeonmania.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Assassin;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class AssassinTest {

    public static final String CHARACTER_TYPE = "player";
    public static final String DUNGEON_NAME = "advanced";
    public static final String GAME_MODE = "Peaceful";

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        Mode mode = new Standard();
        // Assassins only spawn in dungeons with at least one enemy
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        int numEntities = game.getEntities().size();
        for(int i = 0; i < 200; i++) {
            assertTrue(game.getEntities().size() == numEntities);
        }
    }

    @Test
    public void testSimpleMovement() {
        Mode mode = new Standard();
        // Distance between the assassin and player should decrease per tick/movement
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(3, 3), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.RIGHT);

        // Assassin should move upwards or stay in the same horizontal line
        assertTrue(assassin.getY() <= 3);
    }

    @Test
    public void testSimpleMovementFollowFar() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 10), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.NONE);

        // Assassin should move to the left to follow the player
        assertTrue(assassin.getPosition().equals(new Position(1, 9)));

        game.tick(null, Direction.NONE);

        // Assassin should move to the left to follow the player
        assertTrue(assassin.getPosition().equals(new Position(1, 8)));
    }

    @Test
    public void testSimpleMovementFollowBattle() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.NONE);

        // Assassin should battle player
        assertTrue(!game.getEntities().contains(assassin) || !game.getEntities().contains(player));

    }

    @Test
    public void testAssassinSimpleWall() {
        Mode mode = new Standard();
        // Wall with 1 gap exists and assassin should go directly to player, and not move
        // outside/go through the gap
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        // Create horizontal wall with 1 gap near the right game border between the player and assassin
        for(int i = 0; i < 4; i ++) {
            game.addEntity(new Wall(new Position(i + 1, 2)));
        }

        Assassin assassin = new Assassin(new Position(4, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin now at same horizontal level as player and any further ticks reduce the horizontal distance
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 2);
        game.tick(null, Direction.NONE);
        // Same position as player but assassin should be killed
        assertTrue(assassin.getX() == 1);
    }

    @Test
    public void testBribeWithoutOneRing() {
        Mode mode = new Standard();
        // Character attemps to bribe assassin without TheOneRing should throw an exception
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        game.addEntity(new Treasure(new Position(1, 2)));
        
        // Player collects coin
        player.move(game, Direction.DOWN);

        Assassin assassin = new Assassin(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testBribeWithoutTreasure() {
        Mode mode = new Standard();
        // Character attemps to bribe assassin without treasure should throw an exception
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        game.addEntity(new TheOneRing(new Position(1, 2)));
        
        // Player collects TheOneRing
        player.move(game, Direction.DOWN);

        Assassin assassin = new Assassin(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testInteractAssassinNotAdjacent() {
        // InvalidActionException if the player is not within 2 cardinal
        // tiles to the assassin and they are bribing
        Mode mode = new Standard();

        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new TheOneRing(new Position(1, 3)));

        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        
        Position assassinPos = new Position(5, 5);
        Assassin assassin = new Assassin(assassinPos, mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin too far away from character
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testBribedAssassinDoesNotAttack() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new Treasure(new Position(1, 3)));
        game.addEntity(new Treasure(new Position(1, 4)));
        
        // Make player collect all 3 coins
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.addEntity(new TheOneRing(new Position(2,4)));

        // Make player collect TheOneRing
        player.move(game, Direction.RIGHT);

        Position updatedPlayerPos = new Position(2, 4);

        while(!game.getAdjacentEntities(player.getPosition()).contains(assassin)) {
            game.tick(null, Direction.NONE);
        }

        // Assassin in adjacent tile, so bribe
        int playerHealth = player.getHealth();

        game.interact(assassin.getId());
        // Player still at tile
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1);
        
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
    }

    @Test
    public void testCannotMoveThroughExit() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 1), mode.damageMultiplier(),player);
        game.addEntity(assassin);

        Position exitPos = new Position(1, 2);
        Exit exit = new Exit(exitPos);
        game.addEntity(exit);
        
        assertTrue(game.getEntities(exitPos).size() == 1);
        assassin.moveTo(exitPos);
        assertTrue(game.getEntities(exitPos).size() == 2);
    }
    
    @Test
    public void testCannotMoveThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(5, 5);
        Player player = new Player(playerPos);
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 1), mode.damageMultiplier(),player);
        game.addEntity(assassin);

        // Surround assassin and door with wall
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(3, 2)));
        game.addEntity(new Wall(new Position(3, 1)));
    
        Position doorPos = new Position(2, 1);
        Door door = new Door(doorPos, 0);
        game.addEntity(door);
        
        assertTrue(game.getEntities(doorPos).size() == 1);
        
        // Assassin should not be able to go in the door position
        for(int i = 0; i < 100; i ++) {
            game.tick(null, Direction.NONE);   
            assertTrue(!game.getEntity(assassin.getId()).getPosition().equals(doorPos));
        }
    }

    @Test
    public void testSimpleFight() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);
        
        Position assassinPos = new Position(2, 1);
        Assassin assassin = new Assassin(assassinPos, mode.damageMultiplier(), player);
        game.addEntity(assassin);
    
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(assassinPos).size() == 1);
        game.tick(null, Direction.NONE);

        // Assassin should move towards player, the two should fight and character should win
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(assassinPos).size() == 0); // assassin should die
    }

    @Test
    public void testMindControlledMovementAndAttack() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.addEntity(new Wood(new Position(2, 1)));
        game.addEntity(new Key(new Position(2, 2), 1));
        game.addEntity(new SunStone(new Position(1, 2)));

        // Player collects all items and builds a sceptre
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        game.build("sceptre");

        // Player uses the sceptre to mind control the assassin
        Position updatedPlayerPos = new Position(1, 2);
        game.interact(assassin.getId());

        // The distance between the player and the assassin should decrease
        int distance = assassin.getDistanceToPlayer(game, updatedPlayerPos);
        for (int i = 0; i < 10; i++) {
            game.tick(null, Direction.NONE);
            assertTrue(assassin.getDistanceToPlayer(game, updatedPlayerPos) <= distance);
            distance = assassin.getDistanceToPlayer(game, updatedPlayerPos);
        }

        Position assassinPos = assassin.getPosition();
        // After 10 ticks, the assassin will no longer be mind controlled
        // It will battle with the player and will consequently die
        game.tick(null, Direction.NONE);

        assertTrue(game.getEntities(assassinPos).size() == 0);
    }

    private List<Entity> sevenBySevenWallBoundary() {
        ArrayList<Entity> wallBorder = new ArrayList<>();
        
        // Left border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }
        
        // Right border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // Top border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // Bottom border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }
}