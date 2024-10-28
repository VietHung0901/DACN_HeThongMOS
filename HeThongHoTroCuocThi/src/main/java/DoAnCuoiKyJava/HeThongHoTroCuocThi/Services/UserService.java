package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Constant.Provider;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Constant.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IRoleRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IUserRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
//Các thư viện dùng lưu ảnh vào local
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void Save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder()
                .encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByCCCD(String id) {
        return userRepository.findByCccd(id);
    }

    public void saveOauthUser(String email, @NotNull String username) {
        if(userRepository.findByUsername(username) != null)
            return;
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username){
        userRepository.findByUsername(username)
                .getRoles()
                .add(roleRepository.findRoleById(Role.USER.value));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public String saveImage(MultipartFile file) {
        // Lấy tên file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Đường dẫn lưu file
        //        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "/Users/tranviethung/Documents/Học tập/HeThongHoTroCuocThiJaVa/HeThongHoTroCuocThi/src/main/resources/static/images/";
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            // Kiểm tra xem thư mục có tồn tại không
            Files.createDirectories(Paths.get(uploadDir));

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }

        // Trả về đường dẫn của file đã lưu
        return "/images/" + fileName;
    }

    public void saveUser(UserUpdateRequest updatedUser) {
        User user = findById(updatedUser.getId());
        if (user != null) {
            user.setCccd(updatedUser.getCccd());
            user.setHoten(updatedUser.getHoten());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setNgaySinh(updatedUser.getNgaySinh());

            String fileName = updatedUser.getImageUrl().getOriginalFilename();
            if(fileName != "") {
                String images = saveImage(updatedUser.getImageUrl());
                user.setImageUrl(images);
            }
            user.setTruong(updatedUser.getTruong());
            userRepository.save(user);
        }
    }

    public int getTotalUsers() {
        return userRepository.findAll().size(); // Đếm số lượng người dùng
    }

    // Example method to get user counts by LoaiTruong
    public List<Object[]> getUserCountsByLoaiTruong() {
        return userRepository.getUserCountsByLoaiTruong();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}