package dungeonmania.model.goal;

public class OrComposite extends GoalComposite {

    @Override
    public boolean evaluate() {
        return getGoals().stream().anyMatch(Goal::evaluate);
    }
}
