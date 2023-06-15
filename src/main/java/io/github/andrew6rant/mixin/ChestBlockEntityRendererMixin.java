package io.github.andrew6rant.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestBlockEntityRendererMixin<T extends BlockEntity & LidOpenable> {

    @Mutable
    @Final
    @Shadow private final ModelPart singleChestLid;

    @Mutable
    @Final
    @Shadow private final ModelPart singleChestLatch;

    @Mutable
    @Final
    @Shadow private final ModelPart singleChestBase;
    private boolean isEnder = false;
    private float hoverCounter = 0;

    public ChestBlockEntityRendererMixin(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
        this.singleChestLid = modelPart.getChild("lid");
        this.singleChestLatch = modelPart.getChild("lock");
        this.singleChestBase = modelPart.getChild("bottom");
    }


    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/ChestBlockEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
                    ordinal = 2, shift = At.Shift.BEFORE), cancellable = true)
    private void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (isEnder) {
            VertexConsumer vertexConsumer = TexturedRenderLayers.ENDER.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
            if(entity.hasWorld()) {
                int directionOffset = 0;
                BlockState blockState = entity.getWorld().getBlockState(entity.getPos());
                if (blockState.getBlock() instanceof EnderChestBlock) {
                    Direction direction = blockState.get(EnderChestBlock.FACING);
                    switch (direction) {
                        case NORTH -> directionOffset = 90;
                        case EAST -> directionOffset = 180;
                        case SOUTH -> directionOffset = 270;
                    }
                }
                matrices.push();
                World world = entity.getWorld();
                BlockPos pos = entity.getPos();
                PlayerEntity playerEntity = world.getClosestPlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 10.0, false);
                if (playerEntity != null) {
                    if (hoverCounter > (12*Math.PI)) {
                        hoverCounter = 0;
                        singleChestLid.pivotY = 14;
                        singleChestLatch.pivotY = 14;
                    } else {
                        float pivotCalc = (float) ((Math.sin(hoverCounter) / 20f));
                        singleChestLid.pivotY += pivotCalc;
                        singleChestLatch.pivotY += pivotCalc;
                        hoverCounter += 0.01f;
                        //hoverCounter = 0;

                        //matrices.translate(0.5F, 0.0F, 0.5F);
                        //matrices.translate(0.4375, 0.0F, 0.4375); // because the lid is only 14px wide
                        double d = playerEntity.getX() - ((double)pos.getX() + 0.5);
                        double e = playerEntity.getZ() - ((double)pos.getZ() + 0.5);
                        //singleChestLid.pivotX = 8f;
                        //singleChestLid.pivotZ = 8f;
                        //singleChestLid.setPivot(8f, 9f, 8f);
                        //singleChestLid.
                        //singleChestLid.yaw = (float) -MathHelper.atan2(e, d);
                        //singleChestLid.pivotX = 0.0f;
                        //singleChestLid.pivotZ = 0.0f;
                        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) (-113F * MathHelper.atan2(e, d))));

                        matrices.translate(0.5F, 0.0F, 0.5F);
                        //matrices.multiply(direction.getRotationQuaternion());
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(directionOffset));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) MathHelper.atan2(e, -d)));
                        //matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(hoverCounter));
                        matrices.translate(-0.5F, 0.0F, -0.5F);

                        //System.out.println((float) (115F * MathHelper.atan2(e, d)));
                        //matrices.translate(-0.5F, 0.0F, -0.5F);

                        //matrices.translate(0.5F, 0.0F, 0.5F);
                    }
                }

                //matrices.translate(0.5F, 0.0F, 0.5F);
                singleChestLid.render(matrices, vertexConsumer, light, overlay);
                //matrices.translate(-0.5F, 0.0F, -0.5F);
                singleChestLatch.render(matrices, vertexConsumer, light, overlay);
                matrices.pop();
            } else {
                singleChestLid.render(matrices, vertexConsumer, light, overlay);
                singleChestLatch.render(matrices, vertexConsumer, light, overlay);
            }
            singleChestBase.render(matrices, vertexConsumer, light, overlay);
            matrices.pop();
            ci.cancel();
        }
    }

    @ModifyVariable(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
    at = @At("STORE"), ordinal = 0)
    private SpriteIdentifier captureChestTextureId(SpriteIdentifier spriteIdentifier) {
        if (spriteIdentifier.equals(TexturedRenderLayers.ENDER)) {
            isEnder = true;
        }
        //System.out.println(spriteIdentifier.getTextureId());
        return spriteIdentifier;
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
            at = @At("HEAD"))
    private void renderInject(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay, CallbackInfo ci) {
        /*
        matrices.push();
        if (isEnder) {
            //lid.setPivot(0.0F, 9f, 1.0F);
            //lid.setPivot(5.0F, 9f, 5.0F);
            if (tickDelta > (2*Math.PI)) {
                tickDelta = 0;
                lid.pivotY = 14;
                latch.pivotY = 14;
            } else {
                float pivotCalc = (float) ((Math.sin(tickDelta) / 20f));
                lid.pivotY += pivotCalc;
                latch.pivotY += pivotCalc;
                tickDelta += 0.01f;

                matrices.translate(0.5F, 0.0F, 0.5F);
                //matrices.translate(0.4375, 0.0F, 0.4375); // because the lid is only 14px wide
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(lid.pivotY));
                matrices.translate(-0.5F, 0.0F, -0.5F);
            }
        }
        //lid.setPivot(0.0F, 9f, 1.0F);
        */
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    ordinal = 2, shift = At.Shift.BEFORE))
    private void renderInject2(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay, CallbackInfo ci) {
        //matrices.pop();
    }
}
