package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.Player;
import dungeonmania.model.entities.staticEntity.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class PortalTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Portal("portal1", new Position(1, 1), "BLUE"));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("portal1").getPosition()));
    }

    /**
     * Test if player is able to teleport through the corresponding Portal
     */
    @Test
    public void teleportSuccess() {
        Dungeon dungeon = new Dungeon(3, 3);

        Player player = new Player("player1", new Position(1, 0));

        Portal portalStart = new Portal("portal1", new Position(0,0), "BLUE");
        Portal portalEnd = new Portal("portal2", new Position(2,2), "BLUE");

        dungeon.addEntity(player);
        dungeon.addEntity(portalStart);
        dungeon.addEntity(portalEnd);

        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(0, 0).equals(portalStart.getPosition()));
        assertTrue(new Position(2, 2).equals(portalEnd.getPosition()));


        // player should teleport by moving into the portal
        player.move(Direction.LEFT);
        assertTrue(new Position(2, 2).equals(player.getPosition()));

        // return back to the portal
        player.move(Direction.LEFT);
        assertTrue(new Position(1, 2).equals(player.getPosition()));

        player.move(Direction.RIGHT);
        assertTrue(new Position(0, 0).equals(player.getPosition()));
    }

    /**
     * Test when their are several Portal with different colours.
     */
    @Test
    public void teleportMultiplePortals() {
        fail();
    }
}
