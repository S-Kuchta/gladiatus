package kuchtastefan.item.specificItems.questItem;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.item.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestItem extends Item {

    private int questObjectiveId;

    public QuestItem(Integer itemId, String name, double price, int itemLevel, int questObjectiveId) {
        super(itemId, name, price, itemLevel);
        this.questObjectiveId = questObjectiveId;
    }

    @Override
    public void printItemDescription(Hero hero) {
        System.out.println(this.name);
    }
}
