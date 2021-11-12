package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

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
        Player player = new Player(new Position(0, 0));
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

        Player player = new Player(new Position(0, 0));
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
        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        game.build("midnight_armour");

        // Durability of midnight armour when built should be 5
        int initialDurability = 5;
        MidnightArmour midnightArmour = (MidnightArmour) player.findInventoryItem("midnight_armour");
        assertTrue(midnightArmour.getDurability() == initialDurability);

        Spider spider = new Spider(new Position(2, 1), mode.damageMultiplier());
        game.addEntity(spider);

        // Player moves to defend against the spider with the midnight armour
        // Durability of midnight armour decreases by 1 each time it battles (within one tick)
        player.move(game, Direction.RIGHT);

        assertTrue(midnightArmour == null || midnightArmour.getDurability() != initialDurability);
    }

    /**
     * Test if Midnight Armour can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour armour = new Armour(new Position(1, 0));
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(armour);
        game.addEntity(sunstone);

        // Player picks up both items
        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        game.build("midnight_armour");

        int initialDurability = 5;
        MidnightArmour midnightArmour = (MidnightArmour) player.findInventoryItem("midnight_armour");
        assertTrue(midnightArmour.getDurability() == initialDurability);

        Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to attack the mercenary with the midnight armour
        player.move(game, Direction.DOWN);

        // Mercenary should die upon battle
        // Durability of midnight armour decreases by 1 each time it battles (within one tick)
        assertTrue(game.getEntity(mercenary.getId()) == null);
        assertTrue(midnightArmour == null || midnightArmour.getDurability() != initialDurability);
    }
}
