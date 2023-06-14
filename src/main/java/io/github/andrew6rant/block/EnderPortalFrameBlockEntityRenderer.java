package io.github.andrew6rant.block;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EnderPortalFrameBlockEntityRenderer implements BlockEntityRenderer<EnderPortalFrameBlockEntity> {
    private final ModelPart base;
    private final ModelPart eye;


    public EnderPortalFrameBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelData baseData = new ModelData();
        ModelData eyeData = new ModelData();
        ModelPartData basePartData = baseData.getRoot();
        ModelPartData eyePartData = eyeData.getRoot();

        basePartData.addChild("base", ModelPartBuilder.create()
                .uv(0, 0).cuboid(
                    0f, 0f, 0f,
                    16f, 13f, 16f), ModelTransform.NONE);

        eyePartData.addChild("eye", ModelPartBuilder.create()
                .uv(0, 0).cuboid(
                    -4f, -2.5f, -4f,
                    8f, 5f, 8f), ModelTransform.NONE);

        this.base = basePartData.createPart(64, 32);
        this.eye = eyePartData.createPart(32, 16);
        this.eye.setPivot(8f, 13f, 8f);
    }
    @Override
    public void render(EnderPortalFrameBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(entity.hasWorld()) {
            String eye_path;
            World world = entity.getWorld();
            BlockPos pos = entity.getPos();
            PlayerEntity playerEntity = world.getClosestPlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 10.0, false);
            if (playerEntity != null) {
                double calcX = playerEntity.getX() - ((double)pos.getX() + 0.5);
                //double calcY = playerEntity.getY() - ((double)pos.getY() + 0.5);
                double calcZ = playerEntity.getZ() - ((double)pos.getZ() + 0.5);

                this.eye.roll = -(float)MathHelper.clamp((calcX / (2.5*Math.PI)), -0.25, 0.25);
                this.eye.pitch = (float)MathHelper.clamp((calcZ / (2.5*Math.PI)), -0.25, 0.25);

                int angleX = (int)(this.eye.roll * 10);
                int angleZ = (int)(this.eye.pitch * 10);

                eye_path = switch (angleX) {
                    case -2 -> "eye_2";
                    case -1 -> "eye_1";
                    case 0 -> "eye0";
                    case 1 -> "eye1";
                    case 2 -> "eye2";
                    default -> "eye";
                };

                eye_path += switch (angleZ) {
                    // yeah I know I got it backwards, I am too lazy to fix all the filenames haha
                    case 2 -> "_2.png";
                    case 1 -> "_1.png";
                    case 0 -> "0.png";
                    case -1 -> "1.png";
                    case -2 -> "2.png";
                    default -> "";
                };
            } else {
                eye_path = "eye.png";
            }
            VertexConsumer eyeConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(new Identifier("surveil", "textures/entity/eye/"+eye_path)));
            this.eye.render(matrices, eyeConsumer, light, overlay);
        }

        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(new Identifier("surveil", "textures/entity/frame.png")));
        this.base.render(matrices, baseConsumer, light, overlay);
    }
}
