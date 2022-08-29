package endorh.flightcore;

import endorh.flightcore.events.PlayerTurnEvent;
import endorh.flightcore.events.SetupRotationsRenderPlayerEvent;
import endorh.flightcore.events.PlayerTravelEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Flight Core mod<br>
 * Injects the following 3 events to vanilla using mixins
 * <ul>
 *    <li>{@link PlayerTurnEvent}</li>
 *    <li>{@link PlayerTravelEvent}</li>
 *    <li>{@link SetupRotationsRenderPlayerEvent}</li>
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