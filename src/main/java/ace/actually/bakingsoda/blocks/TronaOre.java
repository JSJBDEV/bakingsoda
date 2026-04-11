package ace.actually.bakingsoda.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class TronaOre extends Block implements PolymerBlock {
    public TronaOre(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, @Nullable PacketContext context) {
        return Blocks.SMOOTH_QUARTZ.defaultBlockState();
    }
}
