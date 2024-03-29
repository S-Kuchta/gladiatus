package kuchtastefan.actions.actionsWIthDuration;

import kuchtastefan.actions.Action;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.actions.ActionName;
import kuchtastefan.character.GameCharacter;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class ActionWithDuration extends Action {

    private final int maxActionTurns;
    private int currentActionTurn;
    private final int actionMaxStacks;
    private int actionCurrentStacks;
    private final ActionDurationType actionDurationType;
    private final ActionStatusEffect actionStatusEffect;


    public ActionWithDuration(ActionName actionName, ActionEffectOn actionEffectOn, int maxActionValue, int maxActionTurns,
                              int actionMaxStacks, ActionDurationType actionDurationType, int chanceToPerformAction,
                              boolean canBeActionCriticalHit, ActionStatusEffect actionStatusEffect) {
        super(actionName, actionEffectOn, maxActionValue, chanceToPerformAction, canBeActionCriticalHit);
        this.maxActionTurns = maxActionTurns;
        this.actionStatusEffect = actionStatusEffect;
        this.currentActionTurn = 0;
        this.actionMaxStacks = actionMaxStacks;
        this.actionCurrentStacks = 0;
        this.actionDurationType = actionDurationType;
    }

    @Override
    public abstract void performAction(GameCharacter gameCharacter);

    public void addActionStack() {
        this.actionCurrentStacks++;
    }

    public void actionAddTurn() {
        this.currentActionTurn++;
    }

    public void actionCurrentTurnReset() {
        this.currentActionTurn = 0;
    }

    public boolean checkIfActionReachMaxActionTurns() {
        return this.maxActionTurns == this.currentActionTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionWithDuration that = (ActionWithDuration) o;
        return actionName.equals(that.actionName) && maxActionTurns == that.maxActionTurns && actionMaxStacks == that.actionMaxStacks && actionDurationType == that.actionDurationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxActionTurns, actionDurationType, actionMaxStacks);
    }
}
