package kuchtastefan.actions.instantActions;

import kuchtastefan.actions.Action;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.characters.GameCharacter;

public class ActionRestoreHealth extends Action {

    public ActionRestoreHealth(String actionName, ActionEffectOn actionEffectOn, int maxActionValue) {
        super(actionName, actionEffectOn, maxActionValue);
    }

    @Override
    public void performAction(GameCharacter gameCharacter) {
        gameCharacter.restoreHealth(this.getCurrentActionValue());
    }
}
