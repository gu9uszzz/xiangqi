package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import com.example.springboot.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private VerificationCodeService verificationCodeService; // Inject service
    @Autowired
    private TokenUtils tokenUtils; // Inject utils
    @Autowired
    private JavaMailSender mailSender; // Inject mail sender
    // Email validation pattern (basic)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    /**
     * Send Verification Code Endpoint
     */
    @PostMapping("/sendcode")
    public Result sendCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (StrUtil.isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            return Result.error("400", "无效的邮箱地址");
        }
        try {
            // 1. Generate and store code in DB via service
            String code = verificationCodeService.createAndStoreCode(email);
            // 2. Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("3351376712@qq.com"); // Set your sender email address here
            message.setTo(email);
            message.setSubject("【在线象棋】邮箱验证码");
            message.setText("您的验证码是：" + code + "，有效期为5分钟，请勿泄露给他人。");
            mailSender.send(message);
            return Result.success("验证码已发送至 " + email + "，请注意查收。");
        } catch (MailException e) {
            log.error("发送邮件失败: {}", email, e);
            return Result.error("500", "验证码发送失败，请检查邮箱地址或稍后重试");
        } catch (Exception e) {
            log.error("处理发送验证码请求失败: {}", email, e);
            return Result.error("500", "请求处理失败");
        }
    }

    /**
     * Register Endpoint
     * User object should now contain: username, email, password, code
     */
    // Remove @AuthAccess if it was for bypassing old auth, JWT interceptor handles it now
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // Basic validation (non-blank)
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getEmail()) ||
                StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getCode())) {
            return Result.error("400", "所有字段均不能为空");
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return Result.error("400", "无效的邮箱地址");
        }
        // Password length check can be here or in service
//         if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
//             return Result.error(Constants.CODE_400, "密码长度需在6-20位之间");
//         }
        try {
            // Call the updated register service method
            User registeredUser = userService.register(user);
            // Optionally: Generate JWT immediately upon successful registration
            String token = tokenUtils.generateToken(registeredUser);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("user", registeredUser); // User object (password should be null)
            resultData.put("token", token);
            return Result.success(resultData); // Return user and token
        } catch (ServiceException e) {
            // Catch specific service exceptions (like user exists, code invalid)
            return Result.error(e.getCode() != null ? e.getCode() : "400", e.getMessage());
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error("400", "注册过程中发生错误");
        }
    }


    /**
     * Login Endpoint
     */
    // Remove @AuthAccess if present
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error("400", "用户名和密码不能为空");
        }
        try {
            // Call the login service method
            User loggedInUser = userService.login(username, password);
            // Generate JWT
            String token = tokenUtils.generateToken(loggedInUser);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("user", loggedInUser); // User object (password should be null)
            resultData.put("token", token);
            return Result.success(resultData);
        } catch (ServiceException e) {
            return Result.error(e.getCode() != null ? e.getCode() : "401", e.getMessage()); // e.g., "用户名或密码错误"
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error("500", "登录过程中发生错误");
        }
    }

    /**
     * Example Protected Endpoint: Get Current User Profile
     */
    @GetMapping("/profile")
    public Result getUserProfile(HttpServletRequest request) {
        // Retrieve user info stored by JwtInterceptor
        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser != null) {
            // Password should already be null, but double-check if needed
            // currentUser.setPassword(null);
            return Result.success(currentUser);
        } else {
            // This case should ideally not happen if interceptor works correctly
            return Result.error("401", "无法获取用户信息，请重新登录");
        }
    }
    // Add other endpoints for your game, they will be protected by the JWT interceptor
    // Example:
    // @GetMapping("/game/state")
    // public Result getGameState(HttpServletRequest request) {
    //     Integer userId = (Integer) request.getAttribute("currentUserId");
    //     // Use userId to fetch game state...
    //     return Result.success(...);
    // }
    // Add a logger instance at the top of the class
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

//    //增加信息
//    @PostMapping("/add")
//    public Result add(@RequestBody User user) {
//        try {
//            userService.insertUser(user);
//        } catch (Exception e) {
//            return Result.error("插入数据错误");
//        }
//        return Result.success();
//    }
//
//    //删除信息
//    @DeleteMapping("/delete/{id}")
//    public Result delete(@PathVariable Integer id) {
//        userService.deleteUser(id);
//        return Result.success();
//    }
//
//    //批量删除信息
//    /*@DeleteMapping("/delete/batch")
//    public Result batchdelete(@RequestBody List<Integer> ids) {
//        userService.batchdeleteUser(ids);
//        return Result.success();
//    }*/
//
//    //id查询信息
//    @GetMapping("/select/{id}")
//    public Result selectById(@PathVariable Integer id) {
//        User user = userService.selectUser(id);
//        return Result.success(user);
//    }
//    //查询所有
//    @GetMapping("/select/All")
//    public Result selectAll() {
//        List<User> users = userService.selectAllUser();
//        return Result.success(users);
//    }
//
//    //更改信息
//    @PutMapping("/update")
//    public Result update(@RequestBody User user) {
//        userService.updateUser(user);
//        return Result.success();
//    }
}
