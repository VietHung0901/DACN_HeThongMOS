package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.ChiTietNoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.ChiTietQuyDinh;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChiTietQuyDinhRepository extends JpaRepository<ChiTietQuyDinh, Long>{
    List<QuyDinh> findAllQuyDinhByCuocThi(CuocThi cuocThi);
    List<ChiTietQuyDinh> findByCuocThiId(Long cuocThiId);
}
