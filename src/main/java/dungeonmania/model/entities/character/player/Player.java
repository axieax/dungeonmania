package dungeonmania.model.entities.character.player;

import dungeonmania.model.entities.character.Character;
import dungeonmania.model.entities.character.enemy.Enemy;
import dungeonmania.model.entities.character.Battle;

public class Player extends Character implements Battle {

    @Override
    public void battle(Player player, Enemy enemy) {
        // TODO Auto-generated method stub
        
        // Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
    }
}
