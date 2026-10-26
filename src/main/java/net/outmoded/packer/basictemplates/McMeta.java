package net.outmoded.packer.basictemplates;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.outmoded.packer.JsonTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class McMeta implements JsonTemplate {

    @JsonProperty("pack") // <- all json properties must have a @JsonProperty annotation
    private final Map<String, Object> pack = new HashMap<>();

    public McMeta(String description, int version){
        pack.put("pack_format", version);
        pack.put("description", description);
    }

    @Override
    public @NotNull String getFilePath() {
        return "pack.mcmeta"; // mcmeta is always in the root of the rp.
    }

}
