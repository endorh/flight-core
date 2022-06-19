package endorh.flight_core.mixins;

import com.mojang.authlib.GameProfile;
import endorh.flight_core.events.PlayerEntityTravelEvent.RemotePlayerEntityTravelEvent;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link RemotePlayerEntityTravelEvent} on
 * {@link RemoteClientPlayerEntity#aiStep()}
 */
@Mixin(RemoteClientPlayerEntity.class)
public class RemoteClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	/**
	 * Dummy constructor required by the Java compiler to inherit from superclass
	 * @param world ignored
	 * @param profile ignored
    * @throws IllegalAccessException always
	 */
	private RemoteClientPlayerEntityMixin(ClientWorld world, GameProfile profile) throws IllegalAccessException {
		super(world, profile);
		throw new IllegalAccessException("Mixin dummy constructor shouldn't be called");
	}
	
	/**
	 * Inject {@link RemotePlayerEntityTravelEvent} on
	 * {@link RemoteClientPlayerEntity#aiStep()}.
	 * @param callbackInfo Mixin {@link CallbackInfo}
	 */
	@Inject(
	  method = "aiStep",
	  at = @At(
	    value = "INVOKE_STRING", args = "ldc=push",
	    target = "Lnet/minecraft/profiler/IProfiler;push(Ljava/lang/String;)V"))
	protected void _flightcore_livingTick(CallbackInfo callbackInfo) {
		MinecraftForge.EVENT_BUS.post(new RemotePlayerEntityTravelEvent(this));
	}
}
