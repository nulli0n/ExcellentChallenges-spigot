package su.nightexpress.excellentchallenges.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.data.serialize.GenChallengeSerializer;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.sql.SQLColumn;
import su.nexmedia.engine.api.data.sql.SQLValue;
import su.nexmedia.engine.api.data.sql.column.ColumnType;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.data.serialize.UniIntSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<ExcellentChallengesPlugin, ChallengeUser> {

    private static final SQLColumn COL_CHALLENGES           = SQLColumn.of("challenges", ColumnType.STRING);
    private static final SQLColumn COL_REFRESH_TIMES        = SQLColumn.of("refreshTimes", ColumnType.STRING);
    private static final SQLColumn COL_REROLL_TOKENS        = SQLColumn.of("rerollTokens", ColumnType.STRING);
    private static final SQLColumn COL_COMPLETED_CHALLENGES = SQLColumn.of("completedChallenges", ColumnType.STRING);

    private static DataHandler                        instance;
    private final  Function<ResultSet, ChallengeUser> userFunction;

    protected DataHandler(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, plugin);

        this.userFunction = (resultSet) -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());

                Map<String, Set<GeneratedChallenge>> challenges = gson.fromJson(resultSet.getString(COL_CHALLENGES.getName()), new TypeToken<Map<String, Set<GeneratedChallenge>>>(){}.getType());
                challenges.values().removeIf(Objects::isNull);

                Map<String, Long> refreshTimes = gson.fromJson(resultSet.getString(COL_REFRESH_TIMES.getName()), new TypeToken<Map<String, Long>>(){}.getType());

                Map<String, Integer> rerollTokens = gson.fromJson(resultSet.getString(COL_REROLL_TOKENS.getName()), new TypeToken<Map<String, Integer>>(){}.getType());

                Map<String, Map<String, Integer>> completedChallenges = gson.fromJson(resultSet.getString(COL_COMPLETED_CHALLENGES.getName()), new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());

                return new ChallengeUser(plugin, uuid, name, dateCreated, lastOnline,
                    challenges, refreshTimes, rerollTokens, completedChallenges);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };
    }

    @NotNull
    public static DataHandler getInstance(@NotNull ExcellentChallengesPlugin plugin) {
        if (instance == null) {
            instance = new DataHandler(plugin);
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
            .registerTypeAdapter(UniInt.class, new UniIntSerializer())
            .registerTypeAdapter(GeneratedChallenge.class, new GenChallengeSerializer());
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

    @Override
    @NotNull
    protected Function<ResultSet, ChallengeUser> getFunctionToUser() {
        return this.userFunction;
    }
}
