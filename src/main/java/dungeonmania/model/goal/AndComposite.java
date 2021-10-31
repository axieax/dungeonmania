package dungeonmania.model.goal;

import dungeonmania.model.Game;

public class AndComposite extends GoalComposite {

    @Override
    public boolean isComplete(Game game) {
        return getGoals().stream().allMatch(g -> g.isComplete(game));
    }
}
