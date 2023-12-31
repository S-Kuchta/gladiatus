package kuchtastefan.service;

import kuchtastefan.ability.Ability;
import kuchtastefan.domain.Enemy;
import kuchtastefan.domain.GameCharacter;
import kuchtastefan.domain.Hero;
import kuchtastefan.item.wearableItem.WearableItem;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.utility.RandomNumberGenerator;

import java.util.List;

public class BattleService {
    public boolean isHeroReadyToBattle(Hero hero, Enemy enemy, List<WearableItem> wearableItemList) {
        System.out.println(hero.getName() + " VS " + enemy.getName());
        PrintUtil.printCurrentAbilityPoints(hero);
        PrintUtil.printCurrentWearingArmor(hero);
        PrintUtil.printCurrentAbilityPointsWithItems(hero);
//        PrintUtil.printCurrentWearingArmor(hero, itemList);

        PrintUtil.printCurrentAbilityPoints(enemy);

        System.out.println("Are you ready to fight?");
        System.out.println("0. No");
        System.out.println("1. Yes");

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
                System.out.println("You have escaped from battle");
                return false;
            }
            case 1 -> {
                System.out.println("Let the battle begin");
                return true;
            }
            default -> {
                System.out.println("Invalid choice");
                return false;
            }
        }
    }

    public boolean battle(Hero hero, Enemy enemy) {
        boolean heroPlay = true;
        while (true) {
            int heroHealth = hero.getAbilityValue(Ability.HEALTH);
            int enemyHealth = enemy.getAbilityValue(Ability.HEALTH);

            System.out.println("Your healths: " + heroHealth);
            System.out.println("Enemy healths: " + enemyHealth);

            if (heroPlay) {
                battleRound(hero, enemy);
                heroPlay = false;
            } else {
                battleRound(enemy, hero);
                heroPlay = true;
            }

            if (enemy.getAbilityValue(Ability.HEALTH) <= 0) {
                return true;
            }

            if (hero.getAbilityValue(Ability.HEALTH) <= 0) {
                return false;
            }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void battleRound(GameCharacter attacker, GameCharacter defender) {
        int damage = 0;
        int finalDamage;

        if (criticalHit(attacker)) {
            System.out.println("Critical hit!");
            damage += (attack(attacker) * 2);
        } else {
            damage = attack(attacker);
        }

        finalDamage = finalDamage(damage, defense(defender));

        defender.receiveDamage(finalDamage);
        System.out.println(attacker.getName() + " attacked " + defender.getName() + " for " + finalDamage + " damage!");
        System.out.println(defender.getName() + " healths are: " + defender.getAbilityValue(Ability.HEALTH));
        PrintUtil.printDivider();
    }

    private int finalDamage(int damage, int defence) {
        int totalDamage = damage - defence;

        return Math.max(totalDamage, 0);
    }

    private int attack(GameCharacter gameCharacter) {
        int minDamage;
        int maxDamage;
        if (gameCharacter instanceof Hero) {
            minDamage = gameCharacter.getAbilityValue(Ability.ATTACK) +
                    ((Hero) gameCharacter).getItemAbilityValue(Ability.ATTACK);
            maxDamage = minDamage
                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.DEXTERITY)
                    + gameCharacter.getAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.SKILL);
        } else {
            minDamage = gameCharacter.getAbilityValue(Ability.ATTACK);
            maxDamage = gameCharacter.getAbilityValue(Ability.ATTACK)
                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
                    + gameCharacter.getAbilityValue(Ability.SKILL);
        }

        return RandomNumberGenerator.getRandomNumber(minDamage, maxDamage);
    }

    private int defense(GameCharacter gameCharacter) {
        int minDefence;
        int maxDefence;
        if (gameCharacter instanceof Hero) {
            minDefence = gameCharacter.getAbilityValue(Ability.DEFENCE)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.DEFENCE);
            maxDefence = minDefence
                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.DEXTERITY);
        } else {
            minDefence = gameCharacter.getAbilityValue(Ability.DEFENCE);
            maxDefence = minDefence + gameCharacter.getAbilityValue(Ability.DEXTERITY);
        }

        return RandomNumberGenerator.getRandomNumber(minDefence, maxDefence);
    }

    private boolean criticalHit(GameCharacter gameCharacter) {
        int criticalHit;
        if (gameCharacter instanceof Hero) {
            criticalHit = gameCharacter.getAbilityValue(Ability.LUCK)
                    + gameCharacter.getAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).getItemAbilityValue(Ability.LUCK);
        } else {
            criticalHit = gameCharacter.getAbilityValue(Ability.LUCK)
                    + gameCharacter.getAbilityValue(Ability.SKILL);
        }
        return criticalHit >= RandomNumberGenerator.getRandomNumber(0, 100);
    }
}
