package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;



import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.FileSample;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IFileSampleRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.FileSampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Admin/FileSample")
public class AdminUploadFileController {
    private final FileSampleService fileSampleService;
    private final IFileSampleRepository fileSampleRepository;

    private static final String BASE_URL = "http://localhost:8080/uploads/FileSample/";

    @GetMapping("/file")
    public String listFiles(Model model) {
        List<FileSample> files = fileSampleService.getAllFiles();
        model.addAttribute("files", files);
        return "/Admin/FileSample/file";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("files") MultipartFile file, Model model) {
        // Kiểm tra nếu không chọn file nào
        if (file.isEmpty()) {
            model.addAttribute("message", "Vui lòng chọn ít nhất 1 file để tải lên");
            return "/Admin/FileSample/file";  // Trả về trang upload nếu không có file
        }

        // Kiểm tra tên file đã tồn tại trong cơ sở dữ liệu
        String fileName = file.getOriginalFilename();
        if (fileSampleRepository.findByFileName(fileName) != null) {
            model.addAttribute("message", "Tên file đã tồn tại. Vui lòng chọn tên file khác.");
            return "/Admin/FileSample/file";  // Trả về trang upload nếu tên file trùng
        }

        try {
            // Gọi service để lưu file
            fileSampleService.saveFile(file);
            model.addAttribute("message", "File đã được tải lên thành công");
        } catch (IOException e) {
            model.addAttribute("message", "Đã có lỗi xảy ra: " + e.getMessage());
        }

        // Lấy danh sách các file đã tải lên
        List<FileSample> files = fileSampleService.getAllFiles();
        model.addAttribute("files", files);

        return "/Admin/FileSample/file";  // Trả về trang upload sau khi tải file thành công
    }

    // Phương thức tải file xuống
    @GetMapping("/uploads/FileSample/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Path filePath = Paths.get("E:\\DACN_HeThongMOS\\HeThongHoTroCuocThi\\src\\main\\resources\\static\\uploads\\FileSample\\" + fileName);
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new IOException("File không tồn tại hoặc không thể đọc được.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Phương thức xem trước file
    @GetMapping("/uploads/FileSample/preview/{fileName}")
    public ResponseEntity<Resource> previewFile(@PathVariable String fileName) {
        Path filePath = Paths.get("E:\\DACN_HeThongMOS\\HeThongHoTroCuocThi\\src\\main\\resources\\static\\uploads\\FileSample\\" + fileName);
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                throw new IOException("File không tồn tại hoặc không thể đọc được.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
