package kuchtastefan.service;

import kuchtastefan.ability.Ability;
import kuchtastefan.characters.GameCharacter;
import kuchtastefan.characters.enemy.Enemy;
import kuchtastefan.characters.hero.Hero;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.utility.RandomNumberGenerator;

public class BattleService {
    public boolean battle(Hero hero, Enemy enemy) {
        boolean heroPlay = true;
        while (true) {
            int heroHealth = hero.getCurrentAbilityValue(Ability.HEALTH);
            int enemyHealth = enemy.getCurrentAbilityValue(Ability.HEALTH);

            System.out.println("Your healths: " + heroHealth);
            System.out.println("Enemy healths: " + enemyHealth);

            if (heroPlay) {
                battleRound(hero, enemy);
                heroPlay = false;
            } else {
                battleRound(enemy, hero);
                heroPlay = true;
            }

            if (enemy.getCurrentAbilityValue(Ability.HEALTH) <= 0) {
                return true;
            }

            if (hero.getCurrentAbilityValue(Ability.HEALTH) <= 0) {
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
        System.out.println(defender.getName() + " healths are: " + defender.getCurrentAbilityValue(Ability.HEALTH));
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
            minDamage = gameCharacter.getCurrentAbilityValue(Ability.ATTACK) +
                    ((Hero) gameCharacter).returnItemAbilityValue(Ability.ATTACK);
            maxDamage = minDamage
                    + gameCharacter.getCurrentAbilityValue(Ability.DEXTERITY)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEXTERITY)
                    + gameCharacter.getCurrentAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.SKILL);
        } else {
            minDamage = gameCharacter.getCurrentAbilityValue(Ability.ATTACK);
            maxDamage = gameCharacter.getCurrentAbilityValue(Ability.ATTACK)
                    + gameCharacter.getCurrentAbilityValue(Ability.DEXTERITY)
                    + gameCharacter.getCurrentAbilityValue(Ability.SKILL);
        }

        return RandomNumberGenerator.getRandomNumber(minDamage, maxDamage);
    }

    private int defense(GameCharacter gameCharacter) {
        int minDefence;
        int maxDefence;
        if (gameCharacter instanceof Hero) {
            minDefence = gameCharacter.getCurrentAbilityValue(Ability.DEFENCE)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEFENCE);
            maxDefence = minDefence
                    + gameCharacter.getCurrentAbilityValue(Ability.DEXTERITY)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEXTERITY);
        } else {
            minDefence = gameCharacter.getCurrentAbilityValue(Ability.DEFENCE);
            maxDefence = minDefence + gameCharacter.getCurrentAbilityValue(Ability.DEXTERITY);
        }

        return RandomNumberGenerator.getRandomNumber(minDefence, maxDefence);
    }

//    private int attack(GameCharacter gameCharacter) {
//        int minDamage;
//        int maxDamage;
//        if (gameCharacter instanceof Hero) {
//            minDamage = gameCharacter.getAbilityValue(Ability.ATTACK) +
//                    ((Hero) gameCharacter).returnItemAbilityValue(Ability.ATTACK);
//            maxDamage = minDamage
//                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
//                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEXTERITY)
//                    + gameCharacter.getAbilityValue(Ability.SKILL)
//                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.SKILL);
//        } else {
//            minDamage = gameCharacter.getAbilityValue(Ability.ATTACK);
//            maxDamage = gameCharacter.getAbilityValue(Ability.ATTACK)
//                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
//                    + gameCharacter.getAbilityValue(Ability.SKILL);
//        }
//
//        return RandomNumberGenerator.getRandomNumber(minDamage, maxDamage);
//    }
//
//    private int defense(GameCharacter gameCharacter) {
//        int minDefence;
//        int maxDefence;
//        if (gameCharacter instanceof Hero) {
//            minDefence = gameCharacter.getAbilityValue(Ability.DEFENCE)
//                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEFENCE);
//            maxDefence = minDefence
//                    + gameCharacter.getAbilityValue(Ability.DEXTERITY)
//                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.DEXTERITY);
//        } else {
//            minDefence = gameCharacter.getAbilityValue(Ability.DEFENCE);
//            maxDefence = minDefence + gameCharacter.getAbilityValue(Ability.DEXTERITY);
//        }
//
//        return RandomNumberGenerator.getRandomNumber(minDefence, maxDefence);
//    }

    private boolean criticalHit(GameCharacter gameCharacter) {
        int criticalHit;
        if (gameCharacter instanceof Hero) {
            criticalHit = gameCharacter.getCurrentAbilityValue(Ability.LUCK)
                    + gameCharacter.getCurrentAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.SKILL)
                    + ((Hero) gameCharacter).returnItemAbilityValue(Ability.LUCK);
        } else {
            criticalHit = gameCharacter.getCurrentAbilityValue(Ability.LUCK)
                    + gameCharacter.getCurrentAbilityValue(Ability.SKILL);
        }
        return criticalHit >= RandomNumberGenerator.getRandomNumber(0, 100);
    }
}
