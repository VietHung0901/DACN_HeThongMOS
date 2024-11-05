package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.UserCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.UserUpdateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.TruongService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Viewmodels.UserGetVM;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TruongService truongService;

    @GetMapping("/login")
    public String login() {
        return "/Account/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new UserCreateRequest());
        model.addAttribute("listTruong", truongService.getAllTruongsHien());
        return "/Account/register";
    }

    @PostMapping("/register")
    public String register(UserCreateRequest userRequest,
                           @NotNull BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("listTruong", truongService.getAllTruongsHien());
            return "/Account/register";
        }

        String ketQua = userService.checkUser(userRequest);
        if (!"success".equals(ketQua)) {
            model.addAttribute("error", ketQua);
            model.addAttribute("user", userRequest);
            model.addAttribute("listTruong", truongService.getAllTruongsHien());
            return "/Account/register";
        }
        userService.createNewUser(userRequest);

        redirectAttributes.addFlashAttribute("successMessage", "Tài khoản của bạn sẽ được duyệt sau 48h. Vui lòng đợi!");
        return "redirect:/login";
    }

    //API lấy thông tin user
    @GetMapping("/User/id/{id}")
    public ResponseEntity<UserGetVM> getUserByCCCD(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserByCCCD(id)
                .map(UserGetVM::from)
                .orElse(null));
    }

    @GetMapping("/User/edit")
    public String editUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("listTruong", truongService.getAllTruongsHien());
        return "/Account/edit";
    }

    @PostMapping("/edit")
    public String saveEditedUser(@Valid @ModelAttribute("user") UserUpdateRequest updateUser,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("error", "Validation errors occurred");
            return "redirect:/User/edit";
        }

        userService.saveUser(updateUser);
        redirectAttributes.addFlashAttribute("successMessage", "Thông tin người dùng đã được cập nhật thành công.");
        return "redirect:/User/edit";
    }

    @GetMapping("/list/ThanhCong")
    public String showAllUser(@NotNull Model model) {
        model.addAttribute("users", userService.getAllUsersByTrangThai(1));
        return "/Account/list";
    }

    @GetMapping("/list/ChuaDuyet")
    public String showAllUserChuaDuyet(@NotNull Model model) {
        model.addAttribute("users", userService.getAllUsersByTrangThai(0));
        return "/Account/list";
    }

    @GetMapping("/list/ThatBai")
    public String showAllUserThatBai(@NotNull Model model) {
        model.addAttribute("users", userService.getAllUsersByTrangThai(2));
        return "/Account/list";
    }

    @GetMapping("/id/{id}")
    public String showUser(@PathVariable Long id, @NotNull Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/Account/detail";
    }

    @GetMapping("/KhongDuyet/{id}")
    public String KhongDuyet(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        userService.KhongDuyet(id);
        redirectAttributes.addFlashAttribute("failMessage", "Tài khoản không được duyệt.");
        return "redirect:/id/{id}";
    }

    @GetMapping("/Duyet/{id}")
    public String Duyet(@PathVariable Long id,
                        RedirectAttributes redirectAttributes) {
        userService.Duyet(id);
        redirectAttributes.addFlashAttribute("successMessage", "Tài khoản đã được duyệt.");
        return "redirect:/id/{id}";
    }
}