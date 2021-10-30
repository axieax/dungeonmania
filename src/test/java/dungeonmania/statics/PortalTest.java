package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class PortalTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Portal("portal1", new Position(1, 1), "BLUE"));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("portal1").getPosition()));
    }

    /**
     * Test if player is able to teleport through the corresponding portal.
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


        // Player should teleport by moving into the portal
        // Note that since they are travelling to the left, they will end up to the left of the portal
        player.move(dungeon, Direction.LEFT);
        assertTrue(new Position(1, 2).equals(player.getPosition()));

        // Return back to the portal
        // Note that since they are travelling to the right, they will end up to the right of the portal
        player.move(dungeon, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
    }

    /**
     * Test when there are several portals with different colours.
     */
    @Test
    public void teleportMultiplePortals() {
        Dungeon dungeon = new Dungeon(3, 3);

        Player player = new Player("player1", new Position(1, 0));

        Portal portal1 = new Portal("portal1", new Position(0,0), "BLUE");
        Portal portal2 = new Portal("portal1", new Position(2,2), "BLUE");
        Portal portal3 = new Portal("portal2", new Position(1,3), "RED");
        Portal portal4 = new Portal("portal2", new Position(3,1), "RED");

        dungeon.addEntity(player);
        dungeon.addEntity(portal1);
        dungeon.addEntity(portal2);
        dungeon.addEntity(portal3);
        dungeon.addEntity(portal4);

        // P1  P   __  __
        // __  __  __  P4
        // __  __  P2  __
        // __  P3  __  __


        // Player teleports by moving into the blue portals (P1 -> P2)
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        player.move(dungeon, Direction.LEFT);
        assertTrue(new Position(1, 2).equals(player.getPosition()));

        // Player teleports by moving into the red portals (P3 -> P4)
        player.move(dungeon, Direction.DOWN);

        assertTrue(new Position(3, 2).equals(player.getPosition()));
    }
}
