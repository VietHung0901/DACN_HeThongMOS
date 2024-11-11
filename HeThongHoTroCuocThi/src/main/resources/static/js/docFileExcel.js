document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('excelFileInput');
    const cancelButton = document.getElementById('cancelButton');
    const tableBody = document.querySelector("#previewTable tbody");

    fileInput.addEventListener('change', function (event) {
        const file = event.target.files[0];

        if (file && file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
            const reader = new FileReader();
            reader.onload = function (e) {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, { type: 'array' });
                const sheet = workbook.Sheets[workbook.SheetNames[0]];

                const rows = XLSX.utils.sheet_to_json(sheet, { header: 1, defval: "", raw: true });
                tableBody.innerHTML = "";  // Xóa nội dung bảng cũ

                // Bỏ qua dòng tiêu đề và duyệt qua các dòng dữ liệu
                rows.slice(1).forEach(row => {
                    const newRow = document.createElement("tr");

                    row.forEach((cellData, index) => {
                        const cellElement = document.createElement("td");

                        // Kiểm tra nếu là ngày sinh (giả sử ngày sinh ở cột thứ 6, index 5)
                        if (index === 5 && cellData) {
                            let date;
                            // Kiểm tra nếu dữ liệu là một đối tượng Date (trường hợp Excel lưu dưới dạng ngày thực tế)
                            if (typeof cellData === 'object' && cellData instanceof Date) {
                                date = cellData;
                            } else if (typeof cellData === 'number') {
                                // Nếu dữ liệu là số, Excel có thể đã lưu nó dưới dạng số ngày (Excel date number)
                                date = XLSX.SSF.parse_date_code(cellData);
                            } else {
                                // Nếu là chuỗi, thử chuyển đổi thành Date
                                date = new Date(cellData);
                            }

                            // Kiểm tra nếu đã có giá trị ngày hợp lệ
                            if (date instanceof Date && !isNaN(date)) {
                                cellElement.textContent = formatDate(date);  // Định dạng ngày theo dd/MM/yyyy
                            } else {
                                cellElement.textContent = "Invalid Date";  // Nếu ngày không hợp lệ, hiển thị lỗi
                            }
                        } else {
                            cellElement.textContent = cellData;  // Thêm dữ liệu vào ô
                        }

                        newRow.appendChild(cellElement);  // Thêm ô vào dòng
                    });

                    tableBody.appendChild(newRow);  // Thêm dòng mới vào bảng
                });
            };
            reader.readAsArrayBuffer(file);
        } else {
            alert("Vui lòng chọn tệp Excel (.xlsx)");
        }
    });

    // Xử lý khi nhấn nút "Hủy Tệp"
    cancelButton.addEventListener('click', function () {
        // Xóa tệp đã chọn
        fileInput.value = "";
        // Xóa bảng xem trước
        tableBody.innerHTML = "";
    });

    // Hàm định dạng ngày theo dd/MM/yyyy
    function formatDate(date) {
        const dd = date.getDate().toString().padStart(2, '0');
        const mm = (date.getMonth() + 1).toString().padStart(2, '0');
        const yyyy = date.getFullYear();
        return `${dd}/${mm}/${yyyy}`;
    }
});
