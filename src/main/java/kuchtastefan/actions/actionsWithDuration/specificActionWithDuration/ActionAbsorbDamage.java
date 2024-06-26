package kuchtastefan.actions.actionsWithDuration.specificActionWithDuration;

import kuchtastefan.ability.Ability;
import kuchtastefan.actions.ActionEffectOn;
import kuchtastefan.actions.ActionName;
import kuchtastefan.actions.ActionStatusEffect;
import kuchtastefan.actions.actionValue.ActionWithIncreasedValueByPrimaryAbility;
import kuchtastefan.actions.actionsWithDuration.ActionWithDurationPerformedOnce;
import kuchtastefan.character.GameCharacter;
import kuchtastefan.utility.ConsoleColor;

public class ActionAbsorbDamage extends ActionWithDurationPerformedOnce implements ActionWithIncreasedValueByPrimaryAbility {

    public ActionAbsorbDamage(ActionName actionName, ActionEffectOn actionEffectOn, int maxActionValue, int maxActionTurns,
                              int actionMaxStacks, int chanceToPerformAction, ActionStatusEffect actionStatusEffect) {
        super(actionName, actionEffectOn, maxActionValue, maxActionTurns, actionMaxStacks, chanceToPerformAction, actionStatusEffect);
    }


    @Override
    public void performAction() {
        if (getCurrentActionTurn() == 0) {
            this.currentActionValue = this.returnFinalValue(charactersInvolvedInBattle.getSpellCaster()) * this.getActionCurrentStacks();
        }

        this.charactersInvolvedInBattle.getSpellTarget().increaseEffectiveAbilityValue(this.currentActionValue, Ability.ABSORB_DAMAGE);
    }

    @Override
    public void printActionPerforming() {
        System.out.println("\t" + charactersInvolvedInBattle.getSpellTarget().getName() + " shield is increased by " + this.currentActionValue);
    }

    @Override
    public void printActionDescription(GameCharacter spellCaster, GameCharacter spellTarget) {
        System.out.print("Absorb " + ConsoleColor.CYAN + this.returnActionValueRange(spellCaster).minimumValue() + ConsoleColor.RESET
                + " - " + ConsoleColor.CYAN + this.returnActionValueRange(spellCaster).maximumValue() + ConsoleColor.RESET + " incoming damage");
    }

    @Override
    public void printActiveAction() {
        System.out.println("\t" + this.charactersInvolvedInBattle.getSpellTarget() + " absorb damage shield is " + this.currentActionValue);
    }

    @Override
    public int returnPriorityPoints(GameCharacter spellCaster, GameCharacter spellTarget) {
        if (spellCaster.getEffectiveAbilityValue(Ability.HEALTH) < spellCaster.getEnhancedAbilities().get(Ability.HEALTH) / 3) {
            return 4;
        } else {
            return 2;
        }
    }

    @Override
    public void returnToDefaultValues() {
        this.charactersInvolvedInBattle.getSpellTarget().decreaseEffectiveAbilityValue(this.currentActionValue, Ability.ABSORB_DAMAGE);
    }
}
