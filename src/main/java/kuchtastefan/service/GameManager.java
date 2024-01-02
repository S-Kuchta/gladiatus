package kuchtastefan.service;

import kuchtastefan.ability.Ability;
import kuchtastefan.ability.HeroAbilityManager;
import kuchtastefan.constant.Constant;
import kuchtastefan.domain.Enemy;
import kuchtastefan.domain.GameLoaded;
import kuchtastefan.domain.Hero;
import kuchtastefan.domain.vendor.WearableItemVendorCharacter;
import kuchtastefan.hint.HintName;
import kuchtastefan.hint.HintUtil;
import kuchtastefan.item.Item;
import kuchtastefan.item.ItemsLists;
import kuchtastefan.item.wearableItem.WearableItem;
import kuchtastefan.item.wearableItem.WearableItemType;
import kuchtastefan.utility.EnemyGenerator;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;

import java.util.*;

public class GameManager {
    private Hero hero;
    private final HeroAbilityManager heroAbilityManager;
    private int currentLevel;
    private final FileService fileService;
    private final Map<Integer, Enemy> enemiesByLevel;
    private final BattleService battleService;
    private final ItemsLists itemsLists;
    private List<WearableItem> wearableItems = new ArrayList<>();
    private final BlacksmithService blacksmithService;
    private final InventoryService inventoryService;
    private final ShopService shopService;
    private final HintUtil hintUtil;
    private WearableItemVendorCharacter wearableItemVendorCharacter;

    public GameManager() {
        this.hero = new Hero("");
        this.currentLevel = Constant.INITIAL_LEVEL;
        this.fileService = new FileService();
        this.battleService = new BattleService();
        this.enemiesByLevel = EnemyGenerator.createEnemies();
        this.heroAbilityManager = new HeroAbilityManager(hero);
        this.itemsLists = new ItemsLists(new ArrayList<>(), new ArrayList<>());
        this.inventoryService = new InventoryService();
        this.shopService = new ShopService();
        this.blacksmithService = new BlacksmithService();
        this.hintUtil = new HintUtil(new HashMap<>());
    }

    private Map<Ability, Integer> getInitialAbilityPoints() {
        return new HashMap<>(Map.of(
                Ability.ATTACK, 1,
                Ability.DEFENCE, 1,
                Ability.DEXTERITY, 1,
                Ability.SKILL, 1,
                Ability.LUCK, 1,
                Ability.HEALTH, 50
        ));
    }

