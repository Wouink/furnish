package io.github.wouink.furnish.event;

import com.google.gson.*;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class CustomPaintings {

	public static void registerCustomPaintings(IForgeRegistry<PaintingType> paintingRegistry, String jsonFilePath) {
		Furnish.LOG.info(String.format("Custom paintings: attempt to load file %s.", jsonFilePath));
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
							Furnish.LOG.error(String.format("%s: Painting at index %d has no name. Skipping.", jsonFilePath, index));
							continue;
						}
						if(painting.has("w")) w = painting.get("w").getAsInt();
						else {
							Furnish.LOG.error(String.format("%s: Painting with name %s at index %d has no width. Skipping.", jsonFilePath, name, index));
							continue;
						}
						if(painting.has("h")) h = painting.get("h").getAsInt();
						else {
							Furnish.LOG.error(String.format("%s: Painting with name %s at index %d has no height. Skipping.", jsonFilePath, name, index));
							continue;
						}
						if(w < 1 || h < 1) {
							Furnish.LOG.error(String.format("%s: Painting with name %s at index %d has invalid dimension. Skipping.", jsonFilePath, name, index));
							continue;
						}
						paintingRegistry.register(FurnishData.createPainting(name, w, h));
					}
				} else Furnish.LOG.error("No paintings element in paintings.json file.");
			} catch (IOException e) {
				e.printStackTrace();
				Furnish.LOG.error(String.format("Could not load %s.", jsonFilePath));
			}
		} else {
			Furnish.LOG.info(String.format("File not found: %s", jsonFilePath));
		}
	}
}
