package su.nightexpress.excellentchallenges;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.command.list.ReloadSubCommand;
import su.nexmedia.engine.utils.EngineUtils;
import su.nexmedia.engine.utils.blocktracker.PlayerBlockTracker;
import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import su.nightexpress.excellentchallenges.challenge.action.ActionRegistry;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;
import su.nightexpress.excellentchallenges.command.OpenCommand;
import su.nightexpress.excellentchallenges.command.RerollTokensCommand;
import su.nightexpress.excellentchallenges.command.ResetCommand;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.DataHandler;
import su.nightexpress.excellentchallenges.data.UserManager;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.hooks.external.PlaceholderHook;
import su.nightexpress.excellentchallenges.nms.ChallengeNMS;
import su.nightexpress.excellentchallenges.nms.V1_18_R2;
import su.nightexpress.excellentchallenges.nms.V1_19_R3;
import su.nightexpress.excellentchallenges.nms.V1_20_R2;

public class ExcellentChallengesPlugin extends NexPlugin<ExcellentChallengesPlugin> implements UserDataHolder<ExcellentChallengesPlugin, ChallengeUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private ActionRegistry   actionRegistry;
    private ChallengeManager challengeManager;
    private ChallengeNMS     challengeNMS;

    @Override
    @NotNull
    protected ExcellentChallengesPlugin getSelf() {
        return this;
    }

    @Override
    public void enable() {
        if (!this.setupNMS()) {
            this.error("Unsupported server version.");
            this.getPluginManager().disablePlugin(this);
            return;
        }

        if (Config.OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS.get()) {
            PlayerBlockTracker.initialize();
            PlayerBlockTracker.BLOCK_FILTERS.add(block -> true);
        }

        new CreatorManager(this).setup();

        this.actionRegistry = new ActionRegistry(this);
        this.actionRegistry.setup();

        this.challengeManager = new ChallengeManager(this);
        this.challengeManager.setup();
    }

    @Override
    public void disable() {
        if (this.challengeManager != null) this.challengeManager.shutdown();
        if (this.actionRegistry != null) this.actionRegistry.shutdown();

        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderHook.shutdown();
        }
    }

    private boolean setupNMS() {
        this.challengeNMS = switch (Version.getCurrent()) {
            case V1_18_R2 -> new V1_18_R2();
            case V1_19_R3 -> new V1_19_R3();
            case V1_20_R2 -> new V1_20_R2();
            default -> null;
        };
        return this.challengeNMS != null;
    }

    @Override
    public boolean setupDataHandlers() {
        this.dataHandler = DataHandler.getInstance(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(Config.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(Lang.class);
        this.getLangManager().loadEnum(EntityDamageEvent.DamageCause.class);
        this.getLang().saveChanges();
    }

    @Override
    public void registerHooks() {
        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderHook.setup(this);
        }
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<ExcellentChallengesPlugin> mainCommand) {
        mainCommand.addDefaultCommand(new OpenCommand(this));
        mainCommand.addChildren(new ResetCommand(this));
        mainCommand.addChildren(new RerollTokensCommand(this));
        mainCommand.addChildren(new ReloadSubCommand<>(this, Perms.COMMAND_RELOAD));
    }

    @Override
    public void registerPermissions() {
        this.registerPermissions(Perms.class);
    }

    @Override
    @NotNull
    public DataHandler getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @NotNull
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    @NotNull
    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    @NotNull
    public ChallengeNMS getChallengeNMS() {
        return challengeNMS;
    }
}
