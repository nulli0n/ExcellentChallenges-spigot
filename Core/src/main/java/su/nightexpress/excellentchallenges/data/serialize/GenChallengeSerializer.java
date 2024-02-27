package su.nightexpress.excellentchallenges.data.serialize;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.challenge.condition.ConditionConfig;
import su.nightexpress.excellentchallenges.challenge.generator.Generator;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenChallengeSerializer implements JsonDeserializer<GeneratedChallenge>, JsonSerializer<GeneratedChallenge> {

    @Override
    public GeneratedChallenge deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        ExcellentChallengesPlugin plugin = ExcellentChallengesAPI.PLUGIN;

        String jobTypRaw = object.get("jobType").getAsString();
        ActionType<?, ?> actionType = plugin.getActionRegistry().getActionType(jobTypRaw);
        if (actionType == null) return null;

        String generatorId = object.get("generator").getAsString();
        Generator generator = plugin.getChallengeManager().getGenerator(generatorId);
        if (generator == null) return null;

        String typeId = object.get("type").getAsString();
        ChallengeCategory challengeCategory = plugin.getChallengeManager().getChallengeType(typeId);
        if (challengeCategory == null) return null;

        String diffId = object.get("difficulty").getAsString();
        Difficulty difficulty = plugin.getChallengeManager().getDifficulty(diffId);
        if (difficulty == null) return null;

        int level = object.get("level").getAsInt();

        Map<String, UniInt> objectives = context.deserialize(object.get("objectives"), new TypeToken<Map<String, UniInt>>() {}.getType());

        List<String> conditionIds = context.deserialize(object.get("conditions"), new TypeToken<List<String>>(){}.getType());
        List<ConditionConfig> conditionConfigs = new ArrayList<>();
        for (String conId : conditionIds) {
            ConditionConfig conditionConfig = plugin.getChallengeManager().getCondition(conId);
            if (conditionConfig == null) continue;

            conditionConfigs.add(conditionConfig);
        }

        List<String> rewardIds = context.deserialize(object.get("rewards"), new TypeToken<List<String>>() {}.getType());
        List<Reward> rewards = new ArrayList<>();
        for (String rewId : rewardIds) {
            Reward reward = plugin.getChallengeManager().getReward(rewId);
            if (reward == null) continue;

            rewards.add(reward);
        }

        long dateCreated = object.get("dateCreated").getAsLong();

        return GeneratedChallenge.create(
            ExcellentChallengesAPI.PLUGIN,
            generator, challengeCategory, difficulty, level,
            objectives, conditionConfigs, rewards, dateCreated
        );

    }

    @Override
    public JsonElement serialize(GeneratedChallenge challenge, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        List<String> conditionIds = challenge.getConditions().keySet().stream().map(ConditionConfig::getId).toList();
        List<String> rewardIds = challenge.getRewards().stream().map(Reward::getId).toList();

        object.addProperty("jobType", challenge.getActionType().getName());
        object.addProperty("generator", challenge.getGenerator().getId());
        object.addProperty("type", challenge.getType().getId());
        object.addProperty("difficulty", challenge.getDifficulty().getId());
        object.addProperty("level", challenge.getLevel());

        object.add("objectives", context.serialize(challenge.getObjectives()));
        object.add("conditions", context.serialize(conditionIds));
        object.add("rewards", context.serialize(rewardIds));

        object.addProperty("dateCreated", challenge.getDateCreated());

        return object;
    }
}
