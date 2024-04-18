package su.nightexpress.excellentchallenges;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import su.nightexpress.excellentchallenges.challenge.action.ActionRegistry;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;
import su.nightexpress.excellentchallenges.command.OpenCommand;
import su.nightexpress.excellentchallenges.command.RerollTokensCommand;
import su.nightexpress.excellentchallenges.command.ResetCommand;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.data.DataHandler;
import su.nightexpress.excellentchallenges.data.UserManager;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.hook.impl.PlaceholderHook;
import su.nightexpress.excellentchallenges.nms.*;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.command.base.ReloadSubCommand;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;

public class ChallengesPlugin extends NightDataPlugin<ChallengeUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private ActionRegistry   actionRegistry;
    private ChallengeManager challengeManager;
    private ChallengeNMS     challengeNMS;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Challenges", new String[]{"excellentchallenges", "challenges"})
            .setConfigClass(Config.class)
            .setLangClass(Lang.class)
            .setPermissionsClass(Perms.class);
    }

    @Override
    public void enable() {
        if (!this.setupNMS()) {
            this.error("Unsupported server version.");
            this.getPluginManager().disablePlugin(this);
            return;
        }

        Keys.load(this);

        this.registerCommands();
        this.getLangManager().loadEnum(EntityDamageEvent.DamageCause.class);

        this.dataHandler = new DataHandler(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        if (Config.OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS.get()) {
            PlayerBlockTracker.initialize();
            PlayerBlockTracker.BLOCK_FILTERS.add(block -> true);
        }

        new CreatorManager(this).setup();

        this.actionRegistry = new ActionRegistry(this);
        this.actionRegistry.setup();

        this.challengeManager = new ChallengeManager(this);
        this.challengeManager.setup();

        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.setup(this);
        }
    }

    @Override
    public void disable() {
        if (this.challengeManager != null) this.challengeManager.shutdown();
        if (this.actionRegistry != null) this.actionRegistry.shutdown();

        if (Plugins.hasPlaceholderAPI()) {
            PlaceholderHook.shutdown();
        }
    }

    private boolean setupNMS() {
        this.challengeNMS = switch (Version.getCurrent()) {
            case V1_18_R2 -> new V1_18_R2();
            case V1_19_R3 -> new V1_19_R3();
            case V1_20_R1 -> new V1_20_R1();
            case V1_20_R2 -> new V1_20_R2();
            case V1_20_R3 -> new V1_20_R3();
            default -> null;
        };
        return this.challengeNMS != null;
    }

    private void registerCommands() {
        NightPluginCommand mainCommand = this.getBaseCommand();

        mainCommand.addDefaultCommand(new OpenCommand(this));
        mainCommand.addChildren(new ResetCommand(this));
        mainCommand.addChildren(new RerollTokensCommand(this));
        mainCommand.addChildren(new ReloadSubCommand(this, Perms.COMMAND_RELOAD));
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
