package endorh.flight_core.mixins;

import endorh.flight_core.events.DisableElytraCheckEvent;
import endorh.flight_core.logging.FailedMixinLogger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameRules.RuleKey;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Redirects checks to {@link GameRules#RULE_DISABLE_ELYTRA_MOVEMENT_CHECK} to
 * {@link DisableElytraCheckEvent}, when the rules do not already disable
 * the check and a player is flying
 */
@Mixin(ServerPlayNetHandler.class)
public abstract class ServerPlayNetHandlerMixin {
	
	/**
	 * The player being processed
	 */
	@Shadow(aliases = "player") public ServerPlayerEntity player;
	
	@Shadow(aliases = "firstGoodX") private double firstGoodX;
	@Shadow(aliases = "firstGoodY") private double firstGoodY;
	@Shadow(aliases = "firstGoodZ") private double firstGoodZ;
	
	@Shadow(aliases = "movePacketCounter") private int movePacketCounter;
	@Shadow(aliases = "lastMovePacketCounter") private int lastMovePacketCounter;
	
	/**
	 * Redirect the query for the game rule
	 * {@link GameRules#RULE_DISABLE_ELYTRA_MOVEMENT_CHECK} when applying the
	 * speed check at {@link ServerPlayNetHandler#handleMovePlayer} to
	 * the event {@link DisableElytraCheckEvent}.<br>
	 *
	 * The redirection only takes place if the check is not already disabled
	 * unconditionally by the rules, and if the player is flying, since
	 * otherwise the redirect would serve no purpose.
	 *
	 * @param rules {@link GameRules} in effect
	 * @param key Key for the {@link GameRules#RULE_DISABLE_ELYTRA_MOVEMENT_CHECK} rule.
	 *            If it does not match, the mixin is considered as missed, and will
	 *            not interfere with the call, after logging a warning.
	 * @param packet The packet that triggered the check
	 * @return True if the check is disabled, no matter the reason.
	 */
	@Redirect(
	  method = "handleMovePlayer",
	  at = @At(
	    value = "INVOKE",
	    target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z",
	    ordinal = 0
	  )
	)
	public boolean _flightcore_shouldDisableElytraCheck(
	  GameRules rules, RuleKey<BooleanValue> key, CPlayerPacket packet
	) {
		// If the key is not DISABLE_ELYTRA_MOVEMENT_CHECK, the mixin missed its target
		if (key != GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK) {
			// Log a warning once
			FailedMixinLogger.missedServerPlayNetHandler$shouldDisableElytraCheck();
			// Return the value without interference
			return rules.getBoolean(key);
		}
		// If the check is disabled by the rules, leave it like that
		if (rules.getBoolean(key))
			return true;
		// If the player is not flying this mixin can't avoid the check, so simply return
		if (!player.isFallFlying())
			return false;
		// Mimic the check
		double pX = packet.getX(player.getX());
		double pY = packet.getY(player.getY());
		double pZ = packet.getZ(player.getZ());
		double dX = pX - firstGoodX;
		double dY = pY - firstGoodY;
		double dZ = pZ - firstGoodZ;
		double playerMotion2 = player.getDeltaMovement().lengthSqr();
		double playerDelta2 = dX * dX + dY * dY + dZ * dZ;
		double excess = playerDelta2 - playerMotion2;
		int stackedPackets = movePacketCounter - lastMovePacketCounter;
		// Post the event
		DisableElytraCheckEvent event = new DisableElytraCheckEvent(
		  player, packet, excess, stackedPackets);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getDisable();
	}
}
