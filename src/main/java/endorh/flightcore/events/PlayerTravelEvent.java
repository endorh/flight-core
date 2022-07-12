package endorh.flightcore.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Generates on every livingTick(), when the player motion should be updated.
 * The field travelVector contains the travelVector for that tick.
 * You may want to read the implementations for the travel method
 * in the {@linkplain LivingEntity} and {@linkplain PlayerEntity} classes
 *
 * @see LivingEntity#travel(Vector3d)
 * @see PlayerEntity#travel(Vector3d)
 */
@Cancelable
public class PlayerTravelEvent extends Event {
	/**
	 * Player being ticked
	 */
	public final PlayerEntity player;
	/**
	 * Travel vector for the event:
	 * {@linkplain Vector3d}(moveStrafing, moveVertical, moveForward)
	 */
	public final Vector3d travelVector;
	
	public PlayerTravelEvent(PlayerEntity player, Vector3d travelVector) {
		this.player = player;
		this.travelVector = travelVector;
	}
	
	/**
	 * Generates on every livingTick() of
	 * {@link net.minecraft.client.entity.player.RemoteClientPlayerEntity}s,
	 * which do not call their {@link PlayerEntity#travel} method
	 * @see PlayerTravelEvent
	 */
	public static class RemotePlayerEntityTravelEvent extends Event {
		/**
		 * Player being ticked
		 */
		public final PlayerEntity player;
		
		public RemotePlayerEntityTravelEvent(PlayerEntity player) {
			this.player = player;
		}
	}
}
