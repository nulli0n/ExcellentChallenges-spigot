package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;

import java.util.Map;
import java.util.Set;

public class GenObjectiveObject extends GenAmountObject {

    //private final String         name;
    //private final String         namesListId;
    //private final Set<ItemStack> icons;
    private final DifficultyValue progress;

    public GenObjectiveObject(@NotNull String id, double weight,
                              //@NotNull String name,
                              //@NotNull String namesListId,
                              //@NotNull Set<ItemStack> icons,
                              @NotNull Map<String, Set<String>> items,
                              @NotNull DifficultyValue amount,
                              @NotNull DifficultyValue progress
                              ) {
        super(id, weight, items, amount, false);
        //this.name = name;
        //this.namesListId = namesListId.toLowerCase();
        //this.icons = icons;
        this.progress = progress;
    }

    @NotNull
    public static GenObjectiveObject read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        GenAmountObject parent = GenAmountObject.read(cfg, path, id);

        /*String name = JOption.create(path + ".Name", StringUtil.capitalizeUnderscored(id),
            "Sets general name for all objectives specified in the 'Items' list.",
            "This name can be used to replace '" + Placeholders.GENERIC_NAME + "' placeholder in a name selected from 'Names_List' setting."
        ).read(cfg);

        String nameListId = JOption.create(path + ".Names_List", "null",
            "Determines the name list used for random name selection for the challenge.",
            "Then, name from the 'Name' setting above can be used to replace '" + Placeholders.GENERIC_NAME + "' placeholder in selected name.",
            "You can modify name lists in the '" + Config.NAMES_FILE + "' file in plugin's folder root."
        ).read(cfg);

        cfg.setComments(path + ".Icons", "List of challenge icons for random selection.");

        Set<ItemStack> icons = new HashSet<>();
        for (String sId : cfg.getSection(path + ".Icons")) {
            ItemStack icon = cfg.getItem(path + ".Icons." + sId);
            if (icon.getType().isAir()) continue;

            icons.add(icon);
        }*/

        cfg.setComments(path + ".Progress",
            "Determines the amount needs to be completed for each objective picked from the 'Items' list."
        );

        DifficultyValue progress = DifficultyValue.read(cfg, path + ".Progress");

        return new GenObjectiveObject(id, parent.getWeight(), /*name, nameListId, icons,*/ parent.getItems(), parent.getAmount(), progress);
    }

    @Override
    public void write(@NotNull JYML cfg, @NotNull String path) {
        super.write(cfg, path);

        /*cfg.set(path + ".Name", this.getName());
        cfg.set(path + ".Names_List", this.getNamesListId());

        int count = 0;
        for (ItemStack icon : this.getIcons()) {
            cfg.setItem(path + ".Icons." + (++count), icon);
        }*/

        this.getProgress().write(cfg, path + ".Progress");
    }

    /*@NotNull
    public ItemStack pickIcon() {
        if (this.getIcons().isEmpty()) return new ItemStack(Material.BARRIER);

        return Rnd.get(this.getIcons());
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getNamesListId() {
        return namesListId;
    }

    @NotNull
    public Set<ItemStack> getIcons() {
        return icons;
    }*/

    @NotNull
    public DifficultyValue getProgress() {
        return progress;
    }
}
