function searchPKQ() {
    const cccd = document.getElementById('cccd').value;

    // Kiểm tra nếu cccd hoặc pdkId là trống
    if (!cccd) {
        alert('Vui lòng điền đủ thông tin.');
        return;
    }

    fetch('/User/PhieuKetQuas/search/pkq/' + cccd)
        .then(response => response.json())
        .then(data => {
            let trHTML = '';

            // Kiểm tra nếu danh sách data không rỗng
            if (!data || data.length === 0) {
                // Xóa các dữ liệu đang có trong bảng
                document.getElementById('pkq-table-body').innerHTML = '';
                alert('Không tìm thấy thông tin!');
                return;
            }

            // Duyệt qua tất cả các kết quả trong danh sách data
            data.forEach(item => {

                trHTML +=
                    '<tr>' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.tenCuocThi + '</td>' +
                    '<td>' + item.cccd + '</td>' +
                    '<td>' + item.hoten + '</td>' +
                    '<td>' + item.tenTruong + '</td>' +
                    '<td>' + item.diem + '</td>' +
                    '<td>' + item.phut + ':' + item.giay + '</td>' +
                    '<td>' +
                    '<a class="btn btn-primary btn-sm" href="/User/PhieuKetQuas/cuocThiId/' + item.cuocThiId + '">kết quả cuộc thi</a>' +
                    '</td>' +
                    '</tr>';
            });

            // Đưa các row vào bảng
            document.getElementById('pkq-table-body').innerHTML = trHTML;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Không tìm thấy kết quả!');
        });

}