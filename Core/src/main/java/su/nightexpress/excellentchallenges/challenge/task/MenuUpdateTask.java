package su.nightexpress.excellentchallenges.challenge.task;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.task.AbstractTask;
import su.nightexpress.excellentchallenges.ExcellentChallenges;

public class MenuUpdateTask extends AbstractTask<ExcellentChallenges> {

    private final AbstractMenu<ExcellentChallenges> menu;

    public MenuUpdateTask(@NotNull AbstractMenu<ExcellentChallenges> menu) {
        super(menu.plugin, 1, false);
        this.menu = menu;
    }

    @Override
    public void action() {
        this.menu.update();
    }
}
