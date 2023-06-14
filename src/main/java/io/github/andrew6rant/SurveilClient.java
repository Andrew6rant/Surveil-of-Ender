package io.github.andrew6rant;

import io.github.andrew6rant.block.EnderPortalFrameBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SurveilClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(SurveilMod.ENDER_PORTAL_FRAME_BLOCK_ENTITY, EnderPortalFrameBlockEntityRenderer::new);
    }
}
