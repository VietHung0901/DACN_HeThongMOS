package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Viewmodels.PhieuKetQuaGetVm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/User/PhieuKetQuas")
@RequiredArgsConstructor
public class PhieuKetQuaController {
    private final PhieuDangKyService phieuDangKyService;
    private final PhieuKetQuaService phieuKetQuaService;
    private final CuocThiService cuocThiService;
    private final UserService userService;
    private final TruongService truongService;

    @GetMapping("/cuocThiId/{id}")
    public String showAllPhieuKetQuaByCuocThi(@PathVariable Long id, @NotNull Model model) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id).orElseThrow(() -> new EntityNotFoundException(""));
        List<PhieuKetQua> phieuKetQuas = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);
        model.addAttribute("phieuKetQuas", phieuKetQuas);
        return "User/PhieuKetQua/list";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "User/PhieuKetQua/search";
    }

    @GetMapping("/search/pkq/{cccd}")
    public ResponseEntity<List<PhieuKetQuaGetVm>> searchPKQ(@PathVariable String cccd) {
        // Tìm User bằng CCCD
        User user = userService.getUserByCCCD(cccd)
                .orElseThrow(() -> new EntityNotFoundException("Not Found user with cccd: " + cccd));

        // Lấy danh sách phiếu kết quả
        List<PhieuKetQuaGetVm> phieuKetQuaGetVms = phieuKetQuaService.GetAllPKQByUserAndTrangThai(user)
                .stream()
                .map(phieuKetQua -> PhieuKetQuaGetVm.from(phieuKetQua, truongService)) // Truyền truongService
                .toList();

        // Trả về ResponseEntity
        return ResponseEntity.ok(phieuKetQuaGetVms);
    }


//    public ResponseEntity<List<PhieuKetQuaGetVm>> searchPKQ(@PathVariable String cccd)
//    {
//        User user = userService.getUserByCCCD(cccd).orElseThrow(() -> new EntityNotFoundException("Not Found user with cccd: " + cccd));
//        return ResponseEntity.ok(phieuKetQuaService.GetAllPKQByUserAndTrangThai(user)
//                .stream()
//                .map(PhieuKetQuaGetVm::from)
//                .toList());
//    }

}
