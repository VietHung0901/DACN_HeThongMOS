package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuKetQuaRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ITruongRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.PhieuKetQuaRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PhieuKetQuaService {
    private final IPhieuKetQuaRepository phieuKetQuaRepository;
    private final ITruongRepository truongRepository;

    //lấy các phiếu kết quả có trạng thái là 1 (Hiện)
    public List<PhieuKetQua> getAllPhieuKetQua() {
        return phieuKetQuaRepository.findByTrangThai(1);
    }

    public List<PhieuKetQua> getAllPhieuKetQuaByCuocThiAndTruong(CuocThi cuocThi, Truong truong) {
        List<PhieuKetQua> list = new ArrayList<>();
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getCuocThi().equals(cuocThi) && pkq.getPhieuDangKy().getTruongId() == truong.getId()) {
                list.add(pkq);
            }
        }
        return list;
    }

    public List<PhieuKetQua> getAllPhieuKetQuastheoCuocThi(CuocThi cuocThi) {
        List<PhieuKetQua> listPKQ = new ArrayList<>();
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getCuocThi() == cuocThi) {
                listPKQ.add(pkq);
            }
        }

        // Sắp xếp listPKQ giảm dần theo điểm, tăng dần theo phút, tăng dần theo giây
        Collections.sort(listPKQ, new Comparator<PhieuKetQua>() {
            @Override
            public int compare(PhieuKetQua pkq1, PhieuKetQua pkq2) {
                int diemCompare = Integer.compare(pkq2.getDiem(), pkq1.getDiem());
                if (diemCompare != 0) {
                    return diemCompare;
                }
                int phutCompare = Integer.compare(pkq1.getPhut(), pkq2.getPhut());
                if (phutCompare != 0) {
                    return phutCompare;
                }
                return Integer.compare(pkq1.getGiay(), pkq2.getGiay());
            }
        });

        return listPKQ;
    }


    public Optional<PhieuKetQua> getPhieuKetQuaById(Long id) {
        return phieuKetQuaRepository.findById(id);
    }

    public PhieuKetQua addPhieuKetQua(PhieuKetQua phieuKetQua) {
        phieuKetQua.setTrangThai(1);
        return phieuKetQuaRepository.save(phieuKetQua);
    }

    //khi sửa phiếu kết quả sẽ có trạng thái là o
    public PhieuKetQua editPhieuKetQua(PhieuKetQua phieuKetQua) {
        phieuKetQua.setTrangThai(0);
        return phieuKetQuaRepository.save(phieuKetQua);
    }

    public String getDiemByCuocThiIdvaUserId(Long cuocThiId, Long userId) {
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getCuocThi().getId() == cuocThiId && pkq.getPhieuDangKy().getUser().getId() == userId) {
                return String.valueOf(pkq.getDiem());
            }
        }
        return null;
    }

    public PhieuKetQua getPhieuKetQuaByPhieuDangKy(PhieuDangKy phieuDangKy) {
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy() == phieuDangKy) {
                return pkq;
            }
        }
        return null;
    }

    public PhieuKetQua updatePhieuKetQua(PhieuKetQua updatephieuKetQua)
    {
        PhieuKetQua pkq = getPhieuKetQuaById(updatephieuKetQua.getId()).orElseThrow(() -> new EntityNotFoundException(""));
        pkq.setDiem(updatephieuKetQua.getDiem());
        pkq.setPhut(updatephieuKetQua.getPhut());
        pkq.setGiay(updatephieuKetQua.getGiay());
        return phieuKetQuaRepository.save(pkq);
    }

    public PhieuKetQuaRequest mapToPhieuKetQuaRequest(PhieuKetQua phieuKetQua)
    {
        PhieuKetQuaRequest phieuKetQuaRequest = new PhieuKetQuaRequest();
        phieuKetQuaRequest.setId(phieuKetQua.getId());
        phieuKetQuaRequest.setPhut(phieuKetQua.getPhut());
        phieuKetQuaRequest.setGiay(phieuKetQua.getGiay());
        phieuKetQuaRequest.setDiem(phieuKetQua.getDiem());
        phieuKetQuaRequest.setPhieuDangKy(phieuKetQua.getPhieuDangKy());
        return phieuKetQuaRequest;
    }

    public List<PhieuKetQua> getPkqByUser (User user)
    {
        List<PhieuKetQua> listPKQ = new ArrayList<>();
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if(pkq.getPhieuDangKy().getUser() == user)
            {
                listPKQ.add(pkq);
            }
        }
        return listPKQ;
    }

    public Optional<PhieuKetQua>  findByPhieuDangKyAndTrangThai (PhieuDangKy phieuDangKy)
    {
        return phieuKetQuaRepository.findByPhieuDangKyAndTrangThai(phieuDangKy, 1);
    }
}
