package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuDangKyRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.PhieuDangKyCreate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuDangKyService {
    private final IPhieuDangKyRepository phieuDangKyRepository;
    private final UserService userService;

    public List<PhieuDangKy> getAllPhieuDangKys() {
        return phieuDangKyRepository.findAll();
    }

    public List<PhieuDangKy> getAllPhieuDangKystheoCuocThi(Long cuocThiId) {
        return phieuDangKyRepository.findByCuocThi_Id(cuocThiId);
    }

    public Optional<PhieuDangKy> getPhieuDangKyById(Long id) {
        return phieuDangKyRepository.findById(id);
    }

    public PhieuDangKy addPhieuDangKy(PhieuDangKy phieuDangKy) {
        phieuDangKy.setTrangThai(1);
        return phieuDangKyRepository.save(phieuDangKy);
    }

    public PhieuDangKy updatePhieuDangKy(PhieuDangKy updatedPhieuDangKy) {
        PhieuDangKy phieuDangKy = getPhieuDangKyById(updatedPhieuDangKy.getId())
                .orElseThrow(() -> new EntityNotFoundException("Truong not found with id"));
        phieuDangKy.setEmail(updatedPhieuDangKy.getEmail());
        phieuDangKy.setSdt(updatedPhieuDangKy.getSdt());
        phieuDangKy.setTruongId(updatedPhieuDangKy.getTruongId());
        return phieuDangKyRepository.save(phieuDangKy);
    }

    //Đếm phiếu đăng ký theo cuộc thi
    public int countPhieuDangKyByCuocThiId(Long cuocThiId) {
        return phieuDangKyRepository.countByCuocThiId(cuocThiId);
    }

    public PhieuDangKy mapToPhieuDangKy (PhieuDangKyCreate phieuDangKyCreate)
    {
        PhieuDangKy phieuDangKy = new PhieuDangKy();
        phieuDangKy.setCuocThi(phieuDangKyCreate.getCuocThi());
        phieuDangKy.setSdt(phieuDangKyCreate.getSdt());
        phieuDangKy.setEmail(phieuDangKyCreate.getEmail());
        phieuDangKy.setTruongId(phieuDangKyCreate.getTruongId());
        phieuDangKy.setNgayDangKy(LocalDateTime.now());

        User user = userService.findById(phieuDangKyCreate.getUserId());
        phieuDangKy.setUser(user);

        return phieuDangKy;
    }

    public boolean tonTaiPhieuDangKyUserId_CuocThiId (Long userId, Long cuocThiId)
    {
        List<PhieuDangKy> listPDK = getAllPhieuDangKys();
        for (PhieuDangKy pdk : listPDK) {
            if(pdk.getUser().getId() == userId && pdk.getCuocThi().getId() == cuocThiId)
                return true;
        }
        return false;
    }

    public List<PhieuDangKy> getPdkByUser (User user)
    {
        List<PhieuDangKy> listPDK = new ArrayList<>();
        for (PhieuDangKy pdk : getAllPhieuDangKys()) {
            if(pdk.getUser() == user)
            {
                listPDK.add(pdk);
            }
        }
        return listPDK;
    }

    public int getTotalPDKs() {
        List<PhieuDangKy> allPhieuDangKys = phieuDangKyRepository.findAll();
        return allPhieuDangKys.size();
    }


}