package com.github.scoliossis.base.mixin.net.minecraft.client.renderer.entity;

import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.impl.client.ShowRotationsModule;
import com.github.scoliossis.base.utils.C;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {
    @Shadow @Final private static Logger logger;
    @Shadow protected ModelBase mainModel;
    @Shadow protected boolean renderOutlines;

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Shadow protected abstract float interpolateRotation(float par1, float par2, float par3);
    @Shadow protected abstract void renderLivingAt(T entityLivingBaseIn, double x, double y, double z);
    @Shadow protected abstract float handleRotationFloat(T livingBase, float partialTicks);
    @Shadow protected abstract void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks);
    @Shadow protected abstract void preRenderCallback(T entitylivingbaseIn, float partialTickTime);
    @Shadow protected abstract boolean setScoreTeamColor(T entityLivingBaseIn);
    @Shadow protected abstract void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor);
    @Shadow protected abstract void unsetScoreTeamColor();
    @Shadow protected abstract boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks);
    @Shadow protected abstract void unsetBrightness();
    @Shadow protected abstract void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_);
    @Shadow protected abstract float getSwingProgress(T livingBase, float partialTickTime);

    @Unique
    private RendererLivingEntity<T> getThis() {
        return (RendererLivingEntity<T>) ((Object)this);
    }

    /**
     * @author Lifix
     * @reason Server-side rotations my beloved
     */
    @Overwrite
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //if (!C.eventBus.post(new RenderLivingEvent.Pre<>(entity, this.getThis(), x, y, z))) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            boolean shouldSit = entity.isRiding() && entity.ridingEntity != null && entity.ridingEntity.shouldRiderSit();
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = entity.isChild();

            float bodyYaw = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float headYaw = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

            if (C.p() == entity) {
                if (ModuleManager.isEnabled(ShowRotationsModule.class)) {
                    if (ShowRotationsModule.yaw != C.p().rotationYawHead) {
                        bodyYaw = ShowRotationsModule.yaw;
                        headYaw = ShowRotationsModule.yaw;
                    }

                    // dumb if statement or smth
                    if (ShowRotationsModule.pitch != C.p().rotationPitch) {
                        pitch = ShowRotationsModule.pitch;
                    }
                }
            }

            try {
                float f = bodyYaw;
                float f2 = headYaw - f;
                float f8;
                if (shouldSit && entity.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f2 = headYaw - f;
                    f8 = MathHelper.wrapAngleTo180_float(f2);
                    if (f8 < -85.0F) {
                        f8 = -85.0F;
                    }

                    if (f8 >= 85.0F) {
                        f8 = 85.0F;
                    }

                    f = headYaw - f8;
                    if (f8 * f8 > 2500.0F) {
                        f += f8 * 0.2F;
                    }
                }

                this.renderLivingAt(entity, x, y, z);
                f8 = this.handleRotationFloat(entity, partialTicks);
                this.rotateCorpse(entity, f8, f, partialTicks);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(entity, partialTicks);
                GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
                float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                if (entity.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }

                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                this.mainModel.setRotationAngles(f6, f5, f8, f2, pitch, 0.0625F, entity);
                boolean flag;
                if (this.renderOutlines) {
                    flag = this.setScoreTeamColor(entity);
                    this.renderModel(entity, f6, f5, f8, f2, pitch, 0.0625F);
                    if (flag) {
                        this.unsetScoreTeamColor();
                    }
                } else {
                    flag = this.setDoRenderBrightness(entity, partialTicks);
                    this.renderModel(entity, f6, f5, f8, f2, pitch, 0.0625F);
                    if (flag) {
                        this.unsetBrightness();
                    }

                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f6, f5, partialTicks, f8, f2, pitch, 0.0625F);
                    }
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception ex) {
                logger.error("Couldn't render entity", ex);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!this.renderOutlines) {
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }

            //C.eventBus.post(new RenderLivingEvent.Post<>(entity, this.getThis(), x, y, z));
        //}
    }
}
