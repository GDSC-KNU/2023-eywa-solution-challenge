package kr.ac.knu.gdsc.Eywa.utils;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import kr.ac.knu.gdsc.Eywa.GcpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class CloudStorageService {
    private final GcpConfiguration gcpConfiguration;
    private Storage storage;

    @Autowired
    public CloudStorageService(GcpConfiguration gcpConfiguration) {
        this.gcpConfiguration = gcpConfiguration;
        try {
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(gcpConfiguration.getKeyPath())))
                    .build()
                    .getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImageToCloudStorage(String imagePath, String imageName) {
        try {
            storage.create(
                    BlobInfo.newBuilder(gcpConfiguration.getBucketName(), imageName).build(),
                    new FileInputStream(imagePath)
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String dirName, MultipartFile image) {
        try {
            String originalImageName = Objects.requireNonNull(image.getOriginalFilename());
            String extension = originalImageName.substring(originalImageName.lastIndexOf("."));
            String imageName = System.nanoTime() + extension;
            String imagePath = Path.of(System.getProperty("user.dir"), "images", dirName, imageName).toString();
            image.transferTo(new File(imagePath));
            uploadImageToCloudStorage(imagePath, imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}