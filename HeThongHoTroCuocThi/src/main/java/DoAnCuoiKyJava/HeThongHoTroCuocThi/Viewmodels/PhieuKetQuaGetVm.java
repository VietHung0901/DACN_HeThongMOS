package DoAnCuoiKyJava.HeThongHoTroCuocThi.Viewmodels;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.TruongService;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record PhieuKetQuaGetVm(Long id, String tenCuocThi, Long cuocThiId, String cccd, String hoten, String tenTruong, int diem, int phut, int giay) {
    public static PhieuKetQuaGetVm from(@NotNull PhieuKetQua phieuKetQua, @NotNull TruongService truongService) {
        Truong truong = truongService.findTruongById(phieuKetQua.getPhieuDangKy().getTruongId());
        return PhieuKetQuaGetVm.builder()
                .id(phieuKetQua.getId())
                .tenCuocThi(phieuKetQua.getPhieuDangKy().getCuocThi().getTenCuocThi())
                .cuocThiId(phieuKetQua.getPhieuDangKy().getCuocThi().getId())
                .cccd(phieuKetQua.getPhieuDangKy().getUser().getCccd())
                .hoten(phieuKetQua.getPhieuDangKy().getUser().getHoten())
                .tenTruong(truong.getTenTruong())
                .diem(phieuKetQua.getDiem())
                .phut(phieuKetQua.getPhut())
                .giay(phieuKetQua.getGiay())
                .build();
    }
}
