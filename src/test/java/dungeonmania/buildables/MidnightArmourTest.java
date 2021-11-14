package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.buildables.MidnightArmour;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class MidnightArmourTest {

    /**
     * Test whether the buildable entity can be built by the Player.
     */
    @Test
    public void buildTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a midnight armour, we need 1 armour and 1 sunstone
        Armour armour = new Armour(new Position(1, 0));
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(armour);
        game.addEntity(sunstone);

        // Player picks up both items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        assertTrue(player.findInventoryItem("midnight_armour") == null);

        // Player builds a midnight armour
        game.build("midnight_armour");

        // Check that the player has a midnight armour
        assertTrue(player.hasItemQuantity("midnight_armour", 1));

        // Check that the player has no armour or sunstone
        assertTrue(player.getInventoryItem(armour.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test whether the Midnight Armour can be built if there are zombies in the dungeon.
     */
    @Test
    public void builtTestZombies() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour armour = new Armour(new Position(1, 0));
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(armour);
        game.addEntity(sunstone);

        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        // Spawn a zombie
        ZombieToast zombie = new ZombieToast(new Position(0, 1), mode.damageMultiplier(), player);
        game.addEntity(zombie);

        // Player cannot build the midnight armour since there are zombies in the dungeon
        assertThrows(InvalidActionException.class, () -> game.build("midnight_armour"));
        assertTrue(player.findInventoryItem("midnight_armour") == null);

        // Player kills the zombie and should then be able to build the midnight armour
        player.move(game, Direction.LEFT);
        assertTrue(game.getEntity(zombie.getId()) == null);

        game.build("midnight_armour");

        assertTrue(player.hasItemQuantity("midnight_armour", 1));
        assertTrue(player.getInventoryItem(armour.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test durability of Midnight Armour.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour armour = new Armour(new Position(1, 0));
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(armour);
        game.addEntity(sunstone);

        // Player picks up both items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        game.build("midnight_armour");

        // Durability of midnight armour when built should be 5
        int initialDurability = 5;
        MidnightArmour midnightArmour = (MidnightArmour) player.findInventoryItem(
            "midnight_armour"
        );
        assertTrue(midnightArmour.getDurability() == initialDurability);

        Spider spider = new Spider(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(spider);

        // Player moves to defend against the spider with the midnight armour
        // Durability of midnight armour decreases by 1 each time it battles (within one tick)
        player.move(game, Direction.RIGHT);

        assertTrue(midnightArmour == null || midnightArmour.getDurability() != initialDurability);
    }

    /**
     * Test if Midnight Armour deals bonus attack damage.
     */
    @Test
    public void battleTest() {
        // Fight an ememy mercenary while the player has armour equipped
        // Fight an enemy mercenary while the player has midnight armour equipped
        // Enemy health should be less (more negative) in the latter case

        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour armour1 = new Armour(new Position(1, 0));
        game.addEntity(armour1);

        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);

        // Player picks up the armour
        player.move(game, Direction.RIGHT);

        Mercenary mercenary1 = new Mercenary(new Position(2, 0), mode.damageMultiplier(), player);
        game.addEntity(mercenary1);

        // Player moves to attack the mercenary with the armour
        player.move(game, Direction.RIGHT);

        // Mercenary should die upon battle - we record their remaining health
        assertTrue(game.getEntity(mercenary1.getId()) == null);
        int enemyHealthAttackedWithArmour = mercenary1.getHealth();

        // Player removes the armour from their inventory
        // Proceeds to collect the necessary items to craft midnight armour
        player.removeInventoryItem(armour1.getId());

        Armour armour2 = new Armour(new Position(2, 1));
        SunStone sunstone = new SunStone(new Position(2, 2));
        game.addEntity(armour2);
        game.addEntity(sunstone);

        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.build("midnight_armour");

        Mercenary mercenary2 = new Mercenary(new Position(2, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary2);

        // Player moves to attack the mercenary with the midnight armour
        player.move(game, Direction.DOWN);

        // Mercenary should die upon battle
        assertTrue(game.getEntity(mercenary2.getId()) == null);
        int enemyHealthAttackedWithMidnightArmour = mercenary2.getHealth();

        // Check that the mercenary has less health when attacked with midnight armour
        assertTrue(enemyHealthAttackedWithMidnightArmour < enemyHealthAttackedWithArmour);
    }
}
