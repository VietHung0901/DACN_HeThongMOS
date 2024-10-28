function searchPKQ() {
    const cccd = document.getElementById('cccd').value;
    const pdkId = document.getElementById('pdkId').value;

    // Kiểm tra nếu cccd hoặc pdkId là trống
    if (!cccd || !pdkId) {
        alert('Vui lòng điền đủ thông tin.');
        return;
    }

    fetch('/User/PhieuKetQuas/search/' + pdkId)
        .then(response => response.json())
        .then(data => {
            let trHTML = '';
            // Kiểm tra nếu giá trị của cccd khác data.cccd
            if (cccd !== data.cccd) {
                // Xóa các dữ liệu đang có trong bảng
                document.getElementById('pkq-table-body').innerHTML = '';
                alert('Không tìm thấy thông tin!');
                return;
            }

            if (data) {
                trHTML +=
                    '<tr>' +
                    '<td>' + data.id + '</td>' +
                    '<td>' + data.tenCuocThi + '</td>' +
                    '<td>' + data.cccd + '</td>' +
                    '<td>' + data.hoten + '</td>' +
                    '<td>' + data.tenTruong + '</td>' +
                    '<td>' + data.diem + '</td>' +
                    '<td>' + data.phut + ':' + data.giay + '</td>' +
                    '<td>' +
                    '<a class="btn btn-primary btn-sm" href="/User/PhieuKetQuas/cuocThiId/' + data.cuocThiId + '">kết quả cuộc thi</a>' +
                    '</td>' +
                    '</tr>';
            }
            document.getElementById('pkq-table-body').innerHTML = trHTML;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Chưa có kết quả!');
        });
}