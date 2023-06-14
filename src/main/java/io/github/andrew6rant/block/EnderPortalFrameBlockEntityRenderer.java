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
        //matrices.push();
        if(entity.hasWorld()) {
            String eye_path;
            World world = entity.getWorld();
            BlockPos pos = entity.getPos();
            PlayerEntity playerEntity = world.getClosestPlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 10.0, false);
            if (playerEntity != null) {
                //path = "eye_1.png";
                double calcX = playerEntity.getX() - ((double)pos.getX() + 0.5);
                //double calcY = playerEntity.getY() - ((double)pos.getY() + 0.5);
                double calcZ = playerEntity.getZ() - ((double)pos.getZ() + 0.5);

                //this.eye.roll = -(float)(calcX / Math.PI);
                //this.eye.pitch = (float)(calcZ / Math.PI);

                //this.pupil.setPivot((int)(8+(float)calcX/3), pupil.pivotY, (int)(8+(float)calcZ/3));

                //old//this.eye.pitch = (float) MathHelper.clamp(MathHelper.atan2(calcZ, calcY), -0.2f, 0.2f);
                //old//this.eye.roll = (float) MathHelper.clamp(MathHelper.atan2(-calcX, calcY), -0.2f, 0.2f);

                //this.eye.pitch = (float) MathHelper.clamp(MathHelper.atan2(calcZ/10, (0.1+calcY)/10), -0.25f, 0.25f);
                //this.eye.roll = (float) MathHelper.clamp(MathHelper.atan2(-calcX/10, (0.1+calcY)/10), -0.25f, 0.25f);

                this.eye.roll = -(float)MathHelper.clamp((calcX / (2.5*Math.PI)), -0.25, 0.25);
                this.eye.pitch = (float)MathHelper.clamp((calcZ / (2.5*Math.PI)), -0.25, 0.25);

                int angleX = (int)(this.eye.roll * 10);
                int angleZ = (int)(this.eye.pitch * 10);
                //waSystem.out.println("angleX: "+angleX);

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

                /*path += switch (angleZ) {
                    case -2 -> "_2.png";
                    case -1 -> "_1.png";
                    case 0 -> "0.png";
                    case 1 -> "1.png";
                    case 2 -> "2.png";
                    default -> "";
                };*/




                //System.out.println("pitch: "+this.eye.pitch+", roll: "+this.eye.roll);
                //this.pupil.pitch = (float) MathHelper.clamp(MathHelper.atan2(calcZ, calcY), -0.2f, 0.2f);
                //this.pupil.roll = (float) MathHelper.clamp(MathHelper.atan2(-calcX, calcY), -0.2f, 0.2f);
                //this.pupil.pivotX = (int)(8+(float)calcX/3);
                //this.pupil.pivotZ = (int)(8+(float)calcZ/3);

                //this.eye.yaw = (float) MathHelper.atan2(calcX, calcZ);

                //this.eye.yaw = (float) MathHelper.clamp(MathHelper.atan2(calcX, calcZ), -0.5f, 0.5f);
                //System.out.println(-calcX);

                //this.eye.pitch = (float) MathHelper.clamp(calcZ, -0.5f, 0.5f); // (float) MathHelper.atan2(e, d);
                //this.eye.roll = (float) MathHelper.clamp(calcY, -0.5f, 0.5f); // (float) MathHelper.atan2(e, d);
                //this.eye.yaw = (float) MathHelper.clamp(calcX, -0.5f, 0.5f); // (float) MathHelper.atan2(e, d);
                //System.out.println(calcX);
                //this.eye.yaw = (float) MathHelper.clamp(MathHelper.atan2(calcX, calcZ), -0.3f, 0.3f);
                //this.eye.roll = (float) MathHelper.clamp(MathHelper.atan2((-calcX)/2, calcZ/2), -0.3f, 0.3f);
                //this.eye.roll = (float) MathHelper.atan2((-calcX)/2, calcZ/2);
                //this.eye.pitch = (float) MathHelper.clamp(MathHelper.atan2(calcZ, calcY), -0.3f, 0.3f);
            } else {
                eye_path = "eye.png";
            }
            VertexConsumer eyeConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(new Identifier("surveil", "textures/entity/eye/"+eye_path)));
            this.eye.render(matrices, eyeConsumer, light, overlay);
        }

        //this.pupil.render(matrices, eyeConsumer, light, overlay);
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(new Identifier("surveil", "textures/entity/frame.png")));
        this.base.render(matrices, baseConsumer, light, overlay);
    }
}
