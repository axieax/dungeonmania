package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerDefaultState;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.movings.player.PlayerInvisibleState;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class CharacterTest {

    static final String CHARACTER_TYPE = "player";
    static final String DUNGEON_NAME = "advanced";
    static final String GAME_MODE = "Peaceful";

    @Test
    public void testMovementIdempotence() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        // a tick without movement
        DungeonResponse response = controller.tick(null, Direction.NONE);

        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));
    }

    @Test
    public void testBasicMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        // move character down
        response = controller.tick(null, Direction.DOWN);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 2).equals(characterPos));

        // move character right
        response = controller.tick(null, Direction.RIGHT);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(2, 2).equals(characterPos));

        // move character up
        response = controller.tick(null, Direction.UP);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(2, 1).equals(characterPos));

        // move character left
        response = controller.tick(null, Direction.LEFT);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));
    }

    @Test
    public void testTopLeftCornerMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        // moving character left or up should not change position as character is already in the top left corner
        DungeonResponse response = controller.tick(null, Direction.LEFT);

        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        response = controller.tick(null, Direction.UP);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));
    }

    @Test
    public void testNonBorderWallMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        DungeonResponse response = controller.tick(null, Direction.DOWN);

        // Place character halfway down the left wall and ensure that it cannot move left
        for (int i = 0; i < 8; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        // Confirm character position
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 10).equals(characterPos));

        // Ensure character cannot move left into dungeon wall
        response = controller.tick(null, Direction.LEFT);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 10).equals(characterPos));

        // Ensure character cannot move into dungeon wall placed in middle of map
        response = controller.tick(null, Direction.RIGHT);
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(2, 10).equals(characterPos));

        // further movement attempts to the right result it the character staying in
        // the same position due to a non border wall
        for (int i = 0; i < 8; i++) {
            response = controller.tick(null, Direction.RIGHT);
            entities = response.getEntities();
            assertTrue(entities.size() > 0);

            characterPos = getCharacterPosition(entities);
            assertNotNull(characterPos);
            assertTrue(new Position(2, 10).equals(characterPos));
        }
    }

    @Test
    public void testMovementIntoSpaceWithEntity() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position is 1, 1

        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        // move the character 4 blocks to the right
        // in this position a sword entity will be to the right
        for (int i = 0; i < 4; i++) {
            response = controller.tick(null, Direction.RIGHT);
            int x = i + 2;

            // ensure character in correct position
            entities = response.getEntities();
            assertTrue(entities.size() > 0);

            characterPos = getCharacterPosition(entities);
            assertNotNull(characterPos);
            assertTrue(new Position(x, 1).equals(characterPos));
        }

        // one more movement to the right will result in character being on top of/using that entity
        assertDoesNotThrow(() -> controller.tick(null, Direction.RIGHT));

        // ensure character in correct position
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(5, 1).equals(characterPos));
    }

    @Test
    public void testMovementDoesNotAffectDungeonState() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1 - original response
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        // Check that as the character moves without interacting with any entities,
        // the dungeon response stays the same (except the position of the character)

        // character moves down - a new response is created
        DungeonResponse updatedResponse = controller.tick(null, Direction.DOWN);

        entities = updatedResponse.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 2).equals(characterPos));

        // compares two DungeonResponses and returns true if they are the same
        // NOTE: Comparison is only done on the actual state of the dungeon, and not
        //       on fields like dungeonId, dungeonName etc. The player position is also disregarded.

        // ensure entities are equal (except player)
        List<EntityResponse> responseEntities = response.getEntities();
        List<EntityResponse> updatedEntities = updatedResponse.getEntities();
        assertTrue(updatedEntities.size() > 0);
        assertEquals(responseEntities.size(), updatedEntities.size());

        for (int i = 0; i < updatedEntities.size(); i++) {
            // don't compare players as their position is different
            if (updatedEntities.get(i).getType() == CHARACTER_TYPE) {
                continue;
            }

            assertTrue(responseEntities.get(i).getId().equals(updatedEntities.get(i).getId()));
        }

        // ensure inventory remains unchanged
        List<ItemResponse> responseInventory = response.getInventory();
        List<ItemResponse> updatedInventory = updatedResponse.getInventory();
        assertEquals(responseInventory.size(), updatedInventory.size());

        for (int i = 0; i < updatedInventory.size(); i++) {
            assertTrue(responseInventory.get(i).equals(updatedInventory.get(i)));
        }

        // ensure buildables are the same
        List<String> responseBuildables = response.getBuildables();
        List<String> updatedBuildables = updatedResponse.getBuildables();

        assertEquals(responseBuildables.size(), updatedBuildables.size());
        for (int i = 0; i < updatedBuildables.size(); i++) {
            assertTrue(responseBuildables.get(i).equals(updatedBuildables.get(i)));
        }

        // ensure goals remain the same
        String responseGoals = response.getGoals();
        String updatedGoals = updatedResponse.getGoals();
        assertEquals(responseGoals, updatedGoals);
    }

    @Test
    public void testSavingAndLoadingGameMovement() {
        // saving a game and then loading it does not affect movement

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        // save the game
        assertDoesNotThrow(() -> controller.saveGame("1"));

        // load the game
        final DungeonManiaController innerController = controller;
        assertDoesNotThrow(
            () -> {
                // move down
                DungeonResponse loadedResponse = innerController.tick(null, Direction.DOWN);

                List<EntityResponse> loadedEntities = loadedResponse.getEntities();
                assertTrue(loadedEntities.size() > 0);

                Position loadedCharacterPos = getCharacterPosition(loadedEntities);
                assertNotNull(loadedCharacterPos);
                assertTrue(new Position(1, 2).equals(loadedCharacterPos));

                // move right
                loadedResponse = innerController.tick(null, Direction.RIGHT);

                loadedEntities = loadedResponse.getEntities();
                assertTrue(loadedEntities.size() > 0);

                loadedCharacterPos = getCharacterPosition(loadedEntities);
                assertNotNull(loadedCharacterPos);
                assertTrue(new Position(2, 2).equals(loadedCharacterPos));

                // move up
                loadedResponse = innerController.tick(null, Direction.UP);

                loadedEntities = loadedResponse.getEntities();
                assertTrue(loadedEntities.size() > 0);

                loadedCharacterPos = getCharacterPosition(loadedEntities);
                assertNotNull(loadedCharacterPos);
                assertTrue(new Position(2, 1).equals(loadedCharacterPos));

                // move left
                // move up
                loadedResponse = innerController.tick(null, Direction.LEFT);

                loadedEntities = loadedResponse.getEntities();
                assertTrue(loadedEntities.size() > 0);

                loadedCharacterPos = getCharacterPosition(loadedEntities);
                assertNotNull(loadedCharacterPos);
                assertTrue(new Position(1, 1).equals(loadedCharacterPos));
            }
        );
    }

    @Test
    public void testBuildDoesNotChangeCharacterPosition() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertTrue(new Position(1, 1).equals(characterPos));

        for (int i = 0; i < 13; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        entities = response.getEntities();
        characterPos = getCharacterPosition(entities);
        assertTrue(new Position(1, 14).equals(characterPos));
        
        for (int i = 0; i < 10; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        
        entities = response.getEntities();
        characterPos = getCharacterPosition(entities);
        assertTrue(new Position(11, 14).equals(characterPos));
        
        response = controller.tick(null, Direction.UP); // collect arrow
        response = controller.tick(null, Direction.DOWN);
        response = controller.tick(null, Direction.RIGHT); // collect arrow
        response = controller.tick(null, Direction.RIGHT); // collect wood
        
        entities = response.getEntities();
        characterPos = getCharacterPosition(entities);
        assertTrue(new Position(13, 14).equals(characterPos));

        // build bow - player has sufficient items
        response = assertDoesNotThrow(() -> controller.build("bow"));

        // position after building should be the same
        assertTrue(new Position(13, 14).equals(characterPos));
    }

    @Test
    public void testItemsUsedToCraftRemoved() {
        // any items that are used to craft another buildable entity should be
        // removed from the player's inventory, and are replaced with the built item
        Mode mode = new Peaceful();
        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        game.addEntity(new Wood(new Position(2, 1)));
        game.addEntity(new Arrow(new Position(3, 1)));
        game.addEntity(new Arrow(new Position(4, 1)));
        game.addEntity(new Arrow(new Position(5, 1)));

        assertTrue(player.getInventoryResponses().size() == 0);

        // player collects items
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getInventoryResponses().size() == 1);
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getInventoryResponses().size() == 2);
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getInventoryResponses().size() == 3);
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getInventoryResponses().size() == 4);

        // items should be used up
        game.build("bow");
        assertTrue(player.getInventoryResponses().size() == 1);
    }

    @Test
    public void testCharacterCannotPickUpBombsItPlaced() {
        // removed from the player's inventory, and are replaced with the built item
        Mode mode = new Peaceful();
        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Bomb bomb = new Bomb(new Position(2, 1));
        game.addEntity(bomb);

        assertTrue(player.getInventoryResponses().size() == 0);
        game.tick(null, Direction.RIGHT); // player picks up bomb
        assertTrue(player.getInventoryResponses().size() > 0);
        
        game.tick(null, Direction.DOWN);
        Position updatedPlayerPos = new Position(2, 2);
        
        game.tick(bomb.getId(), Direction.NONE); // place bomb
        assertTrue(player.getInventoryResponses().size() == 0);
        assertTrue(game.getEntities(updatedPlayerPos).size() == 2); // bomb + player

        game.tick(null, Direction.UP);
        updatedPlayerPos = new Position(2, 1);
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1); // player position

        assertTrue(game.getEntities(new Position(2, 2)).size() == 1); // bomb position

        // any attempt to move onto the block with the bomb fials
        game.tick(null, Direction.LEFT);
        assertTrue(game.getEntities(player.getPosition()).size() == 1); // player position
        assertTrue(game.getEntities(new Position(2, 2)).size() == 1); // bomb position
    }

    @Test
    public void testMovementDoesNotAffectHealth() {
        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            new Peaceful()
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        int playerHealth = player.getHealth();

        // move player
        game.tick(null, Direction.DOWN);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.UP);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.LEFT);
        assertTrue(player.getHealth() == playerHealth);
    }

    @Test
    public void testBattleReducesPlayerHealth() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        int initialPlayerHealth = player.getHealth();
        game.addEntity(player);

        Position mercenaryPos = new Position(2, 1);
        Mercenary mercenary = new Mercenary(mercenaryPos, mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.tick(null, Direction.NONE);

        // mercenary should move towards player and the two should fight,
        // with the player winning & its health reduced

        assertTrue(player.getHealth() < initialPlayerHealth);
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(mercenaryPos).size() == 0);
    }

    @Test
    public void testInvisibleState() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Position playerPos = new Position(1, 2);
        Player player = new Player(playerPos);
        int initialPlayerHealth = player.getHealth();

        game.addEntity(player);

        Position mercenaryPos = new Position(1, 5);
        Mercenary mercenary = new Mercenary(mercenaryPos, mode.damageMultiplier(), player);
        game.addEntity(mercenary);
        
        Position potionPos = new Position(2, 2);
        InvisibilityPotion potion = new InvisibilityPotion(potionPos);
        game.addEntity(potion);

        game.tick(null, Direction.RIGHT); // player picks up potion

        game.tick(potion.getId(), Direction.NONE); // drink potion
        assertTrue(player.getState() instanceof PlayerInvisibleState);
        
        assertTrue(player.getHealth() == initialPlayerHealth);
        
        // mercenary should not fight the player - until the effects of the potion have weared away
        while(!(player.getState() instanceof PlayerInvisibleState)) {
            game.tick(null, Direction.NONE);
            assertTrue(player.getHealth() == initialPlayerHealth);
        }
    }

    @Test
    public void testInvincibleState() {
        Mode mode = new Standard();
        
        Position playerPos = new Position(1, 2);
        Player player = new Player(playerPos);
        
        Position enemyPos = new Position(1, 5);
        List<MovingEntity> enemies = Arrays.asList(
            new Mercenary(enemyPos, mode.damageMultiplier(), player),
            new ZombieToast(enemyPos, mode.damageMultiplier(), player),
            new Hydra(enemyPos, mode.damageMultiplier(), player)
        );
            
        for(MovingEntity enemy: enemies) {
            Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
            player.setHealth(Player.MAX_CHARACTER_HEALTH);
            player.setPosition(playerPos);
            game.addEntity(player);
            game.addEntity(enemy);
            
            Position potionPos = new Position(2, 2);
            InvincibilityPotion potion = new InvincibilityPotion(potionPos);
            game.addEntity(potion);
    
            game.tick(null, Direction.RIGHT); // player picks up potion
    
            game.tick(potion.getId(), Direction.NONE); // drink potion
            assertTrue(player.getState() instanceof PlayerInvincibleState);
            
            assertTrue(player.getHealth() == Player.MAX_CHARACTER_HEALTH);
            // if the player was near the enemy when the player drank the potion,
            // in the next move it should not be in the adjacent tile
            assertTrue(game.getAdjacentEntities(potionPos).size() == 0);
            
            // enemy should not come near the player - until the effects of the potion have weared away
            // and so, the player's health should not reduce
            while(!(player.getState() instanceof PlayerInvincibleState)) {
                game.tick(null, Direction.NONE);
                assertTrue(player.getHealth() == Player.MAX_CHARACTER_HEALTH);
                assertTrue(game.getAdjacentEntities(potionPos).size() == 0);
            }
        }
    }

    @Test
    public void testInvincibilityPotionHasNoEffectInHardMode() {
        Mode mode = new Hard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Position playerPos = new Position(1, 2);
        Player player = new Player(playerPos);
        int initialPlayerHealth = player.getHealth();

        game.addEntity(player);

        Position potionPos = new Position(2, 2);
        InvincibilityPotion potion = new InvincibilityPotion(potionPos);
        game.addEntity(potion);

        // player state should not change at all in hard mode after drinkign invincibility potion
        assertTrue(player.getState() instanceof PlayerDefaultState);
        game.tick(null, Direction.RIGHT); // player picks up potion
        assertTrue(player.getState() instanceof PlayerDefaultState);
        game.tick(potion.getId(), Direction.NONE); // drink potion
        assertTrue(player.getState() instanceof PlayerDefaultState);
        
        
        assertTrue(player.getHealth() == initialPlayerHealth);
    }

    @Test
    public void testCanPickUpMultiplePotions() {
        Mode mode = new Peaceful();
        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(1, 2);
        Player player = new Player(playerPos);

        game.addEntity(player);

        Position invisPotionPos = new Position(2, 2);
        game.addEntity(new InvisibilityPotion(invisPotionPos));
        Position invincPotionPos = new Position(3, 2);
        game.addEntity(new InvincibilityPotion(invincPotionPos));

        assertTrue(player.getInventoryResponses().size() == 0);

        assertDoesNotThrow(
            () -> {
                game.tick(null, Direction.RIGHT);
                game.tick(null, Direction.RIGHT);
            }
        );

        assertTrue(player.getInventoryResponses().size() > 0);
    }

    @Test
    public void testCanDrinkTwoPotions() {
        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            new Peaceful()
        );

        Position playerPos = new Position(1, 2);
        Player player = new Player(playerPos);

        game.addEntity(player);

        Position invisPotionPos = new Position(2, 2);
        Potion invis = new InvisibilityPotion(invisPotionPos);
        game.addEntity(invis);
        Position invincPotionPos = new Position(3, 2);
        Potion invinc = new InvincibilityPotion(invincPotionPos);
        game.addEntity(invinc);

        game.tick(null, Direction.RIGHT); // player picks up invis potion
        game.tick(invis.getId(), Direction.NONE); // drinks poitoin

        game.tick(null, Direction.RIGHT); // player picks up invinc potion
        // player can drink two potions while still having the effect of another
        assertDoesNotThrow(() -> game.tick(invinc.getId(), Direction.NONE));
    }

    @Test
    public void testCharacterDies() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);

        assertDoesNotThrow(() -> {
            // attack player until it dies
            while(player != null) {
                // mercenaries should all attack character and character should die
                game.tick(null, Direction.NONE);
    
                Position mercenaryPos = new Position(1, 2);
                Mercenary mercenary = new Mercenary(mercenaryPos, mode.damageMultiplier(), player);
                game.addEntity(mercenary);
            }
        });
    }
    
    @Test
    public void testCharacterBattleWithAlly() {
        // fight an enemy mercenary while a player has an allied mercenary
        // fight an enemy mercenary without any allies
        // health remaining in the latter case should be smaller than in the former
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.addEntity(new Treasure(new Position(1, 2)));

        Position updatedPlayerPos = new Position(1, 4);
        
        // make player collect coin and move down twice
        game.tick(null, Direction.DOWN);
        game.tick(null, Direction.DOWN);
        game.tick(null, Direction.DOWN);

        while(!game.getAdjacentEntities(player.getPosition()).contains(mercenary)) {
            game.tick(null, Direction.NONE);
        }

        // mercenary in adjacent tile, so bribe
        game.interact(mercenary.getId());
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1); // player still at tile
        assertTrue(player.getAllies().size() > 0);        

        // spawn an enemy mercenary
        Mercenary enemyMercenary = new Mercenary(new Position(5, 5), mode.damageMultiplier(), player);
        game.addEntity(enemyMercenary);

        while(!game.getAdjacentEntities(player.getPosition()).contains(enemyMercenary)) {
            game.tick(null, Direction.NONE);
        }

        // enemy mercenary now adjacent
        game.tick(null, Direction.NONE);
        assertFalse(enemyMercenary.isAlive());
        int healthRemainingWithAlly = player.getHealth();

        // give player full health, remove allied mercenary, and fight enemy mercenary
        player.removeAlly(mercenary);
        game.removeEntity(mercenary);
        player.setHealth(Player.MAX_CHARACTER_HEALTH);

        Mercenary newEnemyMercenary = new Mercenary(new Position(5, 5), mode.damageMultiplier(), player);
        game.addEntity(newEnemyMercenary);
        while(!game.getCardinallyAdjacentEntities(player.getPosition()).contains(newEnemyMercenary)) {
            game.tick(null, Direction.NONE);
        }
        
        // new enemy mercenary now adjacent
        game.tick(null, Direction.NONE);
        assertFalse(newEnemyMercenary.isAlive());

        int healthRemainingWithoutAlly = player.getHealth();

        assertTrue(healthRemainingWithAlly > healthRemainingWithoutAlly);
    }

    public Position getCharacterPosition(List<EntityResponse> entities)
        throws IllegalArgumentException, InvalidActionException {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().startsWith(CHARACTER_TYPE))
            .findFirst()
            .orElse(null);

        if (player == null) return null;
        return player.getPosition();
    }

    private List<Entity> sevenBySevenWallBoundary() {
        ArrayList<Entity> wallBorder = new ArrayList<>();

        // left border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }

        // right border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // top border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // bottom border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }
}
