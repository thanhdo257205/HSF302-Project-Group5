package vn.edu.fpt.hsf302_group5.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(MultipartFile file) throws IOException;
}
