package dungeonmania.model.goal;

import dungeonmania.model.Game;

public class OrComposite extends GoalComposite {

    @Override
    public boolean isComplete(Game game) {
        return getGoals().stream().anyMatch(g -> g.isComplete(game));
    }
}
