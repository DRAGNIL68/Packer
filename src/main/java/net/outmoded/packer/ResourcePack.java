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

    private final ConcurrentHashMap<String, byte[]> byteData = new ConcurrentHashMap<>(); // new file path : data

    public ResourcePack(){}

    /**
     *
     * @param template JSON file
     */
    public void writeJson(JsonTemplate template) throws JsonProcessingException{
        byteData.put(template.getFilePath(), OBJECT_MAPPER.writeValueAsString(template).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * don't use this if you don't know how
     * @throws IOException thrown if it cant read the InputStream
     */

    public void writeInputStream(String rpPath, InputStream inputStream) throws IOException{
        byteData.put(rpPath, inputStream.readAllBytes());
    }

    public void write(String rpPath, Path path) throws IOException, IllegalArgumentException{
        if (!Files.exists(path.getParent())){
            throw new IllegalArgumentException("path does not exist");
        }
        
        if (Files.isDirectory(path)){
            throw new IllegalArgumentException("path cannot be a directory");
        }
        
        try (InputStream input = Files.newInputStream(path)) {
            byteData.put(rpPath, input.readAllBytes());
        }
    }


    /**
     * generatest the rp
     */
    public CompletableFuture<Void> generate(String writePath, int compressionLevel, boolean debug){



        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            ConcurrentHashMap<String, byte[]> byteDataSnapshot = new ConcurrentHashMap<>(byteData); // async safety I hope

            long startTime = System.currentTimeMillis();
            FileOutputStream fos;
            try {
                Path path = Path.of(writePath).getParent();

                if (!Files.isDirectory(path)){
                    Files.createDirectories(path);
                }

                fos = new FileOutputStream(writePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            ZipOutputStream zipOut = new ZipOutputStream(bos);
            zipOut.setLevel(compressionLevel);

            for (Map.Entry<String, byte[]> entry : byteDataSnapshot.entrySet()){
                String key = entry.getKey();
                byte[] bytes = entry.getValue();

                if (debug)
                    log("writing file: "+key);

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
                log("time taken: "+duration+"ms");

            try {
                zipOut.close();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        future.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return future;
    }

    /**
     * disposes of data in resource pack
     */
    public void clear(){
        byteData.clear();
    }

    /**
     * if your using this in a Minecraft plugin override this
     * to your plugins logger to avoid PaperMc complaining
     */
    public void log(String string){
        System.out.println(string);
    }
}
