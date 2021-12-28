package io.github.wouink.furnish.setup;

import net.minecraftforge.common.ForgeConfigSpec;

public class FurnishConfig {
	private final ForgeConfigSpec spec;

	public final ForgeConfigSpec.BooleanValue restrictMailboxItems;

	public FurnishConfig() {
		final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment("Restrict items that can be inserted by other players in a Mailbox to items tagged #furnish:mail?");
		restrictMailboxItems = builder.define("restrictMailboxItems", true);
		spec = builder.build();
	}

	public ForgeConfigSpec getSpec() {
		return spec;
	}
}
