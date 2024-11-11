package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.TruongService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ADMIN_SCHOOL/export-students")
@RequiredArgsConstructor
public class AdminSchoolExportController {

    private final PhieuKetQuaService phieuKetQuaService;
    private final UserService userService;
    private final CuocThiService cuocThiService;

    @GetMapping
    public String showExportPage() {
        return "ADMIN_SCHOOL/export-students";
    }

    @GetMapping("/list/cuocThiId/{cuocThiId}")
    public List<PhieuKetQua> list(@PathVariable Long cuocThiId, Principal principal) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElseThrow(() -> new EntityNotFoundException("Cuộc thi không tồn tại"));
        User user = userService.findByUsername(principal.getName());
        return phieuKetQuaService.getAllPhieuKetQuaByCuocThiAndTruong(cuocThi, user.getTruong());
    }

    @GetMapping("/export/{cuocThiId}")
    public void exportStudents(@PathVariable Long cuocThiId, Principal principal, HttpServletResponse response) throws IOException {
        CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElseThrow(() -> new EntityNotFoundException("Cuộc thi không tồn tại"));
        User user = userService.findByUsername(principal.getName());

        List<PhieuKetQua> phieuKetQuaList = phieuKetQuaService.getAllPhieuKetQuaByCuocThiAndTruong(cuocThi, user.getTruong());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=danhsach_ketqua_thisinh.xls");
        exportToExcel(phieuKetQuaList, response.getOutputStream());
    }

    private void exportToExcel(List<PhieuKetQua> phieuKetQuaList, OutputStream outputStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Danh sách thí sinh");

        // Tạo tiêu đề cột
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã số thí sinh");
        headerRow.createCell(1).setCellValue("Tên thí sinh");
        headerRow.createCell(2).setCellValue("Điểm");
        headerRow.createCell(3).setCellValue("Thời gian làm bài");


        int rowNum = 1;
        for (PhieuKetQua pkq : phieuKetQuaList) {
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pkq.getPhieuDangKy().getUser().getId());
            row.createCell(1).setCellValue(pkq.getPhieuDangKy().getUser().getHoten());
            row.createCell(2).setCellValue(pkq.getDiem());
            row.createCell(3).setCellValue(pkq.getPhut() + " phút " + pkq.getGiay() + " giây");
        }

        workbook.write(outputStream);
        workbook.close();
    }
}

