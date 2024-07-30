# Furnish Changelog

## Furnish versioning

- Each release of Furnish is only compatible with one version of Minecraft.
- There will be no backport of any feature in an earlier Minecraft version.
- This goes for bug fixes too. Bug fixes are only available in the next version of Furnish.

This set of rules is the only way I can work on Furnish efficiently on my limited time, thanks for your understanding!

## Furnish v27 for Minecraft 1.21 (in development)

- **TODO** Fix an exploit with `C2S_UpdateItemStack` message
- **TODO** Fix carpet on stairs/trapdoors placement on Fabric and Quilt
- **TODO** Allow Endermen to despawn when they spawn with a plate
- Fix Stone Brick Chimney Conduit recipe (#47)

## Furnish v26 for Minecraft 1.20.1

- Block tags are now usable as item tags as well (except for configuration tags)
- A bunch of new tags available, both as block tags and item tags. See "New block tags"
- Fix client initialization on Quilt (which resulted in containers with missing GUIs) (#43)
- Fix "Add arms to Armor-Stand" and "Cycle paintings" not working on Quilt
- Painting cycling can now be disabled by configuring the `#furnish:can_cycle` item tag (#42)
- Carpets on stairs and on trapdoors can now be disabled with `#furnish:place_on_stairs` and `#furnish:place_on_trapdoor` block tags
- Snow on stairs and on fences can now be disabled with `#furnish:place_on_stairs` and `#furnish:place_on_fence` block tags
- Update `it_it.json`. Thanks Zano1999!
- Update `zh_tw.json`. Thanks Lobster0228!

### New block tags

- `#furnish:bedside_tables`
- `#furnish:benches`
- `#furnish:cabinets`
- `#furnish:chairs`
- `#furnish:chimney_conduits`
- `#furnish:coffins`
- `#furnish:crates`
- `#furnish:kitchen_cabinets`
- `#furnish:lockers`
- `#furnish:log_benches`
- `#furnish:mailboxes`
- `#furnish:pedestal_tables`
- `#furnish:recycle_bins`
- `#furnish:shelves`
- `#furnish:shutters`
- `#furnish:square_tables`
- `#furnish:stools`
- `#furnish:tables`
- `#furnish:wardrobes`

## Furnish v25 for Minecraft 1.20.1

- Add Mangrove, Cherry and Bamboo furniture!
- Rework the texture of most Shutters
- Book Pile now provides enchanting power
- Fix painting cycling not properly saving new art
- Fix loot table parsing error for Furnish Cobweb
- Fix "In the Mail" advancement parsing
- Fix spruce furniture closing sound
- All Shutters can now use a transparent texture
- Adding a Ladder to a group of Ladders now makes a sound

## Furnish v24 for Minecraft 1.20.1

- Update to 1.20.1
- Furnish is now available for Forge, Fabric and Quilt!
- Shutters can now be interacted with through windows
- Fix #31 (Bookshelf Chest items lost on breaking)
- Fix #36 (Shelf duplication bug)
