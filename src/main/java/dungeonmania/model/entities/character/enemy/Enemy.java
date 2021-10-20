package dungeonmania.model.entities.character.enemy;

import dungeonmania.model.entities.character.Character;
import dungeonmania.model.entities.character.player.Player;
import dungeonmania.model.entities.character.Battle;

public abstract class Enemy extends Character implements Battle {

    @Override
    public void battle(Player player, Enemy enemy) {
        // TODO Auto-generated method stub
        
        // Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
    }

}
