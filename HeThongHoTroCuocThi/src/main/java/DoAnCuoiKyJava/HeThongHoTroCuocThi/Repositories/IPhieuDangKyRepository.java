package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface IPhieuDangKyRepository extends JpaRepository<PhieuDangKy, Long> {
    //Xuất các loại trường đang không bị ẩn
    List<PhieuDangKy> findByCuocThi_Id(Long cuocThiId);

    //Đếm phiếu đăng ký theo cuộc thi
    int countByCuocThiId(Long cuocThiId);

}
