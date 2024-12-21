package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ URL "/images/**" tới thư mục trên VPS
        registry.addResourceHandler("/images1/**")
                .addResourceLocations("file:/var/www/project/static/images/");

        // Ánh xạ URL "/uploads/exercise/**" tới thư mục lưu file
        registry.addResourceHandler("/uploads/exercise/**")
                .addResourceLocations("file:/var/www/project/uploads/exercise");

        // Ánh xạ URL "/uploads/**" tới thư mục lưu file
        registry.addResourceHandler("/uploads/Result/**")
                .addResourceLocations("file:/var/www/project/uploads/Result/");
    }
}
