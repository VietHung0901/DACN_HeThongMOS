package DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class VerificationController {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "/Account/XacNhanEmail/fail";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true); // Kích hoạt tài khoản
        userRepository.save(user);

        return "/Account/XacNhanEmail/success";
    }
}

