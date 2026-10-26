package net.outmoded.packer;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.outmoded.packer.basictemplates.GeneratedItemModel;
import net.outmoded.packer.basictemplates.McMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.zip.Deflater;

public class ExampleJson{

    // this is just an example of how to generate a pack
    public static void main(String[] args) {
        ResourcePack resourcePack = new ResourcePack();
        try {
            resourcePack.writeJson(new McMeta("hello",  75));
            resourcePack.writeJson(new GeneratedItemModel("test:folder/test.png", GeneratedItemModel.modelType.generated, "assets/test/models/item/test.json"));

        } catch (IOException e) {
            System.out.println("cant find file "+e.getMessage());
        }


        CompletableFuture<Void> future = resourcePack.generate("C:/Users/linga/Downloads/testzip.zip", (Deflater.DEFAULT_COMPRESSION), true);


        while (!future.isDone()){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
