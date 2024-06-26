package kuchtastefan.service;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.hero.inventory.UsingHeroInventory;
import kuchtastefan.item.Item;
import kuchtastefan.item.itemFilter.ItemFilter;
import kuchtastefan.item.itemType.ItemType;
import kuchtastefan.item.specificItems.consumeableItem.ConsumableItem;
import kuchtastefan.item.specificItems.craftingItem.CraftingReagentItem;
import kuchtastefan.item.specificItems.keyItem.KeyItem;
import kuchtastefan.item.specificItems.questItem.QuestItem;
import kuchtastefan.item.specificItems.wearableItem.WearableItem;
import kuchtastefan.item.specificItems.wearableItem.WearableItemType;
import kuchtastefan.item.usableItem.UsableItem;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.printUtil.InventoryPrint;
import kuchtastefan.utility.printUtil.PrintUtil;

public class InventoryMenuService implements UsingHeroInventory {

    @Override
    public void mainMenu(Hero hero) {
        if (!hero.isInCombat()) {
            PrintUtil.printMenuHeader(hero.getName() + " Inventory");
            PrintUtil.printMenuOptions("Go back", "Wearable Items", "Crafting reagents", "Consumable Items", "Quest Items", "Key Items");
            int choice = InputUtil.intScanner();
            switch (choice) {
                case 0 -> {
                }
//                case 1 -> wearableItemsMenu(hero);
                case 1 -> hero.getHeroInventory().selectItem(hero, WearableItem.class, new ItemFilter(WearableItemType.WEAPON), this, 1);
                case 2 ->
                        hero.getHeroInventory().selectItem(hero, CraftingReagentItem.class, new ItemFilter(), this, 1);
                case 3 -> hero.getHeroInventory().selectItem(hero, ConsumableItem.class, new ItemFilter(), this, 1);
                case 4 -> hero.getHeroInventory().selectItem(hero, QuestItem.class, new ItemFilter(), this, 1);
                case 5 -> hero.getHeroInventory().selectItem(hero, KeyItem.class, new ItemFilter(), this, 1);
                default -> PrintUtil.printEnterValidInput();
            }
        }
    }

    @Override
    public boolean itemOptions(Hero hero, Item item) {
        return UsableItem.useItem(hero, item, this);
    }

    public void wearableItemsMenu(Hero hero) {
        PrintUtil.printMenuHeader("Wearable");
        String[] wearableTypes = {"Go Back",
                "Weapons (" + InventoryPrint.printWearableItemCountByType(hero, WearableItemType.WEAPON) + "x)",
                "Head (" + InventoryPrint.printWearableItemCountByType(hero, WearableItemType.HEAD) + "x)",
                "Body (" + InventoryPrint.printWearableItemCountByType(hero, WearableItemType.BODY) + "x)",
                "Hands (" + InventoryPrint.printWearableItemCountByType(hero, WearableItemType.HANDS) + "x)",
                "Boots (" + InventoryPrint.printWearableItemCountByType(hero, WearableItemType.BOOTS) + "x)"};

        PrintUtil.printMenuOptions(wearableTypes);
        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> mainMenu(hero);
            case 1, 2, 3, 4, 5 ->
                    hero.getHeroInventory().selectItem(hero, WearableItem.class, new ItemFilter(WearableItemType.values()[choice - 1]), this, 1);
            default -> PrintUtil.printEnterValidInput();
        }
    }
}
