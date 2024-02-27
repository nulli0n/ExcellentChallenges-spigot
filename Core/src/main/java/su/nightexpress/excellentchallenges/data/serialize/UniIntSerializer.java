package su.nightexpress.excellentchallenges.data.serialize;

import com.google.gson.*;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.lang.reflect.Type;

public class UniIntSerializer implements JsonSerializer<UniInt>, JsonDeserializer<UniInt> {

    @Override
    public UniInt deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        int min = object.get("min").getAsInt();
        int max = object.get("max").getAsInt();

        return UniInt.of(min, max);
    }

    @Override
    public JsonElement serialize(UniInt value, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("min", value.getMinValue());
        object.addProperty("max", value.getMaxValue());
        return object;
    }
}
