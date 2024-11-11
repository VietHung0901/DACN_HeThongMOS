package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IUserRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ADMIN_SCHOOL/import-students")
@RequiredArgsConstructor
public class AdminSchoolController {
    private final UserService userService;
    private final IUserRepository userRepository;

    @GetMapping
    public String showUploadForm() {
        return "ADMIN_SCHOOL/import-students";
    }

    @PostMapping
    public String importStudents(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            userService.importStudentsFromExcel(file);
            redirectAttributes.addFlashAttribute("message", "Thông báo sẽ gửi đến email của thí sinh trong thời gian sớm nhất. Xin cảm ơn!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi nhập: " + e.getMessage());
            return "redirect:/ADMIN_SCHOOL/import-students";
        }
        return "redirect:/ADMIN_SCHOOL/import-students";
    }
}



