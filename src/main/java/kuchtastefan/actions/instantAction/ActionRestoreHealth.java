package kuchtastefan.actions.instantAction;

import kuchtastefan.ability.Ability;
import kuchtastefan.actions.Action;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.actions.ActionName;
import kuchtastefan.actions.actionValue.ActionWithIncreasedValueByPrimaryAbility;
import kuchtastefan.actions.criticalHit.CanBeCriticalHit;
import kuchtastefan.character.GameCharacter;
import kuchtastefan.utility.ConsoleColor;

public class ActionRestoreHealth extends Action implements ActionWithIncreasedValueByPrimaryAbility, CanBeCriticalHit {

    public ActionRestoreHealth(ActionName actionName, ActionEffectOn actionEffectOn, int baseActionValue, int chanceToPerformAction) {
        super(actionName, actionEffectOn, baseActionValue, chanceToPerformAction);
    }

    @Override
    public void performAction() {
        this.currentActionValue = this.returnFinalValue(charactersInvolvedInBattle.getSpellCaster());
        charactersInvolvedInBattle.getSpellTarget().restoreAbilityValue(this.getCurrentActionValue(), Ability.HEALTH);
    }

    @Override
    public void printActionDescription(GameCharacter spellCaster, GameCharacter spellTarget) {
        System.out.print("Restore "
                + ConsoleColor.RED + this.returnActionValueRange(spellCaster).minimumValue() + ConsoleColor.RESET
                +  " - " + ConsoleColor.RED + this.returnActionValueRange(spellCaster).maximumValue() + ConsoleColor.RESET
                + " of " + ConsoleColor.YELLOW + returnTargetName(spellCaster, spellTarget) + ConsoleColor.RESET
                + " Health");
    }

    @Override
    public int returnPriorityPoints(GameCharacter spellCaster, GameCharacter spellTarget) {
        if (spellCaster.getEffectiveAbilityValue(Ability.HEALTH) < spellCaster.getEnhancedAbilities().get(Ability.HEALTH) / 2) {
            return 2;
        } else if (spellCaster.getEffectiveAbilityValue(Ability.HEALTH) < spellCaster.getEnhancedAbilities().get(Ability.HEALTH) / 3) {
            return 4;
        } else {
            return 1;
        }
    }
}
