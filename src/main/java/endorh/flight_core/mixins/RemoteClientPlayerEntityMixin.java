package endorh.flight_core.mixins;

import com.mojang.authlib.GameProfile;
import endorh.flight_core.events.PlayerEntityTravelEvent.RemotePlayerEntityTravelEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link RemotePlayerEntityTravelEvent} on
 * {@link RemotePlayer#aiStep()}
 */
@Mixin(RemotePlayer.class)
public class RemoteClientPlayerEntityMixin extends AbstractClientPlayer {
	/**
	 * Dummy mixin constructor, required by the Java compiler to inherit from superclass.
	 * @param world ignored
	 * @param profile ignored
    * @throws IllegalAccessException always
	 */
	private RemoteClientPlayerEntityMixin(ClientLevel world, GameProfile profile) throws IllegalAccessException {
		super(world, profile);
		throw new IllegalAccessException("Mixin dummy constructor shouldn't be called");
	}
	
	/**
	 * Inject {@link RemotePlayerEntityTravelEvent} on
	 * {@link RemotePlayer#aiStep()}.
	 * @param callbackInfo Mixin {@link CallbackInfo}
	 */
	@Inject(
	  method = "aiStep",
	  at = @At(
	    value = "INVOKE_STRING", args = "ldc=push",
	    target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V"))
	protected void _flightcore_livingTick(CallbackInfo callbackInfo) {
		MinecraftForge.EVENT_BUS.post(new RemotePlayerEntityTravelEvent(this));
	}
}
