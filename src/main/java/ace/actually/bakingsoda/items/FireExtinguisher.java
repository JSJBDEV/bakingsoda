package ace.actually.bakingsoda.items;

import ace.actually.bakingsoda.BakingSoda;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class FireExtinguisher extends Item implements PolymerItem {
    public FireExtinguisher(Properties properties) {
        super(properties);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.STICK;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(hand==InteractionHand.MAIN_HAND && !level.isClientSide())
        {
            if(player.getOffhandItem().is(BakingSoda.BAKING_SODA))
            {
                player.getOffhandItem().shrink(1);
            }
            else
            {
                return super.use(level, player, hand);
            }


            for (int i = -5; i < 5; i++) {
                for (int j = -5; j < 5; j++) {
                    for (int k = -5; k < 5; k++) {
                        if(level.getBlockState(player.blockPosition().offset(i,j,k)).is(BlockTags.FIRE))
                        {
                            BlockPos p = player.blockPosition().offset(i,j,k);
                            level.playSound(null,p.getX(),p.getY(),p.getZ(), SoundEvents.WIND_CHARGE_BURST, SoundSource.BLOCKS,1,1);
                            level.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }
        return super.use(level, player, hand);
    }
}
