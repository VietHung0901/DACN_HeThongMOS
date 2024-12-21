package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.INoiDungRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.NoiDungCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class NoiDungService {
    private final INoiDungRepository noiDungRepository;

    public List<NoiDung> getAllNoiDungs() {
        return noiDungRepository.findAll();
    }

    public List<NoiDung> getAllNoiDungsHien() {
        return noiDungRepository.findByTrangThai(1);
    }

    public Optional<NoiDung> getNoiDungById(Long id) {
        return noiDungRepository.findById(id);
    }

    public void addNoiDung(NoiDung noiDung) {
        noiDung.setTrangThai(1);
        noiDungRepository.save(noiDung);
    }

    public void updateNoiDung(NoiDung noiDung) {
        NoiDung existingNoiDung = noiDungRepository.findById(noiDung.getId()).orElse(null);
        if (existingNoiDung != null) {
            existingNoiDung.setTenNoiDung(noiDung.getTenNoiDung());
            existingNoiDung.setMoTaNoiDung(noiDung.getMoTaNoiDung());
            existingNoiDung.setImageUrl(noiDung.getImageUrl());
            existingNoiDung.setFileBaiTapUrl(noiDung.getFileBaiTapUrl());
            noiDungRepository.save(existingNoiDung);
        }
    }

    public void AnHien(Long id) {
        NoiDung noiDung = noiDungRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        noiDung.setTrangThai(noiDung.getTrangThai() == 0 ? 1 : 0);
        noiDungRepository.save(noiDung);
    }

    //hàm chuyển từ NoiDungCreateRequest sang NoiDung
    public NoiDung mapToNoiDung(NoiDungCreateRequest noiDungCreateRequest) {
        NoiDung noiDung = new NoiDung();
        noiDung.setId(noiDungCreateRequest.getId());
        noiDung.setTenNoiDung(noiDungCreateRequest.getTenNoiDung());
        noiDung.setMoTaNoiDung(noiDungCreateRequest.getMoTaNoiDung());
        if (!noiDungCreateRequest.getImageUrl().isEmpty()) {
            String image = saveImage(noiDungCreateRequest.getImageUrl());
            noiDung.setImageUrl(image);
        }
        if (!noiDungCreateRequest.getFileBaiTapUrl().isEmpty()) {
            String fileUrl = saveFile(noiDungCreateRequest.getFileBaiTapUrl());
            noiDung.setFileBaiTapUrl(fileUrl);
        }
        return noiDung;
    }

    public String saveImage(MultipartFile file) {
        // Lấy tên file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Đường dẫn lưu file trên Linux
        String uploadDir = "/var/www/project/static/images/";
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            // Kiểm tra xem thư mục có tồn tại không
            Files.createDirectories(Paths.get(uploadDir));

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }

        // Trả về đường dẫn của file đã lưu
        return "/images1/" + fileName;
    }

    public String saveFile(MultipartFile file) {
        // Đường dẫn lưu file trên VPS Linux
        String uploadDir = "/var/www/project/uploads/exercise/";
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file vào thư mục
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + fileName, e);
        }

        // Trả về đường dẫn URL để truy cập file
        return "/uploads/exercise/" + fileName;
    }
}

