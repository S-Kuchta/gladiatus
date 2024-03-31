package kuchtastefan.world.event.specificEvent;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.npc.vendor.ShopService;
import kuchtastefan.character.npc.vendor.VendorCharacter;
import kuchtastefan.character.npc.vendor.VendorDB;
import kuchtastefan.character.npc.vendor.VendorOfferDB;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.utility.RandomNameGenerator;
import kuchtastefan.utility.RandomNumberGenerator;
import kuchtastefan.world.event.Event;

public class MerchantEvent extends Event {


    public MerchantEvent(int eventLevel) {
        super(eventLevel);
    }

    @Override
    public boolean eventOccurs(Hero hero) {
        ShopService shopService = new ShopService();
        int randomNumberToMeet = RandomNumberGenerator.getRandomNumber(0, 1);

        if (randomNumberToMeet == 0) {
            System.out.println("\t--> The merchant was too fast for you, you couldn't catch up and talk to him <--");
        } else {
            System.out.println("\tYou meet merchant caravan, what will you do?");
            PrintUtil.printIndexAndText("0", "Pass around");
            System.out.println();
            PrintUtil.printIndexAndText("1", "Talk");
            System.out.println();

            int choice = InputUtil.intScanner();
            switch (choice) {
                case 0 -> System.out.println("\t--> You just walk around merchant and greets him <--");
                case 1 -> {
                    VendorCharacter vendorCharacter = VendorDB.returnRandomMerchant();
                    vendorCharacter.setRandomCurrentItemListId(this.eventLevel);
                    vendorCharacter.setName(RandomNameGenerator.getRandomName());
                    shopService.vendorMenu(hero, vendorCharacter);
                }
                default -> PrintUtil.printEnterValidInput();
            }
        }

        return true;
    }
}
