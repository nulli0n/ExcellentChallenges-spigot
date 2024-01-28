package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectList<T extends GenObject> {

    private final Map<String, T> objectMap;

    public ObjectList() {
        this.objectMap = new HashMap<>();
    }

    @Nullable
    public T pickObject(@NotNull Difficulty difficulty) {
        Map<T, Double> weightMap = new HashMap<>();
        this.getObejcts().forEach(obj -> {
            if (obj.getWeight() <= 0D) return;
            if (obj.getItems(difficulty).isEmpty()) return;

            weightMap.put(obj, obj.getWeight());
        });
        if (weightMap.isEmpty()) return null;

        return Rnd.getByWeight(weightMap);
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
