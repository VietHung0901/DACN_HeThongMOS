package DoAnCuoiKyJava.HeThongHoTroCuocThi.Request;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private Long id;
    private String hoten;
    private String cccd;
    private String email;
    private String phone;
    private MultipartFile imageUrl;
    private Truong truong;
    private LocalDate ngaySinh;
}
