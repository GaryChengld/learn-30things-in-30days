# Day 26: Upload / Download file by Restful API

Today I'm going to create Restful API by Spring Boot to upload/download files

## Initialize Spring-boot project
Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47598556-3c3a9380-d96b-11e8-8855-91853dae0a60.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

## Create application properties file
>application.yml

```yaml
#Application configuration
server:
  port: 9080

spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 100MB
      max-request-size: 125MB

file:
  upload-dir: c:/tmp/files/uploads
```

## FileProperties class to load file properties from application.yml

```java
@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {
    private String uploadDir;
}
```

## Main application
```java
@SpringBootApplication
@EnableConfigurationProperties({FileProperties.class})
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
```

## Upload file

** Create UploadResponse.java class**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private String fileName;
    private String downloadUri;
    private String fileType;
    private long fileSize;
}
```

**Create FileStorageService.java**
```java
@Service
@Slf4j
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileProperties fileProperties) {
        log.debug("upload directory:{}", fileProperties.getUploadDir());
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(this.fileStorageLocation)) {
            log.debug("Upload directory not exists");
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory.", ex);
            }
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.debug("storeFile filename:{}", fileName);
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Store file " + fileName + "failed", ex);
        }
    }
}
```

**FileController**
```java
@RestController
@RequestMapping(value = "/v1/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public UploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
        return new UploadResponse(fileName, downloadUri, file.getContentType(), file.getSize());
    }
}
```

## Test upload

Now I'm testing upload api by Postman

- Start application
- Open Postman
  - Method: POST
  - URL: http://localhost:9080/v1/file/upload
  - Body: form-data
  
  <img width="880" src="https://user-images.githubusercontent.com/3359299/47599305-377cdc00-d979-11e8-9624-4039e5bec4b8.PNG" />
- Select any file and click "Send" button, it shows following json

```json
{
    "fileName": "sandupetrasco.png",
    "downloadUri": "http://localhost:9080/download/sandupetrasco.png",
    "fileType": "image/png",
    "fileSize": 14305
}
``` 

And the file is stored in directory C:\tmp\files\uploads

## Download file

**Add method in FileStorageService**

```java
    public Resource loadFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
```

**Add method to FileController**

```java
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFile(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
```

**Test result**
- Open Postman
  - Method: GET
  - URL: http://localhost:9080/v1/file/download/sandupetrasco.png
  
  <img width="880" src="https://user-images.githubusercontent.com/3359299/47599499-701eb480-d97d-11e8-9f54-b841f4b2a198.PNG" />
  
 That's all for today, you can find the complete source code under [this folder](.).  

