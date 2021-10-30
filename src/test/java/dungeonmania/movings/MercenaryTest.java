package dungeonmania.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.response.models.DungeonResponse;
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

    static final String CHARACTER_TYPE = "player";
    static final String DUNGEON_NAME = "advanced";
    static final String GAME_MODE = "peaceful";

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        // mercenaries only spawn in dungeons with at least one enemy
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());
        assertTrue(game.getEntities().size() == 28);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        int numEntities = game.getEntities().size();
        assertTrue(numEntities == 29);
        for(int i = 0; i < 200; i++) {
            assertTrue(game.getEntities().size() == numEntities);
        }
    }

    @Test
    public void testSimpleMovement() {
        // Distance between the mercenary and player should decrease per tick/movement
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(3, 3));
        game.addEntity(mercenary);

        assertTrue(game.getEntity("player").getPosition().equals(new Position(1, 1)));
        assertTrue(game.getEntity("mercenary").getPosition().equals(new Position(3, 3)));

        player.move(game, Direction.RIGHT);

        // mercenary should move upwards or stay in the same horizontal line
        assertTrue(mercenary.getY() <= 3);
    }

    @Test
    public void testMercenarySimpleWall() {
        // Wall exists between player and mercenary and so mercenary should go around the wall
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());


        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        // create horizontal wall with 1 gap near the right game border between the player and mercenary
        for(int i = 0; i < 4; i ++) {
            game.addEntity(new Wall(new Position(i + 1, 2)));
        }

        Mercenary mercenary = new Mercenary(new Position(1, 3));
        game.addEntity(mercenary);

        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 2);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 4);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 5);

        // mercenary now below wall gap
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getY() == 2);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getY() == 1);

        // mercenary now at same horizontal level as player and any further ticks reduce the horizontal distance
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 4);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 2);
        game.tick(null, Direction.NONE);
        assertTrue(mercenary.getX() == 1); // same position as player
    }

    @Test
    public void testBribedMercenaryMovement() {
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(5, 1));
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
        game.interact("mercenary");

        assertTrue(game.getEntities(updatedPlayerPos).size() == 1); // player still at tile
        game.tick(null, Direction.NONE);
        assertTrue(game.getEntities(updatedPlayerPos).size() == 2); // mercenary on top of player
        
        // any further movements result in the mercenary following the player
        game.tick(null, Direction.RIGHT);
        assertTrue(game.getEntities(player.getPosition()).size() == 2); // player still at tile

        game.tick(null, Direction.DOWN);
        assertTrue(game.getEntities(player.getPosition()).size() == 2); // player still at tile

        game.tick(null, Direction.UP);
        assertTrue(game.getEntities(player.getPosition()).size() == 2); // player still at tile
        
        game.tick(null, Direction.LEFT);
        assertTrue(game.getEntities(player.getPosition()).size() == 2); // player still at tile
    }

    @Test
    public void testCannotMoveThroughExit() {
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());

        Mercenary mercenary = new Mercenary(new Position(1, 1));
        game.addEntity(mercenary);

        Position exitPos = new Position(1, 2);
        Exit exit = new Exit(exitPos);
        
        assertTrue(game.getEntities(exitPos).size() == 1);
        mercenary.moveTo(exitPos);
        assertTrue(game.getEntities(exitPos).size() == 2);
    }
    
    @Test
    public void testCannotMoveThroughClosedDoor() {
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());
    
        Mercenary mercenary = new Mercenary(new Position(1, 1));
        game.addEntity(mercenary);
    
        Position doorPos = new Position(1, 2);
        Door door = new Door(doorPos, 0);
        
        assertTrue(game.getEntities(doorPos).size() == 1);

        // mercenary should not be able to move to the tile with the door as it is closed
        mercenary.moveTo(doorPos);
        assertTrue(game.getEntities(doorPos).size() == 1);
    }

    @Test
    public void testSimpleFight() {
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);
        
        Position mercenaryPos = new Position(2, 1);
        Mercenary mercenary = new Mercenary(mercenaryPos);
        game.addEntity(mercenary);
    
        game.tick(null, Direction.NONE);
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(mercenaryPos).size() == 1);

        // mercenary should move towards player, the two should fight and character should win
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(mercenaryPos).size() == 0); // mercenary should die
    }

    @Test
    public void testInteractMercenaryNotAdjacent() {
        // InvalidActionException if the player is not within 2 cardinal
        // tiles to the mercenary, if they are bribing

        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());
    
        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);
        
        Position mercenaryPos = new Position(5, 5);
        Mercenary mercenary = new Mercenary(mercenaryPos);
        game.addEntity(mercenary);

        // mercenary too far away from character
        assertThrows(InvalidActionException, () -> game.interact("mercenary"));
    }
    
    public void testBribeWithoutTreasure() {
        // character attemps to bribe mercenary without any treasure should throw an exception
        Game game = new Game("game", sevenBySevenWallBoundary(), new Goal(), new Peaceful());

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(2, 1));

        // mercenary in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException, () -> game.interact("mercenary"));
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
