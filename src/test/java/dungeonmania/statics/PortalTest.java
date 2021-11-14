package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 0), mode.initialHealth());

        Portal portalStart = new Portal(new Position(0, 0), "BLUE");
        Portal portalEnd = new Portal(new Position(2, 2), "BLUE");

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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 0), mode.initialHealth());

        Portal portal1 = new Portal(new Position(0, 0), "BLUE");
        Portal portal2 = new Portal(new Position(2, 2), "BLUE");
        Portal portal3 = new Portal(new Position(1, 3), "RED");
        Portal portal4 = new Portal(new Position(3, 1), "RED");

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

    /**
     * Test teleport if there is no corresponding portal
     */
    @Test
    public void testNoPortal() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 0), mode.initialHealth());

        Portal portal = new Portal(new Position(0, 0), "BLUE");
        game.addEntity(player);
        game.addEntity(portal);

        // Player teleports by moving into the portal
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        player.move(game, Direction.LEFT);
        // player should stay on the same position - based on assumption
        assertTrue(new Position(1, 0).equals(player.getPosition()));
    }

    /**
     * Test teleport if the entity cannot pass through the destination.
     */
    @Test
    public void teleportWithBlockableEntity() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 0), mode.initialHealth());

        Portal portalStart = new Portal(new Position(0, 0), "BLUE");
        Portal portalEnd = new Portal(new Position(2, 2), "BLUE");
        Wall wall = new Wall(new Position(1, 2));

        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);
        game.addEntity(wall);

        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(0, 0).equals(portalStart.getPosition()));
        assertTrue(new Position(2, 2).equals(portalEnd.getPosition()));
        assertTrue(new Position(1, 2).equals(wall.getPosition()));

        // Player cannot teleport since a wall is blocking the teleport position
        player.move(game, Direction.LEFT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
    }

    /**
     * Test if a spider can teleport through a portal and resumes in a circular movement.
     */
    @Test
    public void testSpiderTeleport() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Player player = new Player(new Position(3, 6), mode.initialHealth());

        Portal portalStart = new Portal(new Position(5, 5), "BLUE");
        Portal portalEnd = new Portal(new Position(7, 5), "BLUE");
        Spider spider = new Spider(new Position(4, 5), mode.damageMultiplier(), player);
        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);
        game.addEntity(spider);

        // Spider move up
        game.tick(null, Direction.NONE);
        assertEquals(new Position(4, 4), spider.getPosition());

        // Spider move right
        game.tick(null, Direction.NONE);
        assertEquals(new Position(5, 4), spider.getPosition());

        // Spider goes through portal and move down
        game.tick(null, Direction.NONE);
        assertEquals(new Position(7, 6), spider.getPosition());

        // Spider moves down
        game.tick(null, Direction.NONE);
        assertEquals(new Position(7, 7), spider.getPosition());

        // Spider moves left
        game.tick(null, Direction.NONE);
        assertEquals(new Position(6, 7), spider.getPosition());
    }

    /**
     * Test if a spider can teleport through a portal and resumes in a circular movement.
     * Spider starts next to portal and moves up (initial direction) and continues in
     * a circular movement
     */
    @Test
    public void testSpiderTeleportStart() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Player player = new Player(new Position(3, 6), mode.initialHealth());

        Portal portalStart = new Portal(new Position(5, 5), "BLUE");
        Portal portalEnd = new Portal(new Position(7, 5), "BLUE");
        Spider spider = new Spider(new Position(5, 6), mode.damageMultiplier(), player);
        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);
        game.addEntity(spider);

        // Spider move up into the portal
        game.tick(null, Direction.NONE);
        assertEquals(new Position(7, 4), spider.getPosition());

        // Spider move right
        game.tick(null, Direction.NONE);
        assertEquals(new Position(8, 4), spider.getPosition());

        // Spider moves down
        game.tick(null, Direction.NONE);
        assertEquals(new Position(8, 5), spider.getPosition());

        // Spider moves down
        game.tick(null, Direction.NONE);
        assertEquals(new Position(8, 6), spider.getPosition());

        // Spider moves left
        game.tick(null, Direction.NONE);
        assertEquals(new Position(7, 6), spider.getPosition());
    }

    /**
     * Test if zombie is bounded and cannot teleport through a portal
     */
    @Test
    public void testZombieNoTeleport() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Player player = new Player(new Position(3, 6), mode.initialHealth());

        Portal portalStart = new Portal(new Position(5, 5), "BLUE");
        Portal portalEnd = new Portal(new Position(7, 5), "BLUE");
        // Spawn zombie next to portal
        ZombieToast zombie = new ZombieToast(new Position(4, 5), mode.damageMultiplier(), player);

        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);
        game.addEntity(zombie);

        // Create walls around zombie
        game.addEntity(new Wall(new Position(3, 5)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 6)));

        game.tick(null, Direction.NONE);
        // Zombies are not affected by portals
        assertEquals(new Position(4, 5), zombie.getPosition());
    }

    /**
     * Test if mercenary can teleport through portals.
     */
    @Test
    public void testMercenaryRunToPortal() {
        // Player consumes invincibility potion. Mercenary is in runstate.
        // Mercenary run to portal below.
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(3, 2), mode.initialHealth());
        InvincibilityPotion potion = new InvincibilityPotion(new Position(3, 6));
        player.addInventoryItem(potion);

        Portal portalStart = new Portal(new Position(3, 5), "BLUE");
        Portal portalEnd = new Portal(new Position(7, 5), "BLUE");
        // Spawn mercenary next to portal
        Mercenary mercenary = new Mercenary(new Position(3, 3), mode.damageMultiplier(), player);

        game.addEntity(player);
        game.addEntity(portalStart);
        game.addEntity(portalEnd);
        game.addEntity(mercenary);

        game.tick(potion.getId(), Direction.NONE);

        // Mercenary run away from player
        assertEquals(new Position(3, 4), mercenary.getPosition());
        assertTrue(mercenary.getMovementState() instanceof RunMovementState);

        game.tick(null, Direction.NONE);

        // Mercenary move to portal and teleports
        assertEquals(new Position(7, 6), mercenary.getPosition());
    }
}
