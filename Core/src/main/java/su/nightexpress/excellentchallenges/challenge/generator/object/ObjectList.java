package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.*;

public class ObjectList<T extends GenObject> {

    private final Map<String, T> objectMap;

    public ObjectList() {
        this.objectMap = new HashMap<>();
    }

    @Nullable
    public T pickObject(@NotNull Difficulty difficulty) {
        return this.pickObjects(difficulty, 1).stream().findFirst().orElse(null);
    }

    @NotNull
    public Set<T> pickObjects(@NotNull Difficulty difficulty, int amount) {
        Set<T> objects = new HashSet<>();

        Map<T, Double> weightMap = new HashMap<>();
        this.getObejcts().forEach(obj -> {
            if (obj.getWeight() <= 0D) return;
            if (obj.getItems(difficulty).isEmpty()) return;

            weightMap.put(obj, obj.getWeight());
        });
        if (weightMap.isEmpty()) return objects;

        while (amount > 0 && !weightMap.isEmpty()) {
            T object = Rnd.getByWeight(weightMap);
            objects.add(object);
            weightMap.remove(object);
            amount--;
        }

        return objects;
    }

    public void add(@NotNull T object) {
        this.getObjectMap().put(object.getId(), object);
    }

    @NotNull
    public Map<String, T> getObjectMap() {
        return objectMap;
    }

    @NotNull
    public Collection<T> getObejcts() {
        return this.getObjectMap().values();
    }
}
