
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 对所有接口生效
                        // .allowedOrigins("http://localhost:5173") // 精确允许的前端地址 (生产环境推荐)
                        .allowedOriginPatterns("*") // 允许所有来源 (开发时常用，生产环境慎用!)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                        .allowCredentials(true) // 允许发送 Cookie 等凭证信息
                        .allowedHeaders("*") // 允许所有请求头
                        .maxAge(3600); // 预检请求的有效期，单位秒
            }
        };
    }
}