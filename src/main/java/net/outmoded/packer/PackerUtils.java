package net.outmoded.packer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class PackerUtils {

    // stolen from StackOverflow
    public static UUID resourcePackIdToUuid(String resourcePackId){

        byte[] seedBytes = resourcePackId.getBytes(StandardCharsets.UTF_8);
        return UUID.nameUUIDFromBytes(seedBytes);
    }

    /**
     * splits a string into its parts, example "test:cool_sword" = [0]"test" [1]"cool_sword"
     */
    public static String[] splitNamespaceId(String namespaceId){
        String id = namespaceId.substring(namespaceId.lastIndexOf(':') + 1);
        String namespace = namespaceId.replace(":" + id, "");

        return new String[]{namespace, id};
    }

    /**
     * converts a namespaced id to a path
     */
    public String namespacedIdPath(String namespacedId){
        String[] split = splitNamespaceId(namespacedId);
        return "assets/"+split[0]+"/"+split[1];
    }

    /**
     * Converts a path to namespaced id.
     * <p>
     * Will only work if the path contains "assets/"
     */
    public String pathNamespacedId(String path){
        path = path.replaceFirst("assets/", "");
        return path.replaceFirst("/", ":");
    }

}
