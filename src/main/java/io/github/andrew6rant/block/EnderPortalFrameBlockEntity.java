package io.github.andrew6rant.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static io.github.andrew6rant.SurveilMod.ENDER_PORTAL_FRAME_BLOCK_ENTITY;

public class EnderPortalFrameBlockEntity extends BlockEntity {
    public EnderPortalFrameBlockEntity(BlockPos pos, BlockState state) {
        super(ENDER_PORTAL_FRAME_BLOCK_ENTITY, pos, state);
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        return super.onSyncedBlockEvent(type, data);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnderPortalFrameBlockEntity frame) {
        //if(world.isClient) {
//
        //}
    }
}
