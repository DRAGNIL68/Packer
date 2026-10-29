# Packer: simple resource pack generation
Packer is a simple library, to allow any developer to generate Minecraft resource packs programmatically.

### Why this?:
  - fast and easy to use: I wanted a tool that was not overly complicated, and would allow anyone to generate packs fast and with minimal effort.
  - why so simple?: Instead of implementing every single feature that a resource pack can do, I provide a simple way to easily add only what you need. That way no matter what version you use this library still works fine.

### Dependency:
  maven
  ```
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.DRAGNIL68</groupId>
    <artifactId>Packer</artifactId>
    <version>v1.1</version>
</dependency>
```
gradle
```
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.DRAGNIL68:Packer:v1.1'
}

```
## Example
### how to generate a pack
```java
ResourcePack resourcePack = new ResourcePack();
try {
    resourcePack.writeJson(new McMeta("hello",  75));
    resourcePack.write("assets/folder/texture.png", Path.of("plugins/example/texture.png"));
} catch (IOException | IllegalArgumentException e) {
    System.out.println("file path error: "+e.getMessage());
}


CompletableFuture<Void> future = resourcePack.generate("C:/Users/linga/Downloads/testzip.zip", (Deflater.DEFAULT_COMPRESSION), true);
```

### how to make a custom json element 
```java
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
```

### what maps to what?
```java
@JsonProperty("single_value")
String string = "hello"; // this turns into "single_value": hello

@JsonProperty("list")
String[] list = {"1 frog", "2 frog", "3 frog"}; // "array": ["1 frog", "2 frog", "3 frog"]

@JsonProperty("hashmap")
Map<String, Object> pack = new HashMap<>(); // {"pack":{"description":"hello","pack_format":400}}
pack.put("pack_format", 400);
pack.put("description", "hello");


```
