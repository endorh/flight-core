package endorh.flightcore.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility to log mixin errors and warnings once
 */
public class FailedMixinLogger {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static boolean logServerPlayNetHandler$shouldDisableElytraCheck = false;
	public static void missedServerPlayNetHandler$shouldDisableElytraCheck() {
		if (!logServerPlayNetHandler$shouldDisableElytraCheck) {
			LOGGER.warn("Mixin ServerPlayNetHandler$shouldDisableElytraCheck missed " +
			            "its target. Conditional elytra check disabling may be " +
			            "impossible for mods relying on this mixin.\n" +
			            "You may report this bug in the \"Flight Core\" mod issue tracker");
			logServerPlayNetHandler$shouldDisableElytraCheck = true;
		}
	}
}
