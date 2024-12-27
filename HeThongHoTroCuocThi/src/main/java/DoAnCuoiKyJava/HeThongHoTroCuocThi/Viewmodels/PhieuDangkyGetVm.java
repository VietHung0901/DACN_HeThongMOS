package DoAnCuoiKyJava.HeThongHoTroCuocThi.Viewmodels;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.TruongService;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record PhieuDangkyGetVm(Long pdkId, String tenCuocThi, String cccd, String hoten, String ngaySinh, String Email, String sdt, String tenTruong, int diem) {
    public static PhieuDangkyGetVm from(@NotNull PhieuDangKy phieuDangKy, @NotNull TruongService truongService, @NotNull PhieuKetQuaService phieuKetQuaService) {
        Truong truong = truongService.findTruongById(phieuDangKy.getTruongId());
        PhieuKetQua pkq = phieuKetQuaService.getPhieuKetQuaByPhieuDangKy(phieuDangKy);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Định dạng ngày tháng
        String ngaySinhString = phieuDangKy.getUser().getNgaySinh().format(formatter);
        return PhieuDangkyGetVm.builder()
                .pdkId(phieuDangKy.getId())
                .tenCuocThi(phieuDangKy.getCuocThi().getTenCuocThi())
                .cccd(phieuDangKy.getUser().getCccd())
                .hoten(phieuDangKy.getUser().getHoten())
                .ngaySinh(ngaySinhString)
                .Email(phieuDangKy.getUser().getEmail())
                .sdt(phieuDangKy.getUser().getPhone())
                .tenTruong(truong.getTenTruong())
                .diem(pkq.getDiem())
                .build();
    }
}
