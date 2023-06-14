package io.github.andrew6rant;

import io.github.andrew6rant.block.EnderPortalFrameBlock;
import io.github.andrew6rant.block.EnderPortalFrameBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveilMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("surveil");

	public static final Block ENDER_PORTAL_FRAME_BLOCK = new EnderPortalFrameBlock(FabricBlockSettings.copyOf(Blocks.STONE));

	public static BlockEntityType<EnderPortalFrameBlockEntity> ENDER_PORTAL_FRAME_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("surveil","ender_portal_frame_block"),
			FabricBlockEntityTypeBuilder.create(EnderPortalFrameBlockEntity::new, ENDER_PORTAL_FRAME_BLOCK).build());

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier("surveil", "ender_portal_frame_block"), ENDER_PORTAL_FRAME_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("surveil", "ender_portal_frame_block"), new BlockItem(ENDER_PORTAL_FRAME_BLOCK, new Item.Settings()));
	}
}