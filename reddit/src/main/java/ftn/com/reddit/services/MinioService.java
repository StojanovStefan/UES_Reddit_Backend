package ftn.com.reddit.services;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName = "tvoj-bucket";

    public MinioService() {
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioaccesskey", "miniosecretkey")
                .build();
    }

    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(24 * 60 * 60)
                        .build()
        );
    }
}