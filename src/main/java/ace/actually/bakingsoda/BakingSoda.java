package ace.actually.bakingsoda;

import ace.actually.bakingsoda.blocks.TronaOre;
import ace.actually.bakingsoda.blocks.VolcanoBlock;
import ace.actually.bakingsoda.blocks.VolcanoBlockEntity;
import ace.actually.bakingsoda.items.FireExtinguisher;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class BakingSoda implements ModInitializer {
	public static final String MOD_ID = "bakingsoda";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Block VOLCANO_BLOCK = register("volcano",VolcanoBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CARPET),true);
	public static final Block TRONA_ORE = register("trona_ore", TronaOre::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_QUARTZ_ORE),true);

	public static final Item BAKING_SODA = register("baking_soda",Item::new,new Item.Properties());
	public static final Item VINEGAR = register("vinegar",Item::new,new Item.Properties());
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", FireExtinguisher::new,new Item.Properties());

	public static final ResourceKey<PlacedFeature> TRONA_ORE_PLACED_KEY = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath("bakingsoda","ore_trona"));

	public static final BlockEntityType<VolcanoBlockEntity> VOLCANO_BLOCK_ENTITY =
			register("volcano", VolcanoBlockEntity::new, VOLCANO_BLOCK);


	public static final ResourceKey<CreativeModeTab> BAKINGSODA_TAB_KEY = ResourceKey.create(
			BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(MOD_ID, "creative_tab")
	);
	public static final CreativeModeTab BAKINGSODA_TAB = FabricCreativeModeTab.builder()
			.icon(() -> new ItemStack(BakingSoda.VOLCANO_BLOCK))
			.title(Component.translatable("creativeTab.bakingsoda"))
			.displayItems((params, output) -> {
				output.accept(BakingSoda.VOLCANO_BLOCK);
				output.accept(BakingSoda.TRONA_ORE);
				output.accept(BakingSoda.BAKING_SODA);
				output.accept(BakingSoda.VINEGAR);
				output.accept(BakingSoda.FIRE_EXTINGUISHER);
			})
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, TRONA_ORE_PLACED_KEY);

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BAKINGSODA_TAB_KEY, BAKINGSODA_TAB);

		PolymerResourcePackUtils.addModAssets("bakingsoda");
		LOGGER.info("Hello Fabric world!");
	}

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {

		ResourceKey<Block> blockKey = keyOfBlock(name);

		Block block = blockFactory.apply(settings.setId(blockKey));


		if (shouldRegisterItem) {

			ResourceKey<Item> itemKey = keyOfItem(name);
			PolymerBlockItem blockItem = new PolymerBlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
			Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
		}

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));

		// Create the item instance.
		T item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}


	private static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}




	private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}
}