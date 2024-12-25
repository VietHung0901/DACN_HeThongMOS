package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ADMIN_SCHOOL/PhieuKetQuas")
@RequiredArgsConstructor
public class AdminSchoolKetQuaController {

    private final CuocThiService cuocThiService;
    private final TruongService truongService;
    private final PhieuKetQuaService phieuKetQuaService;

    @GetMapping("/cuocThiId/{id}")
    public String showAllPhieuKetQuaByCuocThi(@PathVariable Long id, @NotNull Model model) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy cuộc thi với ID: " + id));

        List<PhieuKetQua> phieuKetQuas = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);

        List<Truong> truongs = truongService.getAllTruongs();
        model.addAttribute("truongs", truongs);
        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("phieuKetQuas", phieuKetQuas);
        model.addAttribute("truongService", truongService);
        return "ADMIN_SCHOOL/PhieuKetQua/list";
    }

}