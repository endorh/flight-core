# Flight Core
> [!IMPORTANT]
> Since Minecraft 1.20 this mod has been merged with
> [`LazuLib`](https://github.com/endorh/lazulib)
> and is no longer available as a separate mod.

This core mod adds two cancellable events to allow mods to tweak the way the player moves and rotates
 
 - `PlayerTravelEvent` - Fired when the player's position is updated
 - `PlayerTurnEvent` - Fired when the player turns its view around

Mods can subscribe to these events and cancel them to prevent the default behaviour

Another event can disable the elytra check game rule conditionally for certain players
 - `DisableElytraCheckEvent` - Allows skipping the elytra check game rule per player

A few rendering related events are also provided:
 - `SetupRotationsRenderPlayerEvent` - Fired when rotating the rendered player model
 - `CancelCapeRenderEvent` - Allows hiding the cape for players conditionally

Another event is provided to alter the item generated in the item frame at End ships.
 - `GenerateEndShipItemFrameEvent` - Fired when the elytra item frame
   is being generated in an End ship.