    public void startGame() {
        this.initGame();

        while (this.currentLevel <= this.enemiesByLevel.size()) {
            final Enemy enemy = this.enemiesByLevel.get(this.currentLevel);
            System.out.println("\t0. Fight " + enemy.getName() + " (level " + this.currentLevel + ")");
            System.out.println("\t1. Upgrade abilities (" + this.hero.getUnspentAbilityPoints() + " points to spend)");
            System.out.println("\t2. Save game");
            System.out.println("\t3. Exit game");

            System.out.println("\t5. Inventory");
            System.out.println("\t6. Market");
            System.out.println("\t7. Blacksmith");

            final int choice = InputUtil.intScanner();
            switch (choice) {
                case 0 -> {
                    if (this.battleService.isHeroReadyToBattle(this.hero, enemy, this.wearableItems)) {
                        final int heroHealthBeforeBattle = this.hero.getAbilities().get(Ability.HEALTH);
                        final boolean haveHeroWon = battleService.battle(this.hero, enemy);
                        if (haveHeroWon) {
                            PrintUtil.printDivider();
                            System.out.println("You have won this battle! You have gained " + this.currentLevel + " points to spend");
                            this.hero.updateAbilityPoints(this.currentLevel);
                            this.hero.setHeroGold(50 * this.currentLevel);
                            this.currentLevel++;
                        } else {
                            System.out.println("You have lost!");
                        }

                        this.hero.setAbility(Ability.HEALTH, heroHealthBeforeBattle);
                        System.out.println("You have full health now!");
                        PrintUtil.printDivider();
                    }
                }
                case 1 -> this.upgradeAbilityMenu();
                case 2 -> fileService.saveGame(this.hero, this.currentLevel, this.hintUtil.getHintList());
                case 3 -> {
                    System.out.println("Are you sure?");
                    System.out.println("0. No");
                    System.out.println("1. Yes");
                    final int exitChoice = InputUtil.intScanner();
                    if (exitChoice == 0) {
                        continue;
                    }
                    if (exitChoice == 1) {
                        System.out.println("Bye");
                        return;
                    }
                }
                case 4 -> {

                    // TESTING
                    WearableItem item = new WearableItem("item",10, WearableItemType.BODY, Map.of(
                            Ability.ATTACK, 1,
                            Ability.DEFENCE, 1,
                            Ability.DEXTERITY, 1,
                            Ability.SKILL, 1,
                            Ability.LUCK, 1,
                            Ability.HEALTH, 50
                    ), 1);

                    WearableItem item1 = new WearableItem("item1",15, WearableItemType.HANDS, Map.of(
                            Ability.ATTACK, 2,
                            Ability.DEFENCE, 2,
                            Ability.DEXTERITY, 2,
                            Ability.SKILL, 2,
                            Ability.LUCK, 2,
                            Ability.HEALTH, 20
                    ), 2);
//                    Item item1 = new Item("item", 10);

                    this.hero.addItemWithNewCopyToItemList(item);
                    this.hero.addItemWithNewCopyToItemList(item1);

                    System.out.println(item.equals(item1));

                    for (Map.Entry<Item, Integer> itemMap : this.hero.getHeroInventory().entrySet()) {
                        System.out.println(itemMap.getKey().getName() + " : " + itemMap.getValue());
                    }
                    System.out.println(item1.hashCode());
                    System.out.println(item.hashCode());

                    System.out.println(Objects.equals(this.hero.getHeroInventory().get(item), this.hero.getHeroInventory().get(item1)));


                }
                case 5 -> this.inventoryMenu();
//                case 6 -> this.shopMenu();
                case 7 -> blacksmithMenu();
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("You have won the game! Congratulations!");
    }

    public void blacksmithMenu() {
        hintUtil.printHint(HintName.BLACKSMITH);
        System.out.println("0. Go back");
        System.out.println("1. Refinement item");
        System.out.println("2. Dismantle item");
        System.out.println("3. Weapon and Armory shop");
        System.out.println("4. Sell item");
        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> this.blacksmithService.refinementItemQuality(this.hero);
            case 2 -> this.blacksmithService.dismantleItem(this.hero);
            case 3 -> {
//                List<Item> itemList = Collections.singletonList((Item) this.itemList.getItemList());

                wearableItemVendorCharacter.vendorOffer(this.hero);
            }
            case 4 -> wearableItemVendorCharacter.printItemsForSale(this.hero);
//            case 3 -> shopService.shopMenu(this.hero, this.WearableItems);
            default -> System.out.println("Enter valid input");
        }
    }

    public void inventoryMenu() {
        System.out.println("0. Go back");
        System.out.println("1. Items");
        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> this.inventoryService.inventoryMenu(this.hero);
            default -> System.out.println("Enter valid input");
        }
    }

    private void upgradeAbilityMenu() {
        System.out.println("0. Go Back");
        System.out.println("1. Spend points (" + this.hero.getUnspentAbilityPoints() + " points left)");
        System.out.println("2. Remove points");
        final int upgradeChoice = InputUtil.intScanner();
        if (upgradeChoice == 1) {
            this.heroAbilityManager.spendAbilityPoints();
        } else if (upgradeChoice == 2) {
            this.heroAbilityManager.removeAbilityPoints();
        }
    }

    private void initGame() {
        this.wearableItems = fileService.item(itemsLists.getItemList());
        hintUtil.initializeHintList();
        this.wearableItemVendorCharacter = new WearableItemVendorCharacter("Gimli", getInitialAbilityPoints(), this.itemsLists.getItemList());

        System.out.println("Welcome to the Gladiatus game!");
        System.out.println("0. Start new game");
        System.out.println("1. Load game");

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> System.out.println("Let's go then!");
            case 1 -> {
                final GameLoaded gameLoaded = fileService.loadGame();
                if (gameLoaded != null) {
                    this.hero = gameLoaded.getHero();
                    this.currentLevel = gameLoaded.getLevel();
                    this.heroAbilityManager.setHero(gameLoaded.getHero());
                    this.hintUtil.getHintList().putAll(gameLoaded.getHintUtil());
                    return;
                }
            }
            default -> System.out.println("Invalid choice");
        }

        System.out.println("Enter your name: ");
        final String name = InputUtil.stringScanner();
        PrintUtil.printDivider();

        this.hero.setName(name);
        System.out.println("\t\tHello " + hero.getName() + ". Let's start the game!");
        PrintUtil.printDivider();

        this.heroAbilityManager.spendAbilityPoints();
    }
}
