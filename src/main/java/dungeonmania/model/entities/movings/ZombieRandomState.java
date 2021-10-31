package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieRandomState implements ZombieState {
    private ZombieToast zombie;

    public ZombieRandomState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    @Override
    public void move(Game game) {
        Position currPos = zombie.getPosition();
        Set<Direction> chosen = new HashSet<>();
        
        // choose a direction (other than none)
        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
        );
        
        // choose a random direction
        while(chosen.size() != possibleDirections.size()) {
            Direction direction = possibleDirections.get((int) Math.random() % 4);
            chosen.add(direction);
            
            Position newPos = currPos.translateBy(direction);
            List<Entity> entitiesNewPos = game.getEntities(newPos);

            if(entitiesNewPos == null || zombie.canZombieMoveOntoPosition(entitiesNewPos)) {
                zombie.setPosition(newPos);
                return;
            }
        }

        // all 4 directions are blocked, do not move anywhere
    }
    
}