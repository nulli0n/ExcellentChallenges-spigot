package su.nightexpress.excellentchallenges;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.command.list.ReloadSubCommand;
import su.nexmedia.engine.utils.EngineUtils;
import su.nexmedia.playerblocktracker.PlayerBlockTracker;
import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.command.OpenCommand;
import su.nightexpress.excellentchallenges.command.RerollTokensCommand;
import su.nightexpress.excellentchallenges.command.ResetCommand;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.DataHandler;
import su.nightexpress.excellentchallenges.data.UserManager;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.excellentchallenges.hooks.external.PlaceholderHook;
import su.nightexpress.excellentchallenges.nms.*;

import java.sql.SQLException;

public class ExcellentChallenges extends NexPlugin<ExcellentChallenges> implements UserDataHolder<ExcellentChallenges, ChallengeUser> {

    private DataHandler dataHandler;
    private UserManager userManager;

    private ChallengeManager challengeManager;
    private ChallengeNMS     challengeNMS;

    @Override
    @NotNull
    protected ExcellentChallenges getSelf() {
        return this;
    }

    @Override
    public void enable() {
        if (!this.setupNMS()) {
            this.error("Could not setup internal NMS Handler. Disabling...");
            this.getPluginManager().disablePlugin(this);
            return;
        }

        if (Config.OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS.get()) {
            PlayerBlockTracker.initialize(this);
        }

        //LazyGen.go();
        this.challengeManager = new ChallengeManager(this);
        this.challengeManager.setup();
    }

    @Override
    public void disable() {
        PlayerBlockTracker.shutdown();
        if (this.challengeManager != null) {
            this.challengeManager.shutdown();
            this.challengeManager = null;
        }
    }

    private boolean setupNMS() {
        this.challengeNMS = switch (Version.getCurrent()) {
            case V1_17_R1 -> new V1_17_R1();
            case V1_18_R2 -> new V1_18_R2();
            case V1_19_R3 -> new V1_19_R3();
            case V1_20_R1 -> new V1_20_R1();
            default -> null;
        };
        return this.challengeNMS != null;
    }

    @Override
    public boolean setupDataHandlers() {
        try {
            this.dataHandler = DataHandler.getInstance(this);
            this.dataHandler.setup();
        }
        catch (SQLException e) {
            this.error("Could not setup data handler!");
            return false;
        }

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
        this.getLangManager().loadEnum(ChallengeJobType.class);
        this.getLangManager().loadEnum(EntityDamageEvent.DamageCause.class);
        this.getLang().saveChanges();
    }

    @Override
    public void registerHooks() {
        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderHook.setup();
        }
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<ExcellentChallenges> mainCommand) {
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
    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    @NotNull
    public ChallengeNMS getChallengeNMS() {
        return challengeNMS;
    }
}
