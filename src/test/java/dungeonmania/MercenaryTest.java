package dungeonmania;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class MercenaryTest {

    static final String CHARACTER_TYPE = "player";
    static final String DUNGEON_NAME = "advanced";
    static final String GAME_MODE = "peaceful";

    @Test
    public void testSpawnPosition() {
        // mercenaries spawn at the entry location periodically with at least one enemy
        // https://edstem.org/au/courses/7065/discussion/656701
        // although note that mercenaries can pre-exist in the world
        // and after this they periodically spawn
    }

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        // mercenaries only spawn in dungeons with at least one enemy
    }

    @Test
    public void testSimpleMovement() {
        // Distance between the mercenary and player should decrease per tick/movement
        Dungeon dungeon = new Dungeon(5, 5);

        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(new Player("player", player.getPosition()));

        Mercenary mercenary = new Mercenary("mercenary", new Position(3, 3));
        dungeon.addEntity(new Mercenary("mercenary", mercenary.getPosition()));

        assertTrue(dungeon.getEntity("player").getPosition().equals(new Position(1, 1)));
        assertTrue(dungeon.getEntity("mercenary").getPosition().equals(new Position(3, 3)));

        player.move(dungeon, Direction.RIGHT);

        // mercenary should move upwards
        assertTrue(mercenary.getY() < 4);
    }

    @Test
    public void testMercenarySimpleWall() {
        // Wall exists between player and mercenary and so mercenary should go around the wall
        Dungeon dungeon = new Dungeon(7, 7);

        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(new Player("player", player.getPosition()));

        // create horizontal wall with 1 gap near the right dungeon border between the player and mercenary
        for(int i = 0; i < 4; i ++) {
            dungeon.addEntity(new Wall("wall" + i, new Position(i + 1, 2)));
        }

        Mercenary mercenary = new Mercenary("mercenary", new Position(3, 1));
        dungeon.addEntity(new Mercenary("mercenary", mercenary.getPosition()));

        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 2);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 3);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 4);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 5);

        // mercenary now below wall gap
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getY() == 2);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getY() == 1);

        // mercenary now at same horizontal level as player and any further ticks reduce the horizontal distance
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 4);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 3);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 2);
        player.move(dungeon, Direction.NONE);
        assertTrue(mercenary.getX() == 1); // same position as player
    }

    @Test
    public void testBribedMercenaryMovement() {
        Dungeon dungeon = new Dungeon(7, 7);

        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(new Player("player", player.getPosition()));

        Mercenary mercenary = new Mercenary("mercenary", new Position(5, 1));
        dungeon.addEntity(new Mercenary("mercenary", mercenary.getPosition()));

        dungeon.addEntity(new Treasure("treasure_1", new Position(1, 2)));
        dungeon.addEntity(new Treasure("treasure_1", new Position(1, 3)));
        dungeon.addEntity(new Treasure("treasure_1", new Position(1, 4)));

        // make player collect all 3 coins
        player.move(dungeon, Direction.RIGHT);
        player.move(dungeon, Direction.RIGHT);
        player.move(dungeon, Direction.RIGHT);


        // TODO: Dungeon currently has no way to "interact" with an item
        // dungeon.interact("mercenary");
    }

    @Test
    public void testCannotMoveThroughExit() {

    }

    @Test
    public void testCannotMoveThroughClosedDoor() {}
}
