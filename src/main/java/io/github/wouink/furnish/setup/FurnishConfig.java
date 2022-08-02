package io.github.wouink.furnish.setup;

import net.minecraftforge.common.ForgeConfigSpec;

public class FurnishConfig {
	private final ForgeConfigSpec spec;

	public final ForgeConfigSpec.BooleanValue restrictMailboxItems;
	public final ForgeConfigSpec.BooleanValue creativeDestroyMailbox;

	public FurnishConfig() {
		final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Restrict items that can be inserted by other players in a Mailbox to items tagged #furnish:mail?");
		restrictMailboxItems = builder.define("restrictMailboxItems", true);
		builder.comment("Allow all creative players to destroy claimed Mailboxes? By default, only op players are allowed.");
		creativeDestroyMailbox = builder.define("creativeDestroyMailbox", false);
		spec = builder.build();
	}

	public ForgeConfigSpec getSpec() {
		return spec;
	}
}
