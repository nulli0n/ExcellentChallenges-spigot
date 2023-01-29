package su.nightexpress.excellentchallenges.data.serialize;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChallengeSerializer implements JsonDeserializer<Challenge>, JsonSerializer<Challenge> {

    @Override
    public Challenge deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        String typeId = object.get("typeId").getAsString();
        String templateId = object.get("templateId").getAsString();
        String generatorId = object.get("generatorId").getAsString();
        ChallengeJobType jobType = CollectionsUtil.getEnum(object.get("jobType").getAsString(), ChallengeJobType.class);
        if (jobType == null) return null;

        String name = object.get("name").getAsString();
        List<String> description = context.deserialize(object.get("description"), new TypeToken<List<String>>() {
        }.getType());
        int level = object.get("level").getAsInt();
        long dateCreated = object.get("dateCreated").getAsLong();
        //ItemStack icon = context.deserialize(object.get("icon"), new TypeToken<ItemStack>(){}.getType());
        //if (icon == null) return null;

        Map<String, int[]> objectives = context.deserialize(object.get("objectives"), new TypeToken<Map<String, int[]>>() {
        }.getType());
        Set<String> worlds = context.deserialize(object.get("worlds"), new TypeToken<Set<String>>() {
        }.getType());
        Set<String> rewards = context.deserialize(object.get("rewards"), new TypeToken<Set<String>>() {
        }.getType());

        return new Challenge(typeId, templateId, generatorId, jobType, name, description, level, dateCreated, objectives, worlds, rewards);
    }

    @Override
    public JsonElement serialize(Challenge challenge, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("typeId", challenge.getTypeId());
        object.addProperty("templateId", challenge.getTemplateId());
        object.addProperty("generatorId", challenge.getGeneratorId());
        object.addProperty("jobType", challenge.getJobType().name());
        object.addProperty("name", challenge.getName());
        object.add("description", context.serialize(challenge.getDescription()));
        object.addProperty("level", challenge.getLevel());
        object.addProperty("dateCreated", challenge.getDateCreated());
        //object.add("icon", context.serialize(challenge.getIconActive()));
        object.add("objectives", context.serialize(challenge.getObjectives()));
        object.add("worlds", context.serialize(challenge.getWorlds()));
        object.add("rewards", context.serialize(challenge.getRewards()));

        return object;
    }
}
