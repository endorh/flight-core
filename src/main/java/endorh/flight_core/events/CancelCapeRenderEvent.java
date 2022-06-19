package endorh.flight_core.events;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Generates before a player's cape is rendered<br>
 * Cancelling this event will prevent the cape from rendering
 */
@Cancelable
public class CancelCapeRenderEvent extends Event {
	/**
	 * Player being rendered
	 */
	public final AbstractClientPlayerEntity player;
	
	public CancelCapeRenderEvent(AbstractClientPlayerEntity player) {
		this.player = player;
	}
}
