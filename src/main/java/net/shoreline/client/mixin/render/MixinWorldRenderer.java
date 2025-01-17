package net.shoreline.client.mixin.render;

import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.RenderWorldBorderEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.util.Globals;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements Globals
{
    /**
     *
     *
     * @param matrices
     * @param tickDelta
     * @param limitTime
     * @param renderBlockOutline
     * @param camera
     * @param gameRenderer
     * @param lightmapTextureManager
     * @param positionMatrix
     * @param ci
     */
    @Inject(method = "render", at = @At(value = "RETURN"))
    private void hookRender(MatrixStack matrices, float tickDelta,
                            long limitTime, boolean renderBlockOutline,
                            Camera camera, GameRenderer gameRenderer,
                            LightmapTextureManager lightmapTextureManager,
                            Matrix4f positionMatrix, CallbackInfo ci)
    {
        Vec3d pos = mc.getBlockEntityRenderDispatcher().camera.getPos();
        matrices.translate(-pos.x, -pos.y, -pos.z);
        final RenderWorldEvent renderWorldEvent =
                new RenderWorldEvent(matrices, tickDelta);
        Shoreline.EVENT_HANDLER.dispatch(renderWorldEvent);
    }

    /**
     *
     * @param camera
     * @param ci
     */
    @Inject(method = "renderWorldBorder", at = @At(value = "HEAD"), cancellable = true)
    private void hookRenderWorldBorder(Camera camera, CallbackInfo ci)
    {
        RenderWorldBorderEvent renderWorldBorderEvent =
                new RenderWorldBorderEvent();
        Shoreline.EVENT_HANDLER.dispatch(renderWorldBorderEvent);
        if (renderWorldBorderEvent.isCanceled())
        {
            ci.cancel();
        }
    }
}
