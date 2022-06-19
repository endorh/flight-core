package endorh.flight_core.mixins;

import endorh.flight_core.events.PlayerEntityTravelEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link PlayerEntityTravelEvent} on {@link PlayerEntity#travel}
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	/**
	 * Irrelevant constructor required by the Java compiler
	 */
	private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	/**
	 * Inject {@link PlayerEntityTravelEvent} on {@link PlayerEntity#travel}
	 * The event is cancellable. When cancelled, the travel method is skipped.
	 * @param travelVector {@linkplain Vector3d}(moveStrafing, moveVertical, moveForward)
	 * @param callbackInfo Mixin {@linkplain CallbackInfo}
	 *
	 * @see PlayerEntity#travel(Vector3d)
	 * @see LivingEntity#aiStep()
	 */
	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	public void _flightcore_travel(Vector3d travelVector, CallbackInfo callbackInfo) {
		//noinspection ConstantConditions
		PlayerEntityTravelEvent event = new PlayerEntityTravelEvent(
		  (PlayerEntity)(LivingEntity)this, travelVector);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) callbackInfo.cancel();
	}
}
