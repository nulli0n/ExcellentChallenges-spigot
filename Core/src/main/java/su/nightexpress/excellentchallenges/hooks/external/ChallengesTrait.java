package su.nightexpress.excellentchallenges.hooks.external;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;

@TraitName("challenges")
public class ChallengesTrait extends Trait {

    public ChallengesTrait() {
        super("challenges");
    }

    @EventHandler
    public void onClickLeft(NPCRightClickEvent e) {
        if (e.getNPC() == this.getNPC()) {
            Player player = e.getClicker();
            ExcellentChallengesAPI.getChallengeManager().getMainMenu().open(player, 1);
        }
    }
}
