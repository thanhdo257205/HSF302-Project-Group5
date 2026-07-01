package vn.edu.fpt.hsf302_group5.service.impl.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.hsf302_group5.service.cloudinary.CloudinaryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String publicId = null;
        if (originalFilename != null && originalFilename.contains(".")) {
            publicId = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        } else {
            publicId = originalFilename;
        }

        Map<String, Object> options = new HashMap<>();

        // Determine resource type: PDF and images should be uploaded as "image"
        String contentType = file.getContentType();
        if (contentType != null
                && (contentType.startsWith("image/") || contentType.equalsIgnoreCase("application/pdf"))) {
            options.put("resource_type", "image");
        } else if (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf")) {
            options.put("resource_type", "image");
        } else {
            options.put("resource_type", "auto");
        }

        options.put("use_filename", true);
        options.put("unique_filename", true);
        options.put("folder", "Project_HSF302"); // Uploads inside the Project_HSF302 folder

        if (publicId != null && !publicId.trim().isEmpty()) {
            options.put("public_id", publicId);
        }

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                options);

        if (result.containsKey("secure_url")) {
            return result.get("secure_url").toString();
        }

        throw new IOException("Failed to get secure_url from Cloudinary response");
    }
}
