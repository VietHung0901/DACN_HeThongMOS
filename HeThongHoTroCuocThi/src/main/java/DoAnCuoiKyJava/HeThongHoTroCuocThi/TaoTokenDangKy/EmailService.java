package DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, User user, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Vui lòng nhấp vào liên kết dưới đây để xác nhận email của bạn:\n\n"
                + confirmationUrl + "\n\n"
                + "Thông tin của bạn:\n"
                + "Email: " + user.getEmail() + "\n"
                + "SDT: " + user.getPhone() + "\n"
                + "Trường: " + user.getTruong().getTenTruong() + "\n"
                + "Ngày sinh: " + user.getNgaySinh() + "\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    public void sendEmailFail(String to, String subject, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Rất tiếc thông tin cá nhân bạn đăng ký không khớp với thông tin cá nhân của bạn hoặc không hợp lê!!\n"
                + "Bạn có thể thử đăng ký lại với thông tin hợp lệ: \n"
                + "http://localhost:8080/register" + "\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    public void sendEmailSuccess(String to, String subject, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Tài khoản của bạn đã được chúng tôi xác nhận hợp lệ\n"
                + "Bạn có thể đăng nhập tài đây: \n"
                + "http://localhost:8080/login" + "\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }
}

