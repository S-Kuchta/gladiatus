package kuchtastefan.character.hero.save;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.hero.save.item.HeroItem;
import kuchtastefan.character.hero.save.location.HeroLocation;
import kuchtastefan.character.hero.save.quest.HeroQuest;
import kuchtastefan.character.hero.save.quest.HeroQuestObjective;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class SaveGameEntities {

    private final SaveGameEntityList<HeroQuest> heroQuests;
    private final SaveGameEntityList<HeroQuestObjective> heroQuestObjectives;
    private final SaveGameEntityList<HeroLocation> heroLocations;
    private final SaveGameEntityList<HeroItem> heroItems;

    public SaveGameEntities () {
        this.heroItems = new SaveGameEntityList<>(new HashMap<>());
        this.heroQuests = new SaveGameEntityList<>(new HashMap<>());
        this.heroQuestObjectives = new SaveGameEntityList<>(new HashMap<>());
        this.heroLocations = new SaveGameEntityList<>(new HashMap<>());
    }

}
