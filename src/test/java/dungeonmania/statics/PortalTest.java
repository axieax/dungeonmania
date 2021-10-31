package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class PortalTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Portal portal = new Portal(new Position(1, 1), "BLUE");
        game.addEntity(portal);

        assertTrue(new Position(1, 1).equals(game.getEntity(portal.getId()).getPosition()));
    }

    /**
     * Test if player is able to teleport through the corresponding portal.
     */
    @Test
    public void teleportSuccess() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());

        Player player = new Player(new Position(1, 0));

        Portal portalStart = new Portal(new Position(0,0), "BLUE");
        Portal portalEnd = new Portal(new Position(2,2), "BLUE");

        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);

        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(0, 0).equals(portalStart.getPosition()));
        assertTrue(new Position(2, 2).equals(portalEnd.getPosition()));


        // Player should teleport by moving into the portal
        // Note that since they are travelling to the left, they will end up to the left of the portal
        player.move(game, Direction.LEFT);
        assertTrue(new Position(1, 2).equals(player.getPosition()));

        // Return back to the portal
        // Note that since they are travelling to the right, they will end up to the right of the portal
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
    }

    /**
     * Test when there are several portals with different colours.
     */
    @Test
    public void teleportMultiplePortals() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());

        Player player = new Player(new Position(1, 0));

        Portal portal1 = new Portal(new Position(0,0), "BLUE");
        Portal portal2 = new Portal(new Position(2,2), "BLUE");
        Portal portal3 = new Portal(new Position(1,3), "RED");
        Portal portal4 = new Portal(new Position(3,1), "RED");

        game.addEntity(player);
        game.addEntity(portal1);
        game.addEntity(portal2);
        game.addEntity(portal3);
        game.addEntity(portal4);

        // P1  P   __  __
        // __  __  __  P4
        // __  __  P2  __
        // __  P3  __  __


        // Player teleports by moving into the blue portals (P1 -> P2)
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        player.move(game, Direction.LEFT);
        assertTrue(new Position(1, 2).equals(player.getPosition()));

        // Player teleports by moving into the red portals (P3 -> P4)
        player.move(game, Direction.DOWN);

        assertTrue(new Position(3, 2).equals(player.getPosition()));
    }
}
