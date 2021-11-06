package dungeonmania.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.Mercenary;
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
public class MercenaryTest {

    public static final String CHARACTER_TYPE = "player";
    public static final String DUNGEON_NAME = "advanced";
    public static final String GAME_MODE = "Peaceful";

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        Mode mode = new Standard();
        // mercenaries only spawn in dungeons with at least one enemy
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
        // Distance between the mercenary and player should decrease per tick/movement
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(3, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.tick("", Direction.RIGHT);

        // mercenary should move upwards or stay in the same horizontal line
        assertTrue(mercenary.getY() <= 3);
    }

    @Test
    public void testSimpleMovementFollowFar() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(1, 10), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.tick("", Direction.NONE);

        // mercenary should move to the left to follow the player
        assertTrue(mercenary.getPosition().equals(new Position(1, 9)));

        game.tick("", Direction.NONE);

        // mercenary should move to the left to follow the player
        assertTrue(mercenary.getPosition().equals(new Position(1, 8)));
    }

    @Test
    public void testSimpleMovementFollowBattle() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.tick("", Direction.NONE);

        // mercenary should battle player
        assertTrue(!game.getEntities().contains(mercenary) || !game.getEntities().contains(player));

    }

    @Test
    public void testMercenarySimpleWall() {
        Mode mode = new Standard();
        // Wall with 1 gap exists and mercenary should go directly to player, and not move
        // outside/go through the gap
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        // create horizontal wall with 1 gap near the right game border between the player and mercenary
        for(int i = 0; i < 4; i ++) {
            game.addEntity(new Wall(new Position(i + 1, 2)));
        }

        Mercenary mercenary = new Mercenary(new Position(4, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // mercenary now at same horizontal level as player and any further ticks reduce the horizontal distance
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 2);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 1); // same position as player but mercenary should be killed
    }

    @Test
    public void testBribedMercenaryDoesNotAttack() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new Treasure(new Position(1, 3)));
        game.addEntity(new Treasure(new Position(1, 4)));

        Position updatedPlayerPos = new Position(1, 4);
        
        // make player collect all 3 coins
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        while(!game.getAdjacentEntities(player.getPosition()).contains(mercenary)) {
            game.tick(null, Direction.NONE);
        }

        // mercenary in adjacent tile, so bribe
        int playerHealth = player.getHealth();

        game.interact(mercenary.getId());
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1); // player still at tile
        
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

        Mercenary mercenary = new Mercenary(new Position(1, 1), mode.damageMultiplier(),player);
        game.addEntity(mercenary);

        Position exitPos = new Position(1, 2);
        Exit exit = new Exit(exitPos);
        game.addEntity(exit);
        
        assertTrue(game.getEntities(exitPos).size() == 1);
        mercenary.moveTo(exitPos);
        assertTrue(game.getEntities(exitPos).size() == 2);
    }
    
    @Test
    public void testCannotMoveThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(5, 5);
        Player player = new Player(playerPos);
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(1, 1), mode.damageMultiplier(),player);
        game.addEntity(mercenary);

        // surround mercenary and door with wall
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(3, 2)));
        game.addEntity(new Wall(new Position(3, 1)));
    
        Position doorPos = new Position(2, 1);
        Door door = new Door(doorPos, 0);
        game.addEntity(door);
        
        assertTrue(game.getEntities(doorPos).size() == 1);
        
        // mercenary should not be able to go in the door position
        for(int i = 0; i < 100; i ++) {
            game.tick(null, Direction.NONE);   
            assertTrue(!game.getEntity(mercenary.getId()).getPosition().equals(doorPos));
        }
    }

    @Test
    public void testSimpleFight() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);
        
        Position mercenaryPos = new Position(2, 1);
        Mercenary mercenary = new Mercenary(mercenaryPos, mode.damageMultiplier(), player);
        game.addEntity(mercenary);
    
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(mercenaryPos).size() == 1);
        game.tick(null, Direction.NONE);

        // mercenary should move towards player, the two should fight and character should win
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(mercenaryPos).size() == 0); // mercenary should die
    }

    @Test
    public void testInteractMercenaryNotAdjacent() {
        // InvalidActionException if the player is not within 2 cardinal
        // tiles to the mercenary, if they are bribing
        Mode mode = new Standard();

        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);
        
        Position mercenaryPos = new Position(5, 5);
        Mercenary mercenary = new Mercenary(mercenaryPos, mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // mercenary too far away from character
        assertThrows(InvalidActionException.class, () -> game.interact(mercenary.getId()));
    }
    
    public void testBribeWithoutTreasure() {
        Mode mode = new Standard();
        // character attemps to bribe mercenary without any treasure should throw an exception
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // mercenary in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException.class, () -> game.interact("mercenary"));
    }

    private List<Entity> sevenBySevenWallBoundary() {
        ArrayList<Entity> wallBorder = new ArrayList<>();
        
        // left border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }
        
        // right border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // top border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // bottom border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }
}