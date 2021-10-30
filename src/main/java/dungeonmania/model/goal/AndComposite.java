package dungeonmania.model.goal;

public class AndComposite extends GoalComposite {

    @Override
    public boolean evaluate() {
        return getGoals().stream().allMatch(Goal::evaluate);
    }
}
