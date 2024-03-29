package kuchtastefan.actions.instantActions;

import kuchtastefan.actions.Action;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.actions.ActionName;
import kuchtastefan.character.GameCharacter;
import kuchtastefan.utility.ConsoleColor;

public class ActionInstantStun extends Action {

    public ActionInstantStun(ActionName actionName, ActionEffectOn actionEffectOn,
                             int maxActionValue, int chanceToPerformAction, boolean canBeActionCriticalHit) {
        super(actionName, actionEffectOn, maxActionValue, chanceToPerformAction, canBeActionCriticalHit);
    }

    @Override
    public void performAction(GameCharacter gameCharacter) {
        gameCharacter.setCanPerformAction(false);
        System.out.println("\t" + ConsoleColor.YELLOW + gameCharacter.getName() + ConsoleColor.RESET + " is stunned!");
    }
}
