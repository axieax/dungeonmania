package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.TestHelpers;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PotionControllerTest {
    @Test
    public void testInvincibilityPotionRunAway() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("potions", "standard");

        DungeonResponse resp = TestHelpers.tickMovement(dmc, Direction.RIGHT, 3);

        // assert player has collected potions
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "invincibility_potion"));
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "health_potion"));
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "invisibility_potion"));

        // mercenary should move to player
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 12)));

        // observe if mercenary is following player
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 11)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 10)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 9)));

        // now consume invincibility potion
        resp = dmc.tick(TestHelpers.getItemFromInventory(resp.getInventory(), "invincibility_potion").getId(), Direction.NONE);
        // mercenary should run away
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 10)));

        // potions last for two more ticks
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 11)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 12)));

        // once potion has been used, mercenary should revert to following player again
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 11)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 10)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 9)));
    }

    @Test
    public void testInvisibilityPotionRandomMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("potions", "standard");

        DungeonResponse resp = TestHelpers.tickMovement(dmc, Direction.RIGHT, 3);

        // assert player has collected potions
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "invincibility_potion"));
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "health_potion"));
        assertNotNull(TestHelpers.getItemFromInventory(resp.getInventory(), "invisibility_potion"));

        // mercenary should move to player
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 12)));

        // observe if mercenary is following player
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 11)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 10)));
        resp = dmc.tick(null, Direction.NONE);
        assertTrue(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 9)));

        // now consume invisibility potion
        resp = dmc.tick(TestHelpers.getItemFromInventory(resp.getInventory(), "invincibility_potion").getId(), Direction.NONE);

        // after 4 ticks, mercenary should have been making random movement
        resp = TestHelpers.tickMovement(dmc, Direction.NONE, 4);
        assertFalse(TestHelpers.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(3, 4)));
        
    }

}
