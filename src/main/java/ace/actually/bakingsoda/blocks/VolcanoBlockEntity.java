package ace.actually.bakingsoda.blocks;

import ace.actually.bakingsoda.BakingSoda;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.awt.*;

public class VolcanoBlockEntity extends BlockEntity {
    long ticks = 0;
    int colour = 16777215;

    public VolcanoBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BakingSoda.VOLCANO_BLOCK_ENTITY, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putLong("ticks",ticks);
        output.putInt("colour",colour);
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ticks = input.getLongOr("ticks",0);
        colour = input.getIntOr("colour",16777215);
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
        setChanged();
    }

    public void setColour(int colour) {
        this.colour = colour;
        setChanged();
    }
    public void setDye(ItemStack stack)
    {
        if(stack.has(DataComponents.DYE))
        {
            setColour(stack.get(DataComponents.DYE).getFireworkColor());
        }
    }
    public void setDyedItem(ItemStack stack)
    {
        if(stack.has(DataComponents.DYED_COLOR))
        {
            setColour(stack.get(DataComponents.DYED_COLOR).rgb());
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, VolcanoBlockEntity be)
    {
        if(be.ticks>0)
        {
            be.ticks--;
            if(be.ticks==0)
            {
                level.setBlockAndUpdate(pos,blockState.setValue(VolcanoBlock.ERUPTING,false));
            }
        }

        if(level instanceof ServerLevel serverLevel && blockState.getValue(VolcanoBlock.ERUPTING))
        {
            for (int i = 0; i < level.getRandom().nextInt(5); i++) {
                if (level.getRandom().nextInt(3) == 0) {
                    serverLevel.sendParticles(ParticleTypes.LAVA,pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,1,0, level.getRandom().nextFloat() / 2.0F,0,1);
                }
                if (level.getRandom().nextInt(3) == 0) {
                    serverLevel.sendParticles(new DustParticleOptions(be.colour,3),pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,1,0, level.getRandom().nextFloat() / 2.0F,0,0.2);
                }
                if(level.getRandom().nextInt(3)==0)
                {
                    serverLevel.sendParticles(ParticleTypes.FIREWORK,pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,1,0, level.getRandom().nextFloat() / 2.0F,0,0.2);
                }
            }

        }

    }
}
