package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPhieuKetQuaRepository extends JpaRepository<PhieuKetQua, Long> {
    Optional<PhieuKetQua> findByPhieuDangKyAndTrangThai(PhieuDangKy phieuDangKy, int trangThai);
    List<PhieuKetQua> findByTrangThai(int trangThai);


}