package io.github.wouink.furnish.setup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dev.architectury.platform.Platform;
import io.github.wouink.furnish.Furnish;

import java.io.*;
import java.nio.file.Path;

public class FurnishConfig {
	public boolean onlyMailTaggedItemsInMailbox = true;
	public boolean nonOpCreativePlayersCanDestroyMailbox = false;

	public static FurnishConfig INSTANCE;

	public static void load() {
		Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
		File configFile = getConfigFile().toFile();
		INSTANCE = new FurnishConfig();

		if(configFile.exists()) {
			try {
				FileReader fileReader = new FileReader(configFile);
				JsonReader reader = new JsonReader(fileReader);
				INSTANCE = gson.fromJson(reader, FurnishConfig.class);
				writeConfig(gson, configFile, INSTANCE);
			} catch (Exception e) {
				Furnish.LOG.error("Failed to load config file, the following error was encountered...");
				e.printStackTrace();
			}
		} else writeConfig(gson, configFile, INSTANCE);
	}

	public static void writeConfig(Gson gson, File configFile, FurnishConfig furnishConfig) {
		try {
			FileWriter fileWriter = new FileWriter(configFile);
			gson.toJson(furnishConfig, fileWriter);
		} catch (Exception e) {
			Furnish.LOG.error("Failed to write config file, the following error was encoutered...");
			e.printStackTrace();
		}
	}

	public static Path getConfigFile() {
		return Platform.getConfigFolder().resolve(Furnish.MODID + ".json");
	}
}
