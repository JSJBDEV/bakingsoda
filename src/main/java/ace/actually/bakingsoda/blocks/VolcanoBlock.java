package ace.actually.bakingsoda.blocks;

import ace.actually.bakingsoda.BakingSoda;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.SimpleMapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Brightness;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class VolcanoBlock extends BaseEntityBlock implements BlockWithElementHolder, PolymerBlock {

    public static BooleanProperty ERUPTING = BooleanProperty.create("erupting");
    public VolcanoBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ERUPTING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ERUPTING,false);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(VolcanoBlock::new);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, @Nullable PacketContext context) {

        return Blocks.LIGHT_GRAY_CARPET.defaultBlockState();
    }

    @Override
    public @org.jetbrains.annotations.Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        ItemStack stick = new ItemStack(BakingSoda.VOLCANO_BLOCK.asItem());

        ItemDisplayElement element = new ItemDisplayElement(stick);
        element.setBrightness(Brightness.FULL_BRIGHT);
        ElementHolder holder = new ElementHolder();
        holder.addElement(element);
        return holder;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new VolcanoBlockEntity(worldPosition,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, BakingSoda.VOLCANO_BLOCK_ENTITY,VolcanoBlockEntity::tick);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if(entity instanceof ItemEntity item && level instanceof ServerLevel sl)
        {
            if(item.getItem().is(BakingSoda.BAKING_SODA))
            {
                level.setBlockAndUpdate(pos,state.setValue(ERUPTING,true));
                VolcanoBlockEntity vbe = (VolcanoBlockEntity) level.getBlockEntity(pos);
                vbe.setTicks(500);
                entity.kill(sl);
            }
        }
    }
}
