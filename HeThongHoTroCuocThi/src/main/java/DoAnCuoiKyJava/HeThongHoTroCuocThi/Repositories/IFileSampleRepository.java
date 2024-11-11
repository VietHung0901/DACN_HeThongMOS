package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.FileSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileSampleRepository extends JpaRepository<FileSample, Long> {
    // Tìm file theo tên
    FileSample findByFileName(String fileName);
}

