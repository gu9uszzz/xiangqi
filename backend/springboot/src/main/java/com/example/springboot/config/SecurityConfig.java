package com.example.springboot.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Spring Boot 2.x 通常不需要显式继承 WebSecurityConfigurerAdapter，使用 SecurityFilterChain Bean 即可
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig { // 在 Spring Boot 2.x 中，通常不再需要继承 WebSecurityConfigurerAdapter，推荐使用 SecurityFilterChain bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF (对于无状态 JWT API 是标准做法)
                .csrf().disable()
                // 2. 配置授权规则
                .authorizeRequests()
                // 使用 antMatchers 指定路径模式
                .antMatchers(
                        "/user/login",       // 确认路径与 Controller 和 WebConfig 中的一致
                        "/user/register",    // 确认路径与 Controller 和 WebConfig 中的一致
                        "/user/sendcode",    // **确认这个路径被允许**
                        "/files/**",         // 文件访问
                        // Swagger 相关路径 (根据你实际使用的 Swagger 版本和路径调整)
                        "/swagger-ui/**",    // Swagger UI v3+
                        "/v3/api-docs/**",   // OpenAPI v3 spec
                        "/swagger-resources/**", // Swagger 资源
                        "/webjars/**",       // Webjar 资源 (Swagger UI 可能依赖)
                        "/v2/api-docs",      // Swagger v2 spec (如果使用)
                        "/swagger-ui.html",  // Swagger UI v2 入口
                        "/error"             // Spring Boot 默认错误页
                        // 如果需要，可以放行所有 OPTIONS 预检请求
                        // ,HttpMethod.OPTIONS, "/**"
                ).permitAll() // 允许上面 antMatchers 匹配到的所有请求
                // 其他所有请求都需要身份验证
                .anyRequest().authenticated()
                // 3. 使用 .and() 连接下一个配置 (这是 5.x 的常见风格)
                .and()
                // 4. 配置 Session 管理策略为 STATELESS (无状态)，因为我们用 JWT
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 5. 返回构建好的 HttpSecurity 对象
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}