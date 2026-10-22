package net.outmoded.packer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JsonTemplate {

    public String getFileName();

    /**
     * Used to place the file in the correct location.
     * <p>
     * assets/namespace/folder/test.png -> namespace:folder/test.png
     */
    public @NotNull String getFilePath();

    public void setFilePath(String newFilePath);
}
