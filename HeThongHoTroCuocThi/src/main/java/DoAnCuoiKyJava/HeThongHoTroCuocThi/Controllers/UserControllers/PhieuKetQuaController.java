package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
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

    @GetMapping("/cuocThiId/{id}")
    public String showAllPhieuKetQuaByCuocThi(@PathVariable Long id, @NotNull Model model) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id).orElseThrow(() -> new EntityNotFoundException(""));
        List<PhieuKetQua> phieuKetQuas = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);
        model.addAttribute("phieuKetQuas", phieuKetQuas);
        return "/User/PhieuKetQua/list";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "/User/PhieuKetQua/search";
    }

    //API lấy thông tin phiếu kết quả
//    @GetMapping("/search/{pdkId}")
//    public ResponseEntity<PhieuKetQuaGetVm> getPhieuKetQuaByPdkIdAndUserId(@PathVariable Long pdkId) {
//        PhieuDangKy phieuDangKy = phieuDangKyService.getPhieuDangKyById(pdkId).orElseThrow(() -> new EntityNotFoundException(""));
//        return ResponseEntity.ok(phieuKetQuaService.findByPhieuDangKyAndTrangThai(phieuDangKy)
//                .map(PhieuKetQuaGetVm::from)
//                .orElse(null));
//    }

    @GetMapping("/search/pkq")
    public ResponseEntity<List<PhieuKetQuaGetVm>> searchPKQ(Principal principal)
    {
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(phieuKetQuaService.GetAllPKQByUserAndTrangThai(user)
                .stream()
                .map(PhieuKetQuaGetVm::from)
                .toList());
    }
}
