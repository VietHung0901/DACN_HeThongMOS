package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuKetQuaRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final IPhieuKetQuaRepository phieuKetQuaRepository;
    private final PhieuKetQuaService phieuKetQuaService;
    private final CuocThiService cuocThiService;
    private final PhieuDangKyService phieuDangKyService;

    @GetMapping("/export/diem/cuocThi/{id}")
    public ResponseEntity<byte[]> exportToExcelDiem(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("SĐT");
        headerRow.createCell(6).setCellValue("Trường");
        headerRow.createCell(7).setCellValue("Phút");
        headerRow.createCell(8).setCellValue("Giây");
        headerRow.createCell(9).setCellValue("Điểm");

        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));;

        List<PhieuKetQua> dataList = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);

        int rowNum = 1;
        for (PhieuKetQua data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getPhieuDangKy().getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(data.getPhieuDangKy().getUser().getCccd());
            row.createCell(3).setCellValue(data.getPhieuDangKy().getUser().getHoten());
            row.createCell(4).setCellValue(data.getPhieuDangKy().getEmail());
            row.createCell(5).setCellValue(data.getPhieuDangKy().getSdt());
            row.createCell(6).setCellValue(data.getPhieuDangKy().getTruongId());
            row.createCell(7).setCellValue(data.getPhut());
            row.createCell(8).setCellValue(data.getGiay());
            row.createCell(9).setCellValue(data.getDiem());

        }

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/pdk/cuocThi/{id}")
    public ResponseEntity<byte[]> exportToExcelPDK(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("SĐT");
        headerRow.createCell(6).setCellValue("Trường");
        headerRow.createCell(7).setCellValue("Phút");
        headerRow.createCell(8).setCellValue("Giây");
        headerRow.createCell(9).setCellValue("Điểm");

        // Thêm dữ liệu (ở đây giả sử bạn có một danh sách dữ liệu)
        List<PhieuDangKy> dataList = phieuDangKyService.getAllPhieuDangKystheoCuocThi(id); // Bạn hãy thay đổi phương thức này theo cách bạn lấy dữ liệu

        int rowNum = 1;
        for (PhieuDangKy data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(data.getUser().getCccd());
            row.createCell(3).setCellValue(data.getUser().getHoten());
            row.createCell(4).setCellValue(data.getEmail());
            row.createCell(5).setCellValue(data.getSdt());
            row.createCell(6).setCellValue(data.getTruongId());
        }

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/import/form")
    public String showImportForm() {
        return "Excel/import";
    }
    // Phương thức xử lý POST để import Excel
    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "File is empty");
            model.addAttribute("messageType", "danger");
            return "Excel/import"; // Trở về trang upload với thông báo lỗi
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<PhieuKetQua> phieuKetQuaList = new ArrayList<>();

            for (Row row : sheet) {
                PhieuKetQua phieuKetQua = new PhieuKetQua();
                Long id = (long) row.getCell(0).getNumericCellValue();
                PhieuDangKy phieuDangKy = phieuDangKyService.getPhieuDangKyById(id)
                        .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));;
                phieuKetQua.setPhieuDangKy(phieuDangKy);
                phieuKetQua.setPhut((int) row.getCell(7).getNumericCellValue());
                phieuKetQua.setGiay((int) row.getCell(8).getNumericCellValue());
                phieuKetQua.setDiem((int) row.getCell(9).getNumericCellValue());

                phieuKetQuaList.add(phieuKetQua);
            }

            phieuKetQuaRepository.saveAll(phieuKetQuaList);
            model.addAttribute("message", "Import successful");
            model.addAttribute("messageType", "success");
        } catch (IOException e) {
            model.addAttribute("message", "Error reading file");
            model.addAttribute("messageType", "danger");
        }

        return "Excel/import"; // Trở về trang upload với thông báo
    }
}