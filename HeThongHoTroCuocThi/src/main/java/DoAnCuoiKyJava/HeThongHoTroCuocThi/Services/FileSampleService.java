package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.FileSample;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IFileSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FileSampleService {

    @Autowired
    private IFileSampleRepository fileSampleRepository;

    private static final String UPLOAD_DIR = "E:\\DACN_HeThongMOS\\HeThongHoTroCuocThi\\src\\main\\resources\\static\\uploads\\FileSample\\";

    // Định dạng ngày giờ theo mẫu yyyy-MM-dd hh:mm a
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    // Phương thức để định dạng ngày giờ
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    // Lưu file vào thư mục và cơ sở dữ liệu
    public void saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        // Lưu file vào thư mục
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());

        // Lưu thông tin file vào cơ sở dữ liệu
        FileSample fileSample = new FileSample();
        fileSample.setFileName(fileName);
        fileSample.setFilePath(path.toString());
        fileSample.setUploadedAt(LocalDateTime.now());  // Lưu ngày giờ tải lên

        // Định dạng ngày giờ trước khi lưu vào cơ sở dữ liệu
        String formattedDateTime = formatDateTime(fileSample.getUploadedAt());
        fileSample.setFormattedUploadTime(formattedDateTime);

        fileSampleRepository.save(fileSample);
    }

    // Lấy tất cả file từ cơ sở dữ liệu
    public List<FileSample> getAllFiles() {
        return fileSampleRepository.findAll();
    }
}

