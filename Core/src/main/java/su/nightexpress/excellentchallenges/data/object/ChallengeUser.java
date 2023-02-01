package su.nightexpress.excellentchallenges.data.object;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUser;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.util.*;

public class ChallengeUser extends AbstractUser<ExcellentChallenges> {

    private final Map<String, Set<Challenge>>                 challenges;
    private final Map<String, Long>                           refreshTimes;
    private final Map<String, Integer>                        rerollTokens;
    private final Map<String, Map<ChallengeJobType, Integer>> completedChallenges;

    public ChallengeUser(@NotNull ExcellentChallenges plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public ChallengeUser(@NotNull ExcellentChallenges plugin, @NotNull UUID uuid, @NotNull String name,
                         long dateCreated, long lastOnline,
                         @NotNull Map<String, Set<Challenge>> challenges,
                         @NotNull Map<String, Long> refreshTimes,
                         @NotNull Map<String, Integer> rerollTokens,
                         @NotNull Map<String, Map<ChallengeJobType, Integer>> completedChallenges
    ) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.challenges = new HashMap<>(challenges);
        this.refreshTimes = new HashMap<>(refreshTimes);
        this.rerollTokens = new HashMap<>(rerollTokens);
        this.completedChallenges = new HashMap<>(completedChallenges);

        this.removeInvalidChallenges();
    }

    @NotNull
    public Map<String, Set<Challenge>> getChallengesMap() {
        return challenges;
    }

    @NotNull
    public Set<Challenge> getChallenges(@NotNull ChallengeType type) {
        return this.getChallenges(type.getId());
    }

    @NotNull
    public Set<Challenge> getChallenges(@NotNull String id) {
        return this.getChallengesMap().computeIfAbsent(id, k -> new HashSet<>());
    }

    @NotNull
    public Map<String, Long> getRefreshTimes() {
        return refreshTimes;
    }

    public long getRefreshTime(@NotNull ChallengeType type) {
        return this.getRefreshTimes().getOrDefault(type.getId(), 0L);
    }

    public void updateRefreshTime(@NotNull ChallengeType type) {
        this.getRefreshTimes().put(type.getId(), System.currentTimeMillis() + type.getRefreshTime() * 1000L);
    }

    public boolean hasChallenges(@NotNull ChallengeType type) {
        return !this.getChallenges(type).isEmpty();
    }

    public boolean isTimeForNewChallenges(@NotNull ChallengeType type) {
        long deadline = this.getRefreshTime(type);
        return System.currentTimeMillis() > deadline;// && this.getChallenges(type).stream().allMatch(chal -> chal.getDateCreated() < deadline);
    }

    public double getProgressPercent(@NotNull ChallengeType type) {
        double size = this.getChallenges(type).size();
        double sum = this.getChallenges(type).stream().map(Challenge::getProgressPercent).mapToDouble(d -> d).sum();
        return sum / (size == 0D ? 1D : size);
    }

    public void removeInvalidChallenges() {
        this.getChallengesMap().values().forEach(challenges -> {
            challenges.removeIf(challenge -> {
                if (plugin.getChallengeManager().getTemplate(challenge.getTemplateId()) == null) return true;
                if (plugin.getChallengeManager().getGenerator(challenge.getGeneratorId()) == null) return true;
                return false;
            });
        });
    }

    @NotNull
    public Map<String, Integer> getRerollTokens() {
        return rerollTokens;
    }

    public int getRerollTokens(@NotNull ChallengeType type) {
        return this.getRerollTokens(type.getId());
    }

    public int getRerollTokens(@NotNull String id) {
        return this.getRerollTokens().getOrDefault(id.toLowerCase(), 0);
    }

    public void setRerollTokens(@NotNull ChallengeType type, int amount) {
        this.setRerollTokens(type.getId(), amount);
    }

    public void setRerollTokens(@NotNull String id, int amount) {
        this.getRerollTokens().put(id.toLowerCase(), Math.max(0, amount));
    }

    public void addRerollTokens(@NotNull ChallengeType type, int amount) {
        this.addRerollTokens(type.getId(), amount);
    }

    public void addRerollTokens(@NotNull String id, int amount) {
        this.setRerollTokens(id, this.getRerollTokens(id) + amount);
    }

    public void takeRerollTokens(@NotNull ChallengeType type, int amount) {
        this.takeRerollTokens(type.getId(), amount);
    }

    public void takeRerollTokens(@NotNull String id, int amount) {
        this.addRerollTokens(id, -amount);
    }

    @NotNull
    public Map<String, Map<ChallengeJobType, Integer>> getCompletedChallengesMap() {
        return completedChallenges;
    }

    @NotNull
    public Map<ChallengeJobType, Integer> getCompletedChallenges(@NotNull String id) {
        return this.getCompletedChallengesMap().getOrDefault(id.toLowerCase(), new HashMap<>());
    }

    public void addCompletedChallenge(@NotNull Challenge challenge) {
        Map<ChallengeJobType, Integer> completed = this.getCompletedChallenges(challenge.getTypeId());
        int amount = completed.getOrDefault(challenge.getJobType(), 0);
        completed.put(challenge.getJobType(), amount + 1);
        this.getCompletedChallengesMap().put(challenge.getTypeId(), completed);
    }

    public boolean isAllChallengesCompleted(@NotNull ChallengeType type) {
        return this.getChallenges(type).stream().allMatch(Challenge::isCompleted);
    }

    public int getCompletedChallengesAmount() {
        return this.getCompletedChallengesMap().values().stream().flatMap(map -> map.values().stream())
            .mapToInt(i -> i).sum();
    }

    public int getCompletedChallengesAmount(@NotNull ChallengeType type) {
        return this.getCompletedChallengesAmount(type.getId());
    }

    public int getCompletedChallengesAmount(@NotNull String id) {
        return this.getCompletedChallenges(id).values().stream().mapToInt(i -> i).sum();
    }

    public int getCompletedChallengesAmount(@NotNull ChallengeJobType jobType) {
        return this.getCompletedChallengesMap().values().stream()
            .mapToInt(map -> map.entrySet().stream().filter(entry -> entry.getKey() == jobType).mapToInt(Map.Entry::getValue).sum())
            .sum();
    }
}
