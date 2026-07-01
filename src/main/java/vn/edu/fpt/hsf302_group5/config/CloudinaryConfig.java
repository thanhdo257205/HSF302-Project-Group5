package vn.edu.fpt.hsf302_group5.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        return new Cloudinary(config);
    }

    /**
     * Upload a PDF file or any other file to Cloudinary.
     * Maps the file byte array to Cloudinary and returns the secure URL.
     *
     * @param file the MultipartFile to upload
     * @return the secure URL of the uploaded file
     * @throws IOException if an I/O error occurs
     */
    public String uploadPdf(MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinary().uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );
        
        // Print the full response for troubleshooting if needed
        result.forEach((k, v) -> System.out.println(k + " : " + v));

        if (result.containsKey("secure_url")) {
            return result.get("secure_url").toString();
        }
        
        throw new IOException("Failed to get secure_url from Cloudinary response");
    }

    /**
     * Upload a generic file with custom upload options.
     *
     * @param file the MultipartFile to upload
     * @param options custom upload options Map
     * @return the secure URL of the uploaded file
     * @throws IOException if an I/O error occurs
     */
    public String uploadFile(MultipartFile file, Map<?, ?> options) throws IOException {
        Map<?, ?> result = cloudinary().uploader().upload(
                file.getBytes(),
                options == null ? ObjectUtils.emptyMap() : options
        );
        
        if (result.containsKey("secure_url")) {
            return result.get("secure_url").toString();
        }
        
        throw new IOException("Failed to get secure_url from Cloudinary response");
    }
}
