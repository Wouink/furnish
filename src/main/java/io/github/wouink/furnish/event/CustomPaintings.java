package io.github.wouink.furnish.event;

import com.google.gson.*;
import io.github.wouink.furnish.FurnishManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class CustomPaintings {

	public static void registerCustomPaintings(IForgeRegistry<PaintingType> paintingRegistry, String jsonFilePath) {
		FurnishManager.Furnish_Logger.info(String.format("Custom paintings: attempt to load file %s.", jsonFilePath));
		File f = new File(jsonFilePath);
		if(f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(f.toPath())));
				Gson builder = new GsonBuilder().create();
				JsonObject paintingsFile = builder.fromJson(reader, JsonElement.class).getAsJsonObject();
				JsonArray paintings = paintingsFile.getAsJsonArray("paintings");
				if(paintings != null) {
					for(int index = 0; index < paintings.size(); index++) {
						JsonObject painting = paintings.get(index).getAsJsonObject();
						String name;
						int w, h;
						if(painting.has("name")) name = painting.get("name").getAsString();
						else {
							FurnishManager.Furnish_Logger.error(String.format("%s: Painting at index %d has no name. Skipping.", jsonFilePath, index));
							continue;
						}
						if(painting.has("w")) w = painting.get("w").getAsInt();
						else {
							FurnishManager.Furnish_Logger.error(String.format("%s: Painting with name %s at index %d has no width. Skipping.", jsonFilePath, name, index));
							continue;
						}
						if(painting.has("h")) h = painting.get("h").getAsInt();
						else {
							FurnishManager.Furnish_Logger.error(String.format("%s: Painting with name %s at index %d has no height. Skipping.", jsonFilePath, name, index));
							continue;
						}
						if(w < 1 || h < 1) {
							FurnishManager.Furnish_Logger.error(String.format("%s: Painting with name %s at index %d has invalid dimension. Skipping.", jsonFilePath, name, index));
							continue;
						}
						paintingRegistry.register(FurnishManager.createPainting(name, w, h));
					}
				} else FurnishManager.Furnish_Logger.error("No paintings element in paintings.json file.");
			} catch (IOException e) {
				e.printStackTrace();
				FurnishManager.Furnish_Logger.error(String.format("Could not load %s.", jsonFilePath));
			}
		} else {
			FurnishManager.Furnish_Logger.info(String.format("File not found: %s", jsonFilePath));
		}
	}
}
