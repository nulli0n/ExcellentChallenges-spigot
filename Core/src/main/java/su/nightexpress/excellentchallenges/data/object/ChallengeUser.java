package su.nightexpress.excellentchallenges.data.object;

import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.nightcore.database.AbstractUser;

import java.util.*;

public class ChallengeUser extends AbstractUser<ExcellentChallengesPlugin> {

    private final Map<String, Set<GeneratedChallenge>> challenges;
    private final Map<String, Long>                    refreshTimes;
    private final Map<String, Integer>                 rerollTokens;
    private final Map<String, Map<String, Integer>>    completedChallenges;

    public ChallengeUser(@NotNull ExcellentChallengesPlugin plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public ChallengeUser(@NotNull ExcellentChallengesPlugin plugin, @NotNull UUID uuid, @NotNull String name,
                         long dateCreated, long lastOnline,
                         @NotNull Map<String, Set<GeneratedChallenge>> challenges,
                         @NotNull Map<String, Long> refreshTimes,
                         @NotNull Map<String, Integer> rerollTokens,
                         @NotNull Map<String, Map<String, Integer>> completedChallenges
    ) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.challenges = new HashMap<>(challenges);
        this.refreshTimes = new HashMap<>(refreshTimes);
        this.rerollTokens = new HashMap<>(rerollTokens);
        this.completedChallenges = new HashMap<>(completedChallenges);

        this.removeInvalidChallenges();
    }

    @NotNull
    public Map<String, Set<GeneratedChallenge>> getChallengesMap() {
        return challenges;
    }

    @NotNull
    public Set<GeneratedChallenge> getChallenges(@NotNull ChallengeCategory type) {
        return this.getChallenges(type.getId());
    }

    @NotNull
    public Set<GeneratedChallenge> getChallenges(@NotNull String id) {
        return this.getChallengesMap().computeIfAbsent(id, k -> new HashSet<>());
    }

    @NotNull
    public Map<String, Long> getRefreshTimes() {
        return refreshTimes;
    }

    public long getRefreshTime(@NotNull ChallengeCategory type) {
        return this.getRefreshTimes().getOrDefault(type.getId(), 0L);
    }

    public void updateRefreshTime(@NotNull ChallengeCategory type) {
        this.getRefreshTimes().put(type.getId(), System.currentTimeMillis() + type.getRefreshTime() * 1000L);
    }

    public boolean hasChallenges(@NotNull ChallengeCategory type) {
        return !this.getChallenges(type).isEmpty();
    }

    public boolean isTimeForNewChallenges(@NotNull ChallengeCategory type) {
        long deadline = this.getRefreshTime(type);
        return System.currentTimeMillis() > deadline;// && this.getChallenges(type).stream().allMatch(chal -> chal.getDateCreated() < deadline);
    }

    public double getProgressPercent(@NotNull ChallengeCategory type) {
        double size = this.getChallenges(type).size();
        double sum = this.getChallenges(type).stream().map(GeneratedChallenge::getCompletionPercent).mapToDouble(d -> d).sum();
        return sum / (size == 0D ? 1D : size);
    }

    public void removeInvalidChallenges() {
        this.getChallengesMap().values().forEach(challenges -> {
            //if (plugin.getChallengeManager().getTemplate(challenge.getTemplateId()) == null) return true;
            //if (plugin.getChallengeManager().getGenerator(challenge.getGeneratorId()) == null) return true;
            challenges.removeIf(Objects::isNull);
        });
    }

    @NotNull
    public Map<String, Integer> getRerollTokens() {
        return rerollTokens;
    }

    public int getRerollTokens(@NotNull ChallengeCategory type) {
        return this.getRerollTokens(type.getId());
    }

    public int getRerollTokens(@NotNull String id) {
        return this.getRerollTokens().getOrDefault(id.toLowerCase(), 0);
    }

    public void setRerollTokens(@NotNull ChallengeCategory type, int amount) {
        this.setRerollTokens(type.getId(), amount);
    }

    public void setRerollTokens(@NotNull String id, int amount) {
        this.getRerollTokens().put(id.toLowerCase(), Math.max(0, amount));
    }

    public void addRerollTokens(@NotNull ChallengeCategory type, int amount) {
        this.addRerollTokens(type.getId(), amount);
    }

    public void addRerollTokens(@NotNull String id, int amount) {
        this.setRerollTokens(id, this.getRerollTokens(id) + amount);
    }

    public void removeRerollTokens(@NotNull ChallengeCategory type, int amount) {
        this.removeRerollTokens(type.getId(), amount);
    }

    public void removeRerollTokens(@NotNull String id, int amount) {
        this.addRerollTokens(id, -amount);
    }

    @NotNull
    public Map<String, Map<String, Integer>> getCompletedChallengesMap() {
        return completedChallenges;
    }

    /*@NotNull
    public Map<String, Integer> getCompletedChallenges(@NotNull ChallengeActionType<?, ?> type) {
        return this.getCompletedChallenges(type.getName());
    }*/

    @NotNull
    public Map<String, Integer> getCompletedChallenges(@NotNull String id) {
        return this.getCompletedChallengesMap().getOrDefault(id.toLowerCase(), new HashMap<>());
    }

    public void addCompletedChallenge(@NotNull GeneratedChallenge challenge) {
        Map<String, Integer> completed = this.getCompletedChallenges(challenge.getType().getId());
        int amount = completed.getOrDefault(challenge.getActionType().getName(), 0);
        completed.put(challenge.getActionType().getName(), amount + 1);
        this.getCompletedChallengesMap().put(challenge.getType().getId(), completed);
    }

    public boolean isAllChallengesCompleted(@NotNull ChallengeCategory type) {
        return this.getChallenges(type).stream().allMatch(GeneratedChallenge::isCompleted);
    }

    public int getCompletedChallengesAmount() {
        return this.getCompletedChallengesMap().values().stream().flatMap(map -> map.values().stream())
            .mapToInt(i -> i).sum();
    }

    public int getCompletedChallengesAmount(@NotNull ChallengeCategory type) {
        return this.getCompletedChallengesAmount(type.getId());
    }

    public int getCompletedChallengesAmount(@NotNull String id) {
        return this.getCompletedChallenges(id).values().stream().mapToInt(i -> i).sum();
    }

    public int getCompletedChallengesAmount(@NotNull ActionType<?, ?> jobType) {
        return this.getCompletedChallengesMap().values().stream()
            .mapToInt(map -> map.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(jobType.getName())).mapToInt(Map.Entry::getValue).sum())
            .sum();
    }
}
