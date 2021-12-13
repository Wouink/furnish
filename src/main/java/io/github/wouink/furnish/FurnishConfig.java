package io.github.wouink.furnish;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FurnishConfig {
	public static final ForgeConfigSpec FORGE_CONFIG_SPEC;
	public static final FurnishConfig FURNISH_CONFIG;

	public final ForgeConfigSpec.BooleanValue restrictMailboxItems;

	static {
		Pair<FurnishConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(FurnishConfig::new);
		FORGE_CONFIG_SPEC = specPair.getRight();
		FURNISH_CONFIG = specPair.getLeft();
	}

	public FurnishConfig(ForgeConfigSpec.Builder builder) {
		restrictMailboxItems = builder.comment("Restrict items that can be inserted by other players in a Mailbox to items tagged #furnish:mail?")
				.define("restrictMailboxItems", true);
	}
}
