# Welcome to Furnish

Furnish adds lovely and useful Furniture to Minecraft, each one having it's *little something*.
Furnish takes away the ton of crafting required by other furniture mods by adding a Stonecutter-like table: the *Furniture Workbench*.

* [Furnish on Curse](https://www.curseforge.com/minecraft/mc-mods/furnish)
* [Furnish on Modrinth](https://modrinth.com/mod/furnish-furniture)

## Screenshots

![Kitchen Example](https://media.forgecdn.net/attachments/408/875/kitchen_1280x.png)
![Dark Meeting Room](https://media.forgecdn.net/attachments/408/874/meeting_1280x.png)

## Compatibility

Mod/modpack makers can easily add a recipe for their block in the workbench, with the new recipe type `furnish:furniture_making`.
Here's an example with [Market Crates](https://www.curseforge.com/minecraft/mc-mods/market-crates) crates.

```json
{
	"type": "furnish:furniture_making",
	"ingredient": { "item": "minecraft:acacia_planks" },
	"result": "marketcrates:acacia_crate",
	"count": 1
}
```

## Configuring Furnish

Every configuration of Furnish is done with native Minecraft datapack features.

* Items tagged `#furnish:crate_blacklist` can't be put inside Crates (by default, this tag only contains Crates).
* Items tagged `#furnish:music_discs` can be put in Disk Racks alongside all Record items.
* Items tagged `#furnish:food` can be put in Plates alongside all edible items.
* Items tagged `#furnish:mail` are the only items allowed in Mailboxes but you can enable all items to be mailed if you tag the Mailbox with the block tag `#furnish:bypasses_mail_tag`. (Furnish v22+)
* Mailboxes can only be destroyed by op creative players. You can allow all creative players to destroy Mailboxes if you tag the Mailbox with the block tag `#furnish:non_op_creative_can_destroy`. (Furnish v22+)
* All door blocks are tagged with `#furnish:can_knock_on`. If you wish to disable door knocking on server side, just remove all doors from this block tag. (Furnish v23+)
* If you wish to disable door knocking sound **on client side**, you can enable the Door Silencer resource pack included with Furnish (no need to download it somewhere, it's packaged in the mod file). (Furnish v23+)
* Lectern books can be popped out with Left Click (as in Minecraft Bedrock Edition). If you wish to disable that, remove the Lectern from the block tag `#furnish:can_pop_book`. (Furnish v23+)
