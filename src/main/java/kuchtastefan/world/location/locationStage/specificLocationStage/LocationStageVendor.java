package kuchtastefan.world.location.locationStage.specificLocationStage;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.npc.vendor.VendorCharacter;
import kuchtastefan.character.npc.vendor.VendorCharacterDB;
import kuchtastefan.service.ShopService;
import kuchtastefan.world.location.Location;
import kuchtastefan.world.location.locationStage.CanEnterStageAfterComplete;
import kuchtastefan.world.location.locationStage.LocationStage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class LocationStageVendor extends LocationStage implements CanEnterStageAfterComplete {

    private final int vendorId;

    public LocationStageVendor(String stageName, int vendorId) {
        super(stageName);
        this.vendorId = vendorId;
    }

    @Override
    public boolean exploreStage(Hero hero, Location location) {
        VendorCharacter vendorCharacter = VendorCharacterDB.returnVendorCharacterFromDb(this.vendorId);
        ShopService shopService = new ShopService(vendorCharacter);

        this.setStageName(vendorCharacter.getName() + " (" +  StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                vendorCharacter.returnItemClass().getSimpleName().replaceAll("\\d+", "")), " ") + " Vendor)");

        shopService.mainMenu(hero);
        return true;
    }
}
