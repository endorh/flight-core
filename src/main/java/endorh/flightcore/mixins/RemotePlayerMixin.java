package endorh.flightcore.mixins;

import com.mojang.authlib.GameProfile;
import endorh.flightcore.events.PlayerTravelEvent.RemotePlayerEntityTravelEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects {@link RemotePlayerEntityTravelEvent} on
 * {@link RemotePlayer#aiStep()}
 */
@Mixin(RemotePlayer.class)
@OnlyIn(Dist.CLIENT)
public class RemotePlayerMixin extends AbstractClientPlayer {
	/**
	 * Dummy mixin constructor, required by the Java compiler to inherit from superclass.
	 * @param level ignored
	 * @param profile ignored
	 * @param ppk ignored
    * @throws IllegalAccessException always
	 */
	private RemotePlayerMixin(
	  ClientLevel level, GameProfile profile, @Nullable ProfilePublicKey ppk
	) throws IllegalAccessException {
		super(level, profile, ppk);
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
