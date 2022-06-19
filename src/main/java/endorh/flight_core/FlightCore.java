package endorh.flight_core;

import endorh.flight_core.events.ApplyRotationsRenderPlayerEvent;
import endorh.flight_core.events.PlayerEntityRotateEvent;
import endorh.flight_core.events.PlayerEntityTravelEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Flight Core mod<br>
 * Injects the following 3 events to vanilla using mixins
 * <ul>
 *    <li>{@link PlayerEntityRotateEvent}</li>
 *    <li>{@link PlayerEntityTravelEvent}</li>
 *    <li>{@link ApplyRotationsRenderPlayerEvent}</li>
 * </ul>
 */
@Mod(FlightCore.MOD_ID)
public class FlightCore {
    public static final String MOD_ID = "flightcore";
    /**
     * Nothing to do
     */
    public FlightCore() {}
}