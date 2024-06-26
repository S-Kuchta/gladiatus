package kuchtastefan.world.location;

import kuchtastefan.utility.annotationStrategy.Exclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestLocation extends Location {

    @Exclude
    private int questObjectiveId;

    public QuestLocation(int locationId, int questObjectiveId, String locationName, int locationLevel) {
        super(locationId, locationName, locationLevel);
        this.questObjectiveId = questObjectiveId;
    }
}
