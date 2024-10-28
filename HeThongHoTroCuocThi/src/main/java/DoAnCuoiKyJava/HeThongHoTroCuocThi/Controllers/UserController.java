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
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("listTruong", truongService.getAllTruongsHien());
            return "/Account/register";
        }

        String image = userService.saveImage(userRequest.getImageUrl());
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setHoten(userRequest.getHoten());
        user.setCccd(userRequest.getCccd());
        user.setPassword(userRequest.getPassword());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        user.setNgaySinh(userRequest.getNgaySinh());
        user.setImageUrl(image);
        user.setTruong(userRequest.getTruong());
        user.setTrangThai(0);
        userService.Save(user);
        userService.setDefaultRole(user.getUsername());
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

    @GetMapping("/list")
    public String showAllUser(@NotNull Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/Account/list";
    }
}