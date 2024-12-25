package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ADMIN_SCHOOL/list")
@RequiredArgsConstructor
public class AdminSchoolExportController {

    private final PhieuDangKyService phieuDangKyService;
    private final CuocThiService cuocThiService;
    private final LoaiTruongService loaiTruongService;
    private final MonThiService monThiService;

    @GetMapping
    public String showAllCuocThi(@NotNull Model model) {
        model.addAttribute("cuocThis", cuocThiService.getAllCuocThis());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("phieuDangKyService", phieuDangKyService);
        model.addAttribute("monThis", monThiService.getAllMonThis());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
        return "ADMIN_SCHOOL/list";
    }
}

