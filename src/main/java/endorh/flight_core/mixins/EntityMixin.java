package endorh.flight_core.mixins;

import endorh.flight_core.events.PlayerEntityRotateEvent;
import net.minecraft.client.MouseHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link PlayerEntityRotateEvent} on {@link PlayerEntity#turn}.<br>
 * The mixin is applied to {@linkplain Entity} instead of
 * {@linkplain PlayerEntity} because the method is inherited from
 * {@linkplain Entity} in {@linkplain PlayerEntity}
 */
@Mixin(Entity.class)
public abstract class EntityMixin
  extends CapabilityProvider<Entity> {
	/**
	 * Irrelevant constructor required by the Java compiler
	 */
	private EntityMixin(Class<Entity> baseClass) { super(baseClass); }
	
	/**
	 * Inject {@link PlayerEntityRotateEvent} on {@link PlayerEntity#turn}.<br>
	 *
	 * The event is cancellable. If cancelled, the rotateTowards method
	 * call will be skipped.<br>
	 *
	 * Applied here rather than on the PlayerEntity class because
	 * the method is inherited.<br>
	 *
	 * @param yaw Unscaled relative yaw rotation
	 * @param pitch Unscaled relative pitch rotation
	 * @param callbackInfo Mixin {@linkplain CallbackInfo}
	 *
	 * @see PlayerEntity#turn(double, double)
	 * @see MouseHelper#turnPlayer()
	 */
	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	public void _flightcore_rotateTowards(double yaw, double pitch, CallbackInfo callbackInfo) {
		// noinspection ConstantConditions
		if ((CapabilityProvider<Entity>)this instanceof PlayerEntity) {
			PlayerEntityRotateEvent event = new PlayerEntityRotateEvent(
			  (PlayerEntity)(CapabilityProvider<Entity>)this, yaw, pitch);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.isCanceled()) callbackInfo.cancel();
		}
	}
}
