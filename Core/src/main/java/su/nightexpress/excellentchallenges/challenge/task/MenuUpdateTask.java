package su.nightexpress.excellentchallenges.challenge.task;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.impl.Menu;
import su.nexmedia.engine.api.server.AbstractTask;
import su.nightexpress.excellentchallenges.ExcellentChallenges;

public class MenuUpdateTask extends AbstractTask<ExcellentChallenges> {

    private final Menu<ExcellentChallenges> menu;

    public MenuUpdateTask(@NotNull ExcellentChallenges plugin, @NotNull Menu<ExcellentChallenges> menu) {
        super(plugin, 1, false);
        this.menu = menu;
    }

    @Override
    public void action() {
        this.menu.update();
    }
}
