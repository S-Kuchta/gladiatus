package kuchtastefan.regions.events;

import kuchtastefan.characters.enemy.Enemy;
import kuchtastefan.characters.enemy.EnemyList;
import kuchtastefan.characters.hero.Hero;
import kuchtastefan.items.Item;
import kuchtastefan.regions.locations.LocationType;
import kuchtastefan.service.BattleService;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.RandomNumberGenerator;

import java.util.List;

public class CombatEvent extends Event {

    private final BattleService battleService;
    private final EnemyList enemyList;
    private final LocationType locationType;

    public CombatEvent(int eventLevel, EnemyList enemyList, LocationType locationType) {
        super(eventLevel);
        this.battleService = new BattleService();
        this.enemyList = enemyList;
        this.locationType = locationType;
    }

    @Override
    public void eventOccurs(Hero hero) {
        System.out.println("\tCombat event");


        List<Enemy> suitableEnemies = enemyList.returnEnemyListByLocationTypeAndLevel(this.locationType, this.eventLevel, null);
        int randomNumber = RandomNumberGenerator.getRandomNumber(0, suitableEnemies.size() - 1);
        Enemy randomEnemy = suitableEnemies.get(randomNumber);

        System.out.println("In the distance, you've caught sight of " + randomEnemy.getName() + " Will you attempt a silent evasion or initiate an attack?");
        System.out.println("0. Try to evasion");
        System.out.println("1. Attack");
        int choice = InputUtil.intScanner();

        switch (choice) {
            case 0 -> {
            }
            case 1 -> {
                if (battleService.isHeroReadyToBattle(hero, randomEnemy)) {
                    final boolean haveHeroWon = battleService.battle(hero, randomEnemy);
                    if (haveHeroWon) {
                        double goldEarn = randomEnemy.getGoldDrop();
                        for (Item item : randomEnemy.getItemsDrop()) {
                            hero.getHeroInventory().addItemWithNewCopyToItemList(item);
                            System.out.println("You loot " + item.getName());
                        }
                        System.out.println("You loot " + goldEarn + " golds");
                        hero.setHeroGold(hero.getHeroGold() + goldEarn);

                    }
                }
            }
        }

    }
}
