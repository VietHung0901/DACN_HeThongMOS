package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuNopBaiService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Admin/PhieuNopBais")
@RequiredArgsConstructor
public class AdminPhieuNopBaiController {

    private final PhieuNopBaiService phieuNopBaiService;

    @GetMapping("/ChuaCham")
    public String showAllPhieuNopBaiChuaCham(@NotNull Model model) {
        model.addAttribute("phieuNopBai", phieuNopBaiService.findAllByTrangThai(0));
        return "/Admin/PhieuNopBai/list";
    }

    @GetMapping("/DaCham")
    public String showAllPhieuNopBaiDaCham(@NotNull Model model) {
        model.addAttribute("phieuNopBai", phieuNopBaiService.findAllByTrangThai(1));
        return "/Admin/PhieuNopBai/list";
    }

    @GetMapping("/edit/{id}")
    public String showNoiDung(@PathVariable Long id, @NotNull Model model) {
        PhieuNopBai phieuNopBai = phieuNopBaiService.findById(id).orElseThrow();
        model.addAttribute("phieuNopBai", phieuNopBai);
        return "/Admin/PhieuNopBai/edit";
    }

    @PostMapping("/edit")
    public String editPhieuNopBai(@NotNull Model model,
                                  @Valid @ModelAttribute("PhieuNopBai") PhieuNopBai phieuNopBai) {
        phieuNopBaiService.edit(phieuNopBai);
        return "redirect:/Admin/PhieuNopBais/ChuaCham";
    }

//
//    @GetMapping("/psd/trangthai/{trangthai}/{id}")
//    public String DuyetPhieuSuaDiem(@PathVariable Long id,
//                                    @PathVariable int trangthai) {
//        phieuSuaDiemService.xuLyKetQuaSuaDiem(id, trangthai);
//        if(trangthai == 1)
//            return "redirect:/Manager/PhieuSuaDiems/ThanhCong";
//        else
//            return "redirect:/Manager/PhieuSuaDiems/ThatBai";
//    }
}
