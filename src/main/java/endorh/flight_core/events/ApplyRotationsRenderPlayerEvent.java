package endorh.flight_core.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;

import java.util.function.Consumer;

/**
 * Generates on every call to {@link PlayerRenderer#setupRotations},
 * when the rotations applied to the {@link PlayerModel} are performed.
 * The event is cancellable. If cancelled, the default rotation
 * behaviour won't take place.
 * It's possible to run the super method by using the {@code callSuper}
 * {@linkplain Consumer} provided with the event.
 */
@Cancelable
public class ApplyRotationsRenderPlayerEvent extends Event {
	/**
	 * Player being rendered
	 */
	public final AbstractClientPlayerEntity player;
	/**
	 * Source of the event
	 */
	public final PlayerRenderer renderer;
	/**
	 * Transformation matrix stack
	 */
	public final MatrixStack matrixStack;
	/**
	 * Age of player in ticks, containing the partial fraction.
	 * Is equal to {@code player.ticksExisted + partialTicks}
	 */
	public final float ageInTicks;
	/**
	 * Smoothed yaw for this frame, interpolated between
	 * {@code player.prevRotationYaw} and {@code player.rotationYaw}
	 * by {@code partialTicks}
	 */
	public final float rotationYaw;
	/**
	 * Interpolation progress between this tick and the next (0~1)
	 */
	public final float partialTicks;
	/**
	 * Consumer that may be used to call the super method,
	 * {@link LivingRenderer#render}. The required 
	 * {@linkplain Vector3f} should contain the 3 float arguments
	 * of the call.
	 */
	public final Consumer<Vector3f> callSuper;
	
	public ApplyRotationsRenderPlayerEvent(
	  PlayerRenderer renderer, AbstractClientPlayerEntity player,
	  MatrixStack mStack, float ageInTicks, float rotationYaw,
	  float partialTicks, Consumer<Vector3f> callSuper
	) {
		super();
		this.player = player;
		this.renderer = renderer;
		this.matrixStack = mStack;
		this.ageInTicks = ageInTicks;
		this.rotationYaw = rotationYaw;
		this.partialTicks = partialTicks;
		this.callSuper = callSuper;
	}
}
