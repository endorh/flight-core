package endorh.flight_core.events;

import net.minecraft.client.MouseHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Generates on every client game loop, when the player orientation
 * should be updated. The event fields x and y represent the
 * accumulated mouse input for the frame in each axis.
 * Usually this values are scaled down, multiplying by 0.15D.
 * The event is cancellable. If cancelled, the default rotation
 * behaviour won't take place.
 *
 * @see PlayerEntity#rotateTowards(double, double)
 * @see MouseHelper#updatePlayerLook()
 */
@Cancelable
public class PlayerEntityRotateEvent extends Event {
	/**
	 * Player associated to the event
	 */
	public final PlayerEntity player;
	/**
	 * Accumulated mouse input on the x axis for this frame. Usually
	 * scaled down multiplying by 0.15D
	 *
	 * @see PlayerEntity#rotateTowards(double, double)
	 * @see MouseHelper#updatePlayerLook()
	 */
	public final double x;
	/**
	 * Accumulated mouse input on the y axis for this frame. Usually
	 * scaled down multiplying by 0.15D
	 *
	 * @see PlayerEntity#rotateTowards(double, double)
	 * @see MouseHelper#updatePlayerLook()
	 */
	public final double y;
	
	public PlayerEntityRotateEvent(PlayerEntity player, double x, double y) {
		super();
		this.player = player;
		this.x = x;
		this.y = y;
	}
}
