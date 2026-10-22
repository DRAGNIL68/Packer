package net.outmoded.packer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.zip.Deflater;

public class ExampleJson implements JsonTemplate{

    @JsonProperty("hello_frog")
    private String hello = "frog";

    @JsonProperty("hello_frog123")
    private String[] frog = {"adad", "adada"," frog123"};

    private String fileName;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public @NotNull String getFilePath() {
        return fileName;
    }

    @Override
    public void setFilePath(String newFilePath) {
        fileName = newFilePath;
    }

    // this is jsut an example of how to generate a pack
    public static void main(String[] args) {
        ResourcePack resourcePack = ResourcePack.create();
        resourcePack.testWrite("balls.png" , Path.of("C:/Users/linga/Downloads/image.png"));
        resourcePack.testWrite("frog/leaf.jar",Path.of("C:/Users/linga/Downloads/leaf-1.21.11-179.jar"));
        resourcePack.testWriteJson("mc.meta", new ExampleJson());
        CompletableFuture<Void> future = resourcePack.write("C:/Users/linga/Downloads/testzip.zip", (Deflater.DEFAULT_COMPRESSION), true);


        while (!future.isDone()){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
