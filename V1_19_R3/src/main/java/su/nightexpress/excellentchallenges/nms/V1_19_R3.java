package su.nightexpress.excellentchallenges.nms;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBrewingStand;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.Reflex;

import java.lang.reflect.Method;

public class V1_19_R3 implements ChallengeNMS {

    private static final Method BREWING_A = Reflex.getMethod(TileEntityBrewingStand.class, "a", NonNullList.class);

    @Override
    public boolean canBrew(@NotNull BrewingStand stand) {
        if (BREWING_A == null) return true;

        CraftWorld craftWorld = (CraftWorld) stand.getWorld();
        TileEntity tile = craftWorld.getHandle().c_(new BlockPosition(stand.getX(), stand.getY(), stand.getZ()));
        if (tile == null) return false;

        //if (!(tile instanceof TileEntityBrewingStand)) return false;
        //TileEntityBrewingStand te = (TileEntityBrewingStand) tile;

        NonNullList<?> list = (NonNullList<?>) Reflex.getFieldValue(tile, "m");
        if (list == null) return false;

        Object val = Reflex.invokeMethod(BREWING_A, null, list);
        return val != null && (boolean) val;
    }
}
