package net.outmoded.packer.basictemplates;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.outmoded.packer.JsonTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.outmoded.packer.PackerUtils.splitNamespaceId;

public class GeneratedItemModel implements JsonTemplate {
    private final String writePath;

    @JsonProperty("parent")
    private String parent;

    @JsonProperty("textures")
    private final Map<String, String> textures = new HashMap<>();


    @JsonIgnore
    public GeneratedItemModel(String namespacedTexturePath, modelType modelType , String writePath){
        this.writePath = writePath;

        textures.put("layer0", splitNamespaceId(namespacedTexturePath)[0]+":item/"+splitNamespaceId(namespacedTexturePath)[1]);
        parent = "item/" + modelType.toString();
    }


    @Override
    public @NotNull String getFilePath() {
        return writePath;
    }


    public enum modelType{
        generated,
        handheld
    }

}
