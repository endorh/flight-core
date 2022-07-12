package endorh.flightcore.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import endorh.flightcore.events.CancelCapeRenderEvent;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link CancelCapeRenderEvent} in
 * {@link CapeLayer#render}.
 */
@Mixin(CapeLayer.class)
public class CapeLayerMixin {
	/**
	 * Inject {@link CancelCapeRenderEvent} in {@link CapeLayer#render}.<br>
	 * The event is cancellable. If cancelled, the cape layer will not render<br>
	 * Otherwise, may render as normal, if the player has a cape.
	 * @param player Player being rendered
	 * @param ci Mixin {@link CallbackInfo}
	 */
	@Inject(at = @At("HEAD"), method = "render*", cancellable = true)
	public void _flightcore_cancelRender(
	  MatrixStack mStack, IRenderTypeBuffer buffer, int packedLight,
	  AbstractClientPlayerEntity player, float limbSwing,
	  float limbSwingAmount, float partialTicks, float ageInTicks,
	  float netHeadYaw, float headPitch, CallbackInfo ci
	) {
		if (MinecraftForge.EVENT_BUS.post(new CancelCapeRenderEvent(player)))
			ci.cancel();
	}
}
