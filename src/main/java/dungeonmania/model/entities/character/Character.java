package dungeonmania.model.entities.character;

import dungeonmania.model.entities.GameEntity;
import dungeonmania.model.entities.character.Movement;
import dungeonmania.model.entities.character.enemy.Enemy;
import dungeonmania.model.entities.character.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Character extends GameEntity implements Movement {
    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }

    // The following the code can be used if we are not using a Battle interface
    // public abstract void battle(Player player, Enemy enemy);
}
