package io.github.andrew6rant.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestBlockEntityRendererMixin<T extends BlockEntity & LidOpenable> {
    private boolean isEnder = false;
    private float tickDelta = 0;


    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/ChestBlockEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
                    ordinal = 2, shift = At.Shift.AFTER))
    private void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        //System.out.println(tickDelta);

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
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    ordinal = 2, shift = At.Shift.BEFORE))
    private void renderInject2(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay, CallbackInfo ci) {
        matrices.pop();
    }
}
