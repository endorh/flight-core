# Flight Core
This core mod adds three cancellable events to allow mods to tweak the way the player moves and rotates
 
 - `PlayerEntityTravelEvent` - Fired when the player motion is updated
 - `PlayerEntityRotateEvent` - Fired when the player look is updated
 - `ApplyRotationsRenderPlayerEvent` - Fired when rotating the player model

Mods can subscribe to these events and cancel them to prevent the default behaviour
Additionally, an event is provided to alter the item generated in the
item frame at End ships.
 - `GenerateEndShipItemFrameEvent` - Fired when the elytra item frame
is being generated in an end ship.

Mods can subscribe to this event and change its result to replace the elytra
found at end ships.