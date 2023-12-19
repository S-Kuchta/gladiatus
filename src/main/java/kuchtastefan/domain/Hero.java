package kuchtastefan.domain;

import kuchtastefan.ability.Ability;
import kuchtastefan.constant.Constant;

import java.util.HashMap;
import java.util.Map;

public class Hero extends GameCharacter {

    private int unspentAbilityPoints;

    public Hero(String name) {
        super(name, new HashMap<>());
        this.abilities = this.getInitialAbilities();
        this.unspentAbilityPoints = Constant.INITIAL_ABILITY_POINTS;
    }

    private Map<Ability, Integer> getInitialAbilities() {
        return new HashMap<>(Map.of(
                Ability.ATTACK, 1,
                Ability.DEFENCE, 1,
                Ability.DEXTERITY, 1,
                Ability.SKILL, 1,
                Ability.LUCK, 1,
                Ability.HEALTH, 50
        ));
    }

    public void setNewAbilitiesPoints(Ability ability, int pointsToChange, int heroAvailablePointsChange) {
        int minimumPoints = 1;
        if (ability.equals(Ability.HEALTH)) {
            minimumPoints = 50;
        }

        int tempAbilityPoints = this.abilities.get(ability) + pointsToChange;
        if (tempAbilityPoints < minimumPoints) {
            System.out.println("You don't have enough points!");
        } else {
            if(tempAbilityPoints < this.abilities.get(ability)) {
                System.out.println("You have removed 1 point from " + ability.name());
            }
            if (ability.equals(Ability.HEALTH)) {
                this.abilities.put(ability, this.abilities.get(ability) + pointsToChange * Constant.HEALTH_OF_ONE_POINT);
            } else {
                this.abilities.put(ability, this.abilities.get(ability) + pointsToChange);
            }

            updateAbilityPoints(heroAvailablePointsChange);
        }
    }

    public void putNewAbilityToMap(Ability ability, int value) {
        this.abilities.put(ability, value);
    }

    public void updateAbilityPoints(int numberOfPoints) {
        this.unspentAbilityPoints += numberOfPoints;
    }

    public int getUnspentAbilityPoints() {
        return unspentAbilityPoints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbilities(Map<Ability, Integer> abilities) {
        this.abilities = abilities;
    }

    public void setUnspentAbilityPoints(int unspentAbilityPoints) {
        this.unspentAbilityPoints = unspentAbilityPoints;
    }
}
