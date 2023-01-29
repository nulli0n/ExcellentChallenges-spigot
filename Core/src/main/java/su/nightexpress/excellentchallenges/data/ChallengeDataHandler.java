package su.nightexpress.excellentchallenges.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.DataTypes;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.data.serialize.ChallengeSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ChallengeDataHandler extends AbstractUserDataHandler<ExcellentChallenges, ChallengeUser> {

    private static final String COL_CHALLENGES           = "challenges";
    private static final String COL_REFRESH_TIMES        = "refreshTimes";
    private static final String COL_REROLL_TOKENS        = "rerollTokens";
    private static final String COL_COMPLETED_CHALLENGES = "completedChallenges";

    private static ChallengeDataHandler               instance;
    private final  Function<ResultSet, ChallengeUser> FUNC_USER;

    protected ChallengeDataHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, plugin);

        this.FUNC_USER = (resultSet) -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COL_USER_UUID));
                String name = resultSet.getString(COL_USER_NAME);
                long lastOnline = resultSet.getLong(COL_USER_LAST_ONLINE);
                long dateCreated = resultSet.getLong(COL_USER_DATE_CREATED);
                Map<String, Set<Challenge>> challenges = gson.fromJson(resultSet.getString(COL_CHALLENGES), new TypeToken<Map<String, Set<Challenge>>>() {
                }.getType());
                Map<String, Long> refreshTimes = gson.fromJson(resultSet.getString(COL_REFRESH_TIMES), new TypeToken<Map<String, Long>>() {
                }.getType());
                Map<String, Integer> rerollTokens = gson.fromJson(resultSet.getString(COL_REROLL_TOKENS), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Map<ChallengeJobType, Integer>> completedChallenges = gson.fromJson(resultSet.getString(COL_COMPLETED_CHALLENGES), new TypeToken<Map<String, Map<ChallengeJobType, Integer>>>() {
                }.getType());

                return new ChallengeUser(plugin, uuid, name, dateCreated, lastOnline,
                    challenges, refreshTimes, rerollTokens, completedChallenges);
            }
            catch (SQLException e) {
                return null;
            }
        };
    }

    @NotNull
    public static ChallengeDataHandler getInstance(@NotNull ExcellentChallenges plugin) throws SQLException {
        if (instance == null) {
            instance = new ChallengeDataHandler(plugin);
        }
        return instance;
    }

    @Override
    public void onSynchronize() {
        for (ChallengeUser user : this.dataHolder.getUserManager().getUsersLoaded()) {
            ChallengeUser fetch = this.getUser(user.getId());
            if (fetch == null) continue;

            user.getChallengesMap().clear();
            user.getChallengesMap().putAll(fetch.getChallengesMap());

            user.getRefreshTimes().clear();
            user.getRefreshTimes().putAll(fetch.getRefreshTimes());

            user.getRerollTokens().clear();
            user.getRerollTokens().putAll(fetch.getRerollTokens());

            user.getCompletedChallengesMap().clear();
            user.getCompletedChallengesMap().putAll(fetch.getCompletedChallengesMap());
        }
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
        instance = null;
    }

    @Override
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return super.registerAdapters(builder)
            .registerTypeAdapter(Challenge.class, new ChallengeSerializer());
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToCreate() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_CHALLENGES, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_REFRESH_TIMES, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_REROLL_TOKENS, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_COMPLETED_CHALLENGES, DataTypes.STRING.build(this.getDataType()));
        return map;
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToSave(@NotNull ChallengeUser user) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_CHALLENGES, this.gson.toJson(user.getChallengesMap()));
        map.put(COL_REFRESH_TIMES, this.gson.toJson(user.getRefreshTimes()));
        map.put(COL_REROLL_TOKENS, this.gson.toJson(user.getRerollTokens()));
        map.put(COL_COMPLETED_CHALLENGES, this.gson.toJson(user.getCompletedChallengesMap()));
        return map;
    }

    @Override
    @NotNull
    protected Function<ResultSet, ChallengeUser> getFunctionToUser() {
        return this.FUNC_USER;
    }
}
