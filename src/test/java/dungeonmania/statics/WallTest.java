package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class WallTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Wall("wall1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("wall1").getPosition()));
    }

    /**
     * Test if wall blocks movement of a player.
     */
    @Test
    public void wallBlockPlayer() {
        Dungeon dungeon = new Dungeon(5, 5);
        dungeon.addEntity(new Wall("wall1", new Position(0, 0)));
        dungeon.addEntity(new Wall("wall2", new Position(0, 1)));
        dungeon.addEntity(new Wall("wall3", new Position(0, 2)));
        dungeon.addEntity(new Wall("wall4", new Position(1, 0)));
        dungeon.addEntity(new Wall("wall5", new Position(1, 2)));
        dungeon.addEntity(new Wall("wall6", new Position(2, 0)));
        dungeon.addEntity(new Wall("wall7", new Position(2, 1)));
        dungeon.addEntity(new Wall("wall8", new Position(2, 2)));

        Player player = new Player("player1", new Position(1, 1));
        dungeon.addEntity(player);

        // If movement is blocked by wall, the player should remain in the same position

        player.move(Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(Direction.LEFT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(Direction.DOWN);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
    }

    /**
     * Test if wall blocks movement of enemies.
     */
    @Test
    public void wallBlockEnemies() {
        Dungeon dungeon = new Dungeon(5, 5);
        dungeon.addEntity(new Wall("wall1", new Position(0, 0)));
        dungeon.addEntity(new Wall("wall2", new Position(0, 1)));
        dungeon.addEntity(new Wall("wall3", new Position(0, 2)));
        dungeon.addEntity(new Wall("wall4", new Position(1, 0)));
        dungeon.addEntity(new Wall("wall5", new Position(1, 2)));
        dungeon.addEntity(new Wall("wall6", new Position(2, 0)));
        dungeon.addEntity(new Wall("wall7", new Position(2, 1)));
        dungeon.addEntity(new Wall("wall8", new Position(2, 2)));

        ZombieToast zombie = new ZombieToast("zombie1", new Position(1, 1));
        dungeon.addEntity(zombie);

        zombie.move(Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.move(Direction.LEFT);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.move(Direction.UP);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.move(Direction.DOWN);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));
    }

    /**
     * Test if wall blocks movement of moving boulders.
     */
    public void wallBlockBoulder() {
        Dungeon dungeon = new Dungeon(5, 5);
        dungeon.addEntity(new Wall("wall1", new Position(3, 0)));

        Boulder boulder = new Boulder("boulder1", new Position(1, 0));
        dungeon.addEntity(boulder);

        Player player = new Player("player1", new Position(0, 0));
        dungeon.addEntity(player);

        player.move(Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));

        // Wall blocks movement of boulder
        player.move(Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));
    }
}
