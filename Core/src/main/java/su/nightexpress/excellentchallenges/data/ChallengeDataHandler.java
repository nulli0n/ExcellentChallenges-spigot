package su.nightexpress.excellentchallenges.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.sql.SQLColumn;
import su.nexmedia.engine.api.data.sql.SQLValue;
import su.nexmedia.engine.api.data.sql.column.ColumnType;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.data.serialize.ChallengeSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class ChallengeDataHandler extends AbstractUserDataHandler<ExcellentChallenges, ChallengeUser> {

    private static final SQLColumn COL_CHALLENGES           = SQLColumn.of("challenges", ColumnType.STRING);
    private static final SQLColumn COL_REFRESH_TIMES        = SQLColumn.of("refreshTimes", ColumnType.STRING);
    private static final SQLColumn COL_REROLL_TOKENS        = SQLColumn.of("rerollTokens", ColumnType.STRING);
    private static final SQLColumn COL_COMPLETED_CHALLENGES = SQLColumn.of("completedChallenges", ColumnType.STRING);

    private static ChallengeDataHandler               instance;
    private final  Function<ResultSet, ChallengeUser> userFunction;

    protected ChallengeDataHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, plugin);

        this.userFunction = (resultSet) -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                Map<String, Set<Challenge>> challenges = gson.fromJson(resultSet.getString(COL_CHALLENGES.getName()), new TypeToken<Map<String, Set<Challenge>>>() {
                }.getType());
                Map<String, Long> refreshTimes = gson.fromJson(resultSet.getString(COL_REFRESH_TIMES.getName()), new TypeToken<Map<String, Long>>() {
                }.getType());
                Map<String, Integer> rerollTokens = gson.fromJson(resultSet.getString(COL_REROLL_TOKENS.getName()), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Map<ChallengeJobType, Integer>> completedChallenges = gson.fromJson(resultSet.getString(COL_COMPLETED_CHALLENGES.getName()), new TypeToken<Map<String, Map<ChallengeJobType, Integer>>>() {
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
    protected void onShutdown() {
        super.onShutdown();
        instance = null;
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
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return super.registerAdapters(builder)
            .registerTypeAdapter(Challenge.class, new ChallengeSerializer());
    }

    @Override
    @NotNull
    protected List<SQLColumn> getExtraColumns() {
        return Arrays.asList(COL_CHALLENGES, COL_REFRESH_TIMES, COL_REROLL_TOKENS, COL_COMPLETED_CHALLENGES);
    }

    @Override
    @NotNull
    protected List<SQLValue> getSaveColumns(@NotNull ChallengeUser user) {
        return Arrays.asList(
            COL_CHALLENGES.toValue(this.gson.toJson(user.getChallengesMap())),
            COL_REFRESH_TIMES.toValue(this.gson.toJson(user.getRefreshTimes())),
            COL_REROLL_TOKENS.toValue(this.gson.toJson(user.getRerollTokens())),
            COL_COMPLETED_CHALLENGES.toValue(this.gson.toJson(user.getCompletedChallengesMap()))
        );
    }

    /*@Override
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
    }*/

    @Override
    @NotNull
    protected Function<ResultSet, ChallengeUser> getFunctionToUser() {
        return this.userFunction;
    }
}
