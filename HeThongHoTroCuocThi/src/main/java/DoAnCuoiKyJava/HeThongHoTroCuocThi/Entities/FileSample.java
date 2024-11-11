package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "FileSample")
public class FileSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;
    private String formattedUploadTime;

}
