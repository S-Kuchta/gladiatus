package kuchtastefan.actions.actionsWithDuration.specificActionWithDuration;

import kuchtastefan.ability.Ability;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.actions.ActionName;
import kuchtastefan.actions.ActionStatusEffect;
import kuchtastefan.actions.actionsWithDuration.ActionWithDuration;
import kuchtastefan.actions.actionValue.ActionWithIncreasedValueByPrimaryAbility;
import kuchtastefan.actions.criticalHit.CanBeCriticalHit;
import kuchtastefan.character.GameCharacter;
import kuchtastefan.utility.ConsoleColor;

public class ActionRestoreManaOverTime extends ActionWithDuration implements ActionWithIncreasedValueByPrimaryAbility, CanBeCriticalHit {

    public ActionRestoreManaOverTime(ActionName actionName, ActionEffectOn actionEffectOn, int maxActionValue, int baseActionValue,
                                     int actionMaxStacks, int chanceToPerformAction, ActionStatusEffect actionStatusEffect) {
        super(actionName, actionEffectOn, maxActionValue, baseActionValue, actionMaxStacks, chanceToPerformAction, actionStatusEffect);
    }

    @Override
    public void performAction() {
        this.currentActionValue = this.returnFinalValue(charactersInvolvedInBattle.getSpellCaster()) * this.getActionCurrentStacks();
        charactersInvolvedInBattle.getSpellTarget().restoreAbilityValue(this.currentActionValue, Ability.MANA);
    }

    @Override
    public void printActionPerforming() {
        System.out.println("\t" + charactersInvolvedInBattle.getSpellTarget().getName() + " restores " + ConsoleColor.RED + this.getCurrentActionValue() + ConsoleColor.RESET + " mana");
    }

    @Override
    public void printActionDescription(GameCharacter spellCaster, GameCharacter spellTarget) {
        System.out.print("Restore " + ConsoleColor.RED
                + (this.returnActionValueRange(spellCaster).minimumValue() * this.getMaxActionTurns())
                + ConsoleColor.RESET + " - " + ConsoleColor.RED
                + (this.returnActionValueRange(spellCaster).maximumValue() * this.getMaxActionTurns())
                + ConsoleColor.RESET
                + " Mana " + " over " + this.getMaxActionTurns() + " turns on "
                + ConsoleColor.YELLOW + this.returnTargetName(spellCaster, spellTarget) + ConsoleColor.RESET);
    }

    @Override
    public void printActiveAction() {
        System.out.println("\t" + this.charactersInvolvedInBattle.getSpellTarget().getName() + " restores " + ConsoleColor.RED + this.getCurrentActionValue() + ConsoleColor.RESET + " mana");
    }

    @Override
    public int returnPriorityPoints(GameCharacter spellCaster, GameCharacter spellTarget) {
        if (spellCaster.getEffectiveAbilityValue(Ability.MANA) < (spellCaster.getEnhancedAbilities().get(Ability.MANA) * 0.3)) {
            return 3;
        } else {
            return 2;
        }
    }
}
