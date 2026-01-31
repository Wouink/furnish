# Furnish Changelog

## Furnish versioning

- Each release of Furnish is only compatible with one version of Minecraft.
- There will be no backport of any feature in an earlier Minecraft version.
- This goes for bug fixes too. Bug fixes are only available in the next version of Furnish.

This set of rules is the only way I can work on Furnish efficiently on my limited time, thanks for your understanding!

## Furnish v29

- **TODO** Make Awnings bouncy!
- **TODO** Fix issue with seats (#46) (note for myself: probably client/server issues upon dismounting seat entity)
- **TODO?** Make AbstractFurnitureBlockEntity implements Clearable?

## Furnish v28 for Minecraft 1.21.1 (in development)

Furnish v28 is a complete rewrite of the mod.
It is no longer an Architectury mod, but rather a Fabric only mod.
Don't worry tho, you can still run Furnish on (Neo)Forge using [Sinytra Connector](https://modrinth.com/mod/connector)!

With this cleaner code, I hope to be able to update faster and with less pain in the future!

### Breaking changes

- Removed Bookshelf Chests as we now have Chiseled Bookshelves in vanilla
- Removed Coffins for now (need to be reworked)
- Reworked Plate/Shelf/Showcase logic: items are stored differently.
  **You will lose the contents of these furniture when updating your world.**
- Removed Paper furniture, Telescope, Halloween furniture, Dice, Asphalt, snow on fences and Chimney Conduits for now.
  I'm not happy with these features, they need to be reworked.
  
### Other changes

- Always allow placing Cake on Tables (make tables always solid) (#45)
- All furniture that hold items (Cabinets, Plates, Shelves etc.) can now be used with hoppers and have a loot table
- Rare Plates now spawn in Dungeon Chests rather than in Endermen's hands

## Furnish v27 for Minecraft 1.21

- Fix an exploit with `C2S_UpdateItemStack` message (#50, #52)
- Fix Stone Brick Chimney Conduit recipe (#47, #51)

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
