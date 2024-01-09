package kuchtastefan.regions.events;

import kuchtastefan.characters.enemy.EnemyList;
import kuchtastefan.characters.hero.Hero;
import kuchtastefan.items.ItemsLists;
import kuchtastefan.regions.locations.Location;
import kuchtastefan.regions.locations.LocationType;
import kuchtastefan.utility.RandomNumberGenerator;

import java.util.List;

public class EventService {

    private final ItemsLists itemsLists;
    private final List<Location> allLocations;
    private final List<Location> discoveredLocations;

    public EventService(ItemsLists itemsLists, List<Location> allLocations, List<Location> discoveredLocations) {
        this.itemsLists = itemsLists;
        this.allLocations = allLocations;
        this.discoveredLocations = discoveredLocations;
    }

    public void randomRegionEventGenerate(Hero hero, EnemyList enemyList, LocationType locationType) {
        int randomNumber = RandomNumberGenerator.getRandomNumber(0, 7);
        int eventLevel = hero.getLevel() + RandomNumberGenerator.getRandomNumber(-1, 1);
        if (eventLevel == 0) {
            eventLevel = 1;
        }

        switch (randomNumber) {
            case 0 -> {
                new MerchantEvent(eventLevel, this.itemsLists).eventOccurs(hero);
            }
            case 1, 2 -> {
                new CombatEvent(eventLevel, enemyList, locationType).eventOccurs(hero);
            }
            case 3 -> {
                new DiscoverLocationEvent(eventLevel, this.allLocations, this.discoveredLocations).eventOccurs(hero);
            }
            case 4 -> {
                new FindItemEvent(eventLevel, this.itemsLists).eventOccurs(hero);
            }
            default -> new NoOutcomeEvent(0).eventOccurs(hero);
        }
    }
}