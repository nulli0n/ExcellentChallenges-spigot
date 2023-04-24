package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.Placeholders;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeCompleteEvent;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeObjectiveEvent;
import su.nightexpress.excellentchallenges.challenge.config.ChallengeTemplate;
import su.nightexpress.excellentchallenges.challenge.generator.ChallengeGenerator;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.handler.impl.*;
import su.nightexpress.excellentchallenges.challenge.listener.ChallengeListener;
import su.nightexpress.excellentchallenges.challenge.menu.ChallengesMainMenu;
import su.nightexpress.excellentchallenges.challenge.menu.RerollConfirmMenu;
import su.nightexpress.excellentchallenges.challenge.reward.ChallengeReward;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.*;
import java.util.stream.Collectors;

public class ChallengeManager extends AbstractManager<ExcellentChallenges> {

    private Map<String, ChallengeTemplate>  templates;
    private Map<String, ChallengeGenerator> generators;
    private Map<String, ChallengeReward>    rewards;

    private ChallengesMainMenu mainMenu;
    private RerollConfirmMenu  rerollConfirmMenu;

    public ChallengeManager(@NotNull ExcellentChallenges plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.plugin.getConfigManager().extractResources(Config.DIR_REWARDS);
        this.plugin.getConfigManager().extractResources(Config.DIR_TEMPLATES);
        this.plugin.getConfigManager().extractResources(Config.DIR_GENERATORS);

        this.rewards = new HashMap<>();
        this.templates = new HashMap<>();
        this.generators = new HashMap<>();

        for (ChallengeJobType jobType : Config.CHALLENGES_ENABLED.get()) {
            if (jobType.isAvailable()) {
                ChallengeHandler.register(this.createHandler(jobType));
            }
            else {
                this.plugin.info(jobType.name() + " handler is not registered. Missing dependency?");
            }
        }


        // Load generators.
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_GENERATORS, true)) {
            ChallengeGenerator generator = new ChallengeGenerator(plugin, cfg);
            if (generator.load()) {
                this.generators.put(generator.getId(), generator);
            }
            else this.plugin.error("Challenge generator not loaded: " + cfg.getFile().getName());
        }
        this.plugin.info("Loaded " + this.getGeneratorsMap().size() + " generators.");


        // Load templates.
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_TEMPLATES, true)) {
            ChallengeTemplate template = new ChallengeTemplate(plugin, cfg);
            if (template.load()) {
                this.templates.put(template.getId(), template);
            }
            else this.plugin.error("Challenge template not loaded: " + cfg.getFile().getName());
        }
        this.plugin.info("Loaded " + this.getTemplatesMap().size() + " templates.");


        // Load rewards.
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_REWARDS, true)) {
            cfg.getSection("").forEach(rewardId -> {
                String path = rewardId + ".";

                String name = cfg.getString(path + "Name", rewardId);
                List<String> commands = Colorizer.apply(cfg.getStringList(path + "Commands"));
                ChallengeReward challengeReward = new ChallengeReward(rewardId, name, commands);

                this.rewards.put(challengeReward.getId(), challengeReward);
            });
        }
        this.plugin.info("Loaded " + this.getRewardsMap().size() + " rewards.");

        this.mainMenu = new ChallengesMainMenu(this.plugin);
        this.rerollConfirmMenu = new RerollConfirmMenu(this.plugin);

        this.addListener(new ChallengeListener(this));
    }

    @Override
    protected void onShutdown() {
        if (this.rerollConfirmMenu != null) {
            this.rerollConfirmMenu.clear();
            this.rerollConfirmMenu = null;
        }
        if (this.mainMenu != null) {
            this.mainMenu.clear();
            this.mainMenu = null;
        }
        Config.CHALLENGES_TYPES.get().values().forEach(ChallengeType::clear);
        ChallengeHandler.unregisterAll();
    }

    @NotNull
    private ChallengeHandler createHandler(@NotNull ChallengeJobType jobType) {
        return switch (jobType) {
            case ANVIL_RENAME -> new AnvilRenameHandler(this.plugin);
            case BLOCK_BREAK -> new BlockBreakHandler(this.plugin);
            case BLOCK_PLACE -> new BlockPlaceHandler(this.plugin);
            case BLOCK_FERTILIZE -> new BlockFertilizeHandler(this.plugin);
            case DAMAGE_INFLICT -> new DamageHandler(this.plugin, ChallengeJobType.DAMAGE_INFLICT);
            case DAMAGE_RECEIVE -> new DamageHandler(this.plugin, ChallengeJobType.DAMAGE_RECEIVE);
            case ENTITY_BREED -> new EntityBreedHandler(this.plugin);
            case ENTITY_KILL -> new EntityKillHandler(this.plugin);
            case ENTITY_KILL_MYTHIC -> new EntityKillMythicHandler(this.plugin);
            case ENTITY_SHEAR -> new EntityShearHandler(this.plugin);
            case ENTITY_TAME -> new EntityTameHandler(this.plugin);
            case ITEM_CRAFT -> new ItemCraftHandler(this.plugin);
            case ITEM_COOK -> new ItemCookHandler(this.plugin);
            case ITEM_CONSUME -> new ItemConsumeHandler(this.plugin);
            case ITEM_ENCHANT -> new ItemEnchantHandler(this.plugin);
            case ITEM_DISENCHANT -> new ItemDisenchantHandler(this.plugin);
            case ITEM_FISH -> new ItemFishHandler(this.plugin);
            case ITEM_TRADE -> new ItemTradeHandler(this.plugin);
            case POTION_BREW -> new PotionBrewHandler(this.plugin);
            case PROJECTILE_LAUNCH -> new ProjectileLaunchHandler(this.plugin);
        };
    }

    @NotNull
    public Map<String, ChallengeGenerator> getGeneratorsMap() {
        return generators;
    }

    @NotNull
    public Collection<ChallengeGenerator> getGenerators() {
        return this.getGeneratorsMap().values();
    }

    @Nullable
    public ChallengeGenerator getGenerator(@NotNull String id) {
        return this.getGeneratorsMap().get(id.toLowerCase());
    }

    @NotNull
    public Map<String, ChallengeTemplate> getTemplatesMap() {
        return templates;
    }

    @NotNull
    public Collection<ChallengeTemplate> getTemplates() {
        return this.getTemplatesMap().values();
    }

    @Nullable
    public ChallengeTemplate getTemplate(@NotNull String id) {
        return this.getTemplatesMap().get(id.toLowerCase());
    }

    @Nullable
    public ChallengeType getChallengeType(@NotNull String id) {
        return Config.CHALLENGES_TYPES.get().get(id.toLowerCase());
    }

    @NotNull
    public Map<String, ChallengeReward> getRewardsMap() {
        return this.rewards;
    }

    @NotNull
    public Collection<ChallengeReward> getRewards() {
        return this.getRewardsMap().values();
    }

    @Nullable
    public ChallengeReward getReward(@NotNull String id) {
        return this.getRewardsMap().get(id.toLowerCase());
    }

    @NotNull
    public ChallengesMainMenu getMainMenu() {
        return mainMenu;
    }

    @NotNull
    public RerollConfirmMenu getRerollConfirmMenu() {
        return rerollConfirmMenu;
    }

    public void updateChallenges(@NotNull Player player, boolean force) {
        Collection<ChallengeType> challengeTypes = Config.CHALLENGES_TYPES.get().values();
        challengeTypes.forEach(type -> this.updateChallenges(player, type, force));
    }

    public void updateChallenges(@NotNull Player player, @NotNull ChallengeType type, boolean force) {
        if (Hooks.isCitizensNPC(player)) return;

        ChallengeUser user = this.plugin.getUserManager().getUserData(player);

        if (!force) {
            if (user.hasChallenges(type) && !user.isTimeForNewChallenges(type)) return;
        }

        this.createChallenges(player, type);
    }

    public void createChallenges(@NotNull Player player) {
        Collection<ChallengeType> challengeTypes = Config.CHALLENGES_TYPES.get().values();
        challengeTypes.forEach(type -> {
            this.createChallenges(player, type);
        });
    }

    @NotNull
    public Set<Challenge> createChallenges(@NotNull Player player, @NotNull ChallengeType type) {
        if (Hooks.isCitizensNPC(player)) return Collections.emptySet();

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Set<Challenge> generated = new HashSet<>();

        int amount = type.getAmountPerRank(player);
        if (amount <= 0) return generated;

        String rank = Hooks.getPermissionGroup(player);
        Set<ChallengeTemplate> templates = type.getChallengesPerRank(rank).stream()
            .map(this::getTemplate).filter(Objects::nonNull).collect(Collectors.toSet());
        if (templates.isEmpty()) {
            return generated;
        }

        Set<ChallengeGenerator> generators = templates.stream().flatMap(template -> template.getGenerators().keySet()
            .stream().map(this::getGenerator).filter(Objects::nonNull)).collect(Collectors.toSet());
        if (generators.isEmpty()) {
            return generated;
        }

        while (amount > 0 && !templates.isEmpty() && !generators.isEmpty()) {
            ChallengeTemplate template = Rnd.get(templates);
            if (template == null) break;

            ChallengeGenerator generator = template.pickGenerator();
            if (generator == null) {
                templates.remove(template);
                continue;
            }
            if (type.isUniqueGenerators() && !generators.remove(generator)) continue;

            try {
                generated.add(Challenge.from(template, generator, type));
            }
            catch (IllegalStateException e) {
                this.plugin.error("Could not generate challenge. See stracktrace below:");
                e.printStackTrace();
            }

            //templates.remove(template);
            amount--;
        }

        Set<Challenge> challenges = user.getChallenges(type.getId());
        challenges.clear();
        challenges.addAll(generated);
        user.updateRefreshTime(type);
        user.saveData(this.plugin);

        return generated;
    }

    public void progressChallenge(@NotNull Player player, @NotNull ChallengeJobType jobType, @NotNull String objective, int amount) {
        if (Hooks.isCitizensNPC(player)) return;

        ChallengeUser user = this.plugin.getUserManager().getUserData(player);
        Set<Challenge> challenges = user.getChallengesMap().values().stream().flatMap(Collection::stream)
            .filter(chal -> chal.getJobType() == jobType)
            .filter(chal -> chal.isApplicableWorld(player.getWorld().getName()))
            .filter(chal -> chal.addObjectiveProgress(objective, amount))
            .collect(Collectors.toSet());

        challenges.forEach(challenge -> {
            if (challenge.isCompleted()) {
                PlayerChallengeCompleteEvent event = new PlayerChallengeCompleteEvent(player, user, challenge);
                plugin.getPluginManager().callEvent(event);
            }
            else {
                String obj2 = objective;
                if (!challenge.hasObjectiveExact(objective)) obj2 = Placeholders.WILDCARD;

                PlayerChallengeObjectiveEvent event = new PlayerChallengeObjectiveEvent(player, user, challenge, obj2, amount);
                plugin.getPluginManager().callEvent(event);
            }
        });

        user.saveData(this.plugin);
    }
}
