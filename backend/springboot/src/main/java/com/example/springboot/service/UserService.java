package com.example.springboot.service;

import cn.hutool.log.Log;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

//    @Autowired
//    private HttpSession session;

    @Autowired
    private VerificationCodeService verificationCodeService; // Inject VerificationCodeService

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public void deleteUser(Integer id) {
        userMapper.delete(id);
    }

    //批量删除(可能有用)
    /*public void batchdeleteUser(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.delete(id);
        }
    }*/

    //id查询
    public User selectUser(Integer id) {
        return userMapper.selectByUserId(id);
    }

    public List<User> selectAllUser() {
        return userMapper.selectAll();
    }

    public void updateUser(User user) {
        userMapper.update(user);
    }


//    public User login(User user) {
//        User dbUser = userMapper.selectByEmail(user.getEmail());
//        if (dbUser == null) {
//            throw new ServiceException("邮箱或密码错误");
//        }
//        if(!user.getPassword().equals(dbUser.getPassword())) {
//            throw new ServiceException("邮箱或密码错误");
//        }
//        String sessionCode = (String) session.getAttribute("code_" + user.getEmail());
//        if (!user.getCode().equals(sessionCode)) {
//            throw new ServiceException("验证码错误");
//        }
//        session.removeAttribute("code_" + user.getEmail());
//        return dbUser;
//    }
// Add Login Method
public User login(String username, String password) {
    User dbUser = userMapper.selectByUsername(username);
    if (dbUser == null) {
        throw new ServiceException("用户名或密码错误");
    }
    // Verify password using PasswordEncoder
    if (!passwordEncoder.matches(password, dbUser.getPassword())) {
        throw new ServiceException("用户名或密码错误");
    }
    // Login successful
    dbUser.setPassword(null); // Don't send password hash back
    return dbUser;
}


    private void validateUserRegistrationInput(User user) {
        // Controller already checks for blank fields
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) { // Example length check
            throw new ServiceException("密码长度需在6-20位之间");
        }
        // Add email format validation if needed
        // Add username format validation if needed
    }

//    //注册验证
//    public User register(User user) {
//        User selectUsername = userMapper.selectByUsername(user.getUsername());
//        User selectEmail = userMapper.selectByEmail(user.getEmail());
//        if (selectUsername != null && selectEmail != null) {
//            throw new ServiceException("用户名或邮箱存在");
//        }
//        String sessionCode = (String) session.getAttribute("code_" + user.getEmail());
//        if (!user.getCode().equals(sessionCode)) {
//            throw new ServiceException("验证码错误");
//        }
//        session.removeAttribute("code_" + user.getEmail());
//        user.setUsername(user.getUsername());
//        user.setEmail(user.getEmail());
//        userMapper.insert(user);
//        return user;
//    }

    @Transactional // Make registration transactional
    public User register(User user) {
        // 1. Input Validation (Controller might do some, add more here if needed)
        validateUserRegistrationInput(user);
        // 2. Check if username or email already exists
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new ServiceException("用户名已存在");
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            throw new ServiceException("该邮箱已被注册");
        }
        // 3. Validate verification code from DB
        boolean isCodeValid = verificationCodeService.validateCode(user.getEmail(), user.getCode());
        if (!isCodeValid) {
            throw new ServiceException("验证码错误或已过期");
        }
        // 4. Code is valid, delete it so it cannot be reused
        verificationCodeService.deleteUsedCode(user.getEmail(), user.getCode());
        // 5. Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 6. Clear the verification code from the user object before inserting
        user.setCode(null);
        // 7. Insert the new user
        int inserted = userMapper.insert(user); // Ensure your insert method works correctly
        if (inserted <= 0) {
            throw new ServiceException("注册失败，请稍后重试");
        }
        // Assuming insert populates the ID in the user object if needed
        // 8. Return the registered user (without password/code ideally)
        user.setPassword(null); // Don't return password hash
        return user;
    }

    //修改密码
    public void resetPassword(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser == null) {
            // 抛出一个自定义的异常
            throw new ServiceException("用户名不存在");
        }
        if (!user.getEmail().equals(dbUser.getEmail())) {
            throw new ServiceException("邮箱不存在");
        }
        userMapper.updatepass(user);
    }
}
