package net.outmoded.packer;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePack {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);

    }

    private final ConcurrentHashMap<String, byte[]> byteData = new ConcurrentHashMap<>(); // new file name : data

    public static ResourcePack create(){
        return new ResourcePack();
    }

    private ResourcePack(){
    }

    public void testWriteJson(String newPath, JsonTemplate template) {
        try {
            byteData.put(newPath, OBJECT_MAPPER.writeValueAsString(template).getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testWrite(String newPath, Path path){
        if (!Files.exists(path))
            return; // run error

        if (Files.isDirectory(path))
            return; // run error

        try (InputStream input = Files.newInputStream(path)) {
            byteData.put(newPath, input.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public CompletableFuture<Void> write(String writePath, int compressionLevel, boolean debug){

        return CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(writePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            ZipOutputStream zipOut = new ZipOutputStream(bos);
            zipOut.setLevel(compressionLevel);

            for (Map.Entry<String, byte[]> entry : byteData.entrySet()){
                String key = entry.getKey();
                byte[] bytes = entry.getValue();

                if (debug)
                    System.out.println("writing file: "+key);
                try {
                    ZipEntry zipEntry = new ZipEntry(key);
                    zipOut.putNextEntry(zipEntry);

                    zipOut.write(bytes, 0, bytes.length);



                    zipOut.closeEntry();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            long duration = System.currentTimeMillis() - startTime;

            if (debug)
                System.out.println("time taken: "+duration+"ms");

            try {
                zipOut.close();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
