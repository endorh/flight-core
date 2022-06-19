package endorh.flight_core.mixins;

import endorh.flight_core.events.GenerateEndShipItemFrameEvent;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.EndCityPieces;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.minecraftforge.eventbus.api.Event.Result.DENY;

/**
 * Injects {@link GenerateEndShipItemFrameEvent} on
 * {@link EndCityPieces.CityTemplate#handleDataMarker}.
 */
@Mixin(EndCityPieces.CityTemplate.class)
public abstract class EndCityPieces$CityTemplateMixin {
	/**
	 * Shadow field containing the rotation of the end ship
	 */
	@Shadow(aliases = "rotation") private @Final Rotation rotation;
	
	/**
	 * Inject {@link GenerateEndShipItemFrameEvent} on
	 * {@link EndCityPieces.CityTemplate#handleDataMarker}.<br>
	 *
	 * The event is cancellable. If cancelled, the default behaviour will
	 * be prevented, but no item frame will be added to the world.<br>
	 *
	 * The event has a result. If the result is other than
	 * {@code DENY} ({@code DEFAULT}, {@code ALLOW}) and the event was not
	 * cancelled, the default behaviour takes place.<br>
	 *
	 * If the result is {@code DENY}, and the event was not cancelled, the
	 * default behaviour is prevented, and an item frame with the item stack
	 * set by the event is added.<br>
	 *
	 * @param marker Data marker
	 * @param pos Block position of the marker
	 * @param world Server world where the structure is loaded
	 * @param rand Generation {@link Random} object
	 * @param sbb Structure bounding box
	 * @param callbackInfo Mixin {@link CallbackInfo}
	 */
	@Inject(method = "handleDataMarker", at = @At("HEAD"), cancellable = true)
	protected void _flightcore_handleDataMarker(
	  String marker, BlockPos pos, IServerWorld world, Random rand,
	  MutableBoundingBox sbb, CallbackInfo callbackInfo
	) {
		if (marker.startsWith("Elytra")) {
			ServerWorld serverWorld = world.getLevel();
			ItemFrameEntity itemFrame = new ItemFrameEntity(
			  serverWorld, pos, this.rotation.rotate(Direction.SOUTH));
			ItemStack elytraStack = new ItemStack(Items.ELYTRA);
			
			GenerateEndShipItemFrameEvent event = new GenerateEndShipItemFrameEvent(
			  serverWorld, pos, rand, sbb, itemFrame, elytraStack);
			MinecraftForge.EVENT_BUS.post(event);
			
			if (event.isCanceled()) {
				callbackInfo.cancel();
			} else if (event.getResult() == DENY) {
				callbackInfo.cancel();
				itemFrame.setItem(event.getElytraStack(), false);
				world.addFreshEntity(itemFrame);
			}
		}
	}
}
