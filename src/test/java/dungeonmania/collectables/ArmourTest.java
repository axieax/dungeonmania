package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArmourTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Armour armour = new Armour(new Position(1, 1));
        game.addEntity(armour);

        assertTrue(new Position(1, 1).equals(game.getEntity(armour.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Armour item = new Armour(new Position(1, 1));
        game.addEntity(item);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(item.getId()) == null);
        assertTrue(player.getInventoryItem(item.getId()).equals(item));
    }

    /**
     * Test durability of Armour
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour item = new Armour(new Position(1, 1));
        game.addEntity(item);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        // Durability of armour when picked up should be 5
        assertTrue(item.getDurability() == 5);

        Mercenary mercenary = new Mercenary(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary
        // This will cause the durability of the armour to decrease by 1
        player.move(game, Direction.RIGHT);
        assertTrue(item.getDurability() == 4);
    }

    /**
     * Test armour protection for ZombieToast since zombies randomly spawn with armour.
     */
    @Test
    public void monsterArmourTest() {
        fail();
    }

    /**
     * Test if the Player can take the Armour from a defeated ZombieToast/Mercenary
     */
    @Test
    public void takeArmourTest() {
        fail();
    }
}