package kuchtastefan.item.itemFilter;

import kuchtastefan.item.itemType.ItemType;
import kuchtastefan.utility.InitializeItemTypeList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemTypeFilter {

    private ItemType itemType;
    private List<ItemType> itemTypes;

    public ItemTypeFilter() {
        this.itemTypes = InitializeItemTypeList.itemTypeList();
    }

    public ItemTypeFilter(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemTypeFilter(List<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public boolean containsType(ItemType itemType) {
        return itemTypes.contains(itemType);
    }

    public boolean checkTypeCondition(ItemType itemType) {
        if (itemTypes != null) {
            return containsType(itemType);
        }

        return this.itemType == itemType;
    }

    public void addItemType(ItemType itemType) {
        itemTypes.add(itemType);
    }

    public void removeItemType(ItemType itemType) {
        itemTypes.remove(itemType);
    }
}
