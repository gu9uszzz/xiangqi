<script setup>
import { ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
// import { ElMessage } from 'element-plus' // 如果你用了 Element Plus，可以取消这行注释，并替换下面的 alert

const router = useRouter()
const username = ref('') // 新增：用户名
const email = ref('')    // 修改：使用 email 代替 phoneNumber
const password = ref('')
const confirmPassword = ref('')
const verificationCode = ref('') // 验证码输入
const isCodeSent = ref(false)
const countdown = ref(0)
let countdownTimer = null // 用于存储定时器 ID

// 后端 API 基础 URL
const API_BASE_URL = 'http://localhost:9090' // 你的后端地址

// --- 新增：邮箱验证函数 ---
const validateEmail = (email) => {
  // 简单的邮箱格式正则表达式
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// --- 修改：密码验证函数（保持不变） ---
const validatePassword = (password) => {
  const hasUpperCase = /[A-Z]/.test(password)
  const hasLowerCase = /[a-z]/.test(password)
  const hasNumbers = /\d/.test(password)
  const hasValidLength = password.length >= 8 && password.length <= 16
  return hasUpperCase && hasLowerCase && hasNumbers && hasValidLength
}

// --- 修改：倒计时函数（保持不变） ---
const startCountdown = () => {
  if (countdownTimer) {
    clearInterval(countdownTimer);
  }
  countdown.value = 300 // 后端 session 是 5 分钟，这里也设为 300 秒
  countdownTimer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownTimer)
      countdownTimer = null;
      isCodeSent.value = false;
    }
  }, 1000)
}

// --- 修改：发送验证码函数 ---
const sendVerificationCode = async () => {
  if (!validateEmail(email.value)) {
    alert('请输入正确的邮箱地址')
    // ElMessage.error('请输入正确的邮箱地址'); // Element Plus 方式
    return
  }

  isCodeSent.value = true; // 先禁用按钮

  try {
    // 调用后端的 /sendcode 接口
    // 注意: 后端 @GetMapping 使用 @RequestBody 是非标准的，但如果可行，前端可以尝试发送。
    // 更推荐的方式是将后端改为 @PostMapping 或从查询参数获取 email。
    // 这里我们按照后端代码尝试用 POST 发送带 body 的请求给 @GetMapping
    const response = await fetch(`${API_BASE_URL}/user/sendcode`, {
      method: 'POST', // 改为 POST 以匹配 @RequestBody (虽然注解是 GET)
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: email.value // 发送 email
      })
    })

    const data = await response.json()

    // 检查后端返回的 code
    if (response.ok && data.code === '200') {
      startCountdown()
      console.log('验证码已发送，目标邮箱:', email.value);
      // 后端直接返回了验证码 data.data，实际生产中不应返回给前端，这里仅作提示
      alert('验证码已发送至您的邮箱，请注意查收。');
      // ElMessage.success('验证码已发送至您的邮箱，请注意查收。'); // Element Plus 方式
    } else {
      alert(data.msg || `发送验证码失败 (HTTP ${response.status})`)
      // ElMessage.error(data.msg || `发送验证码失败 (HTTP ${response.status})`); // Element Plus 方式
      isCodeSent.value = false; // 发送失败，恢复按钮状态
    }
  } catch (error) {
    console.error('发送验证码请求失败:', error)
    alert('发送验证码请求失败，请检查网络或联系管理员')
    // ElMessage.error('发送验证码请求失败，请检查网络或联系管理员'); // Element Plus 方式
    isCodeSent.value = false; // 发生异常，恢复按钮状态
  }
}

const handleRegister = async () => {
  // --- 前置校验 (保持不变) ---
  if (!username.value || !email.value || !password.value || !verificationCode.value) {
      alert('请填写所有注册信息');
      return;
  }
  if (!validateEmail(email.value)) {
      alert('请输入有效的邮箱地址');
      return;
  }
  // 可以添加密码复杂度等其他校验 (保持不变)
  if (password.value !== confirmPassword.value) { // 添加密码一致性校验是个好习惯，这里加上
      alert('两次输入的密码不一致');
      return;
  }

  console.log('开始注册流程...'); // 调试点 1 (保持不变)

  // --- 调用后端注册接口 ---
  try {
    const requestBody = {
      username: username.value,
      email: email.value,
      password: password.value,
      code: verificationCode.value
    };
    console.log('发送注册请求，Body:', JSON.stringify(requestBody)); // 调试点 2 (保持不变)

    // 注意：请确保这里的 API 地址 '/register' 与你的后端控制器路径一致
    // 如果后端是 /user/register，请将下面地址改为 `${API_BASE_URL}/user/register`
    const response = await fetch(`${API_BASE_URL}/user/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody)
    });

    console.log('收到响应:', response); // 调试点 3 (保持不变)
    console.log('响应状态码 (status):', response.status); // 调试点 4 (保持不变)
    console.log('响应是否成功 (ok):', response.ok); // 调试点 5 (保持不变)

    // --- 解析响应体 (保持不变) ---
    let data = {};
    try {
        const responseClone = response.clone();
        data = await response.json();
        console.log('解析后的 JSON 数据 (data):', data); // 调试点 6 (保持不变)
    } catch (jsonError) {
        console.error('解析 JSON 失败:', jsonError); // 调试点 7 (保持不变)
        try {
             const text = await responseClone.text();
             console.error('原始响应文本:', text); // 调试点 8 (保持不变)
             alert(`注册失败：服务器响应格式错误 (HTTP ${response.status})。响应内容请查看控制台。`);
        } catch (textError) {
            console.error('读取响应文本也失败:', textError); // 调试点 9 (保持不变)
             alert(`注册失败：无法读取服务器响应 (HTTP ${response.status})。`);
        }
        return; // 提前退出 (保持不变)
    }

    // --- 判断注册结果 ---
    if (response.ok && data.code === '200') {
      console.log('判断为注册成功'); // 调试点 10 (保持不变)

      // --- 新增：存储 Token 和 用户信息 ---
      if (data.data && data.data.token) {
        const token = data.data.token;
        const user = data.data.user;
        localStorage.setItem('authToken', token); // 将 Token 存入 localStorage
        console.log('Token 已存储:', token); // 添加日志，方便调试

        // 可选：同时存储用户信息
        if (user) {
          localStorage.setItem('userInfo', JSON.stringify(user));
          console.log('用户信息已存储:', user);
        }
      } else {
        // 虽然注册成功(code 200)，但后端没有按预期返回 token，打印错误信息
        console.error('注册成功，但响应数据中缺少 token:', data);
        // 这里不改变原有流程，但记录下这个异常情况
      }
      // --- 新增逻辑结束 ---

      // --- 保持原有行为：成功提示 和 跳转 ---
      alert('注册成功！即将跳转到登录页面。'); // **保持不变**
      try {
          router.push('/login'); // **保持跳转到登录页不变**
          console.log('已执行跳转到 /login'); // 调试点 11 (保持不变)
      } catch(routerError) {
          console.error('路由跳转失败:', routerError); // 调试点 12 (保持不变)
          alert('注册成功，但页面跳转失败，请手动前往登录页面。'); // **保持不变**
      }
      // --- 保持原有行为结束 ---

    } else {
      // 注册失败或后端返回业务错误代码 (保持不变)
      console.log('判断为注册失败或业务错误'); // 调试点 13 (保持不变)
      const errorMsg = data.msg || `未知错误`;
      const errorCode = data.code || '无业务代码';
      console.error(`注册失败详情 - HTTP Status: ${response.status}, Business Code: ${errorCode}, Message: ${errorMsg}`); // 调试点 14 (保持不变)
      alert(`注册失败: ${errorMsg} (HTTP: ${response.status}, Code: ${errorCode})`);
      // ElMessage.error(`注册失败: ${errorMsg} (HTTP: ${response.status}, Code: ${errorCode})`);
    }

  } catch (error) {
    // 网络错误或其他 fetch 调用本身的错误 (保持不变)
    console.error('注册请求 fetch 失败:', error); // 调试点 15 (保持不变)
    alert('注册请求失败，请检查网络连接或联系管理员。');
    // ElMessage.error('注册请求失败，请检查网络连接或联系管理员。');
  }
};

// --- 跳转到登录页函数（保持不变） ---
const goToLogin = () => {
  router.push('/login')
}


// --- 组件卸载时清除定时器（保持不变） ---
onUnmounted(() => {
  if (countdownTimer) {
    clearInterval(countdownTimer);
  }
});
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <h2>注册</h2>

      <!-- 新增：用户名输入框 -->
      <div class="input-group">
        <input
          type="text"
          v-model="username"
          placeholder="请输入用户名"
        >
      </div>

      <!-- 修改：邮箱输入框与发送验证码按钮 -->
      <div class="input-group">
        <input
          type="email"
          v-model="email"
          placeholder="请输入邮箱"
        >
        <button
          class="send-code-btn"
          @click="sendVerificationCode"
          :disabled="isCodeSent && countdown > 0"
        >
          {{ isCodeSent && countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
        </button>
      </div>

      <!-- 验证码输入框 -->
      <div class="input-group">
        <input
          type="text"
          v-model="verificationCode"
          placeholder="请输入6位验证码"
          maxlength="6"
        >
      </div>

      <!-- 密码输入框 -->
      <div class="input-group">
        <input
          type="password"
          v-model="password"
          placeholder="请输入密码 (8-16位，含大小写字母和数字)"
        >
      </div>

      <!-- 确认密码输入框 -->
      <div class="input-group">
        <input
          type="password"
          v-model="confirmPassword"
          placeholder="请再次确认密码"
        >
        <p class="password-hint">密码至少需要由大写字母、小写字母、数字组成，长度为8-16</p>
      </div>

      <!-- 按钮组 -->
      <div class="button-group">
        <button @click="handleRegister">注册</button>
        <button @click="goToLogin">返回登录</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 样式基本保持不变 */
.register-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('../pictures/1.webp'); /* 确保图片路径正确 */
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.register-container {
  max-width: 400px;
  width: 90%;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: rgba(255, 255, 255, 0.3);
  position: relative;
  z-index: 1;
  backdrop-filter: blur(3px);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: rgba(0, 0, 0, 0.7);
}

.input-group {
  margin-bottom: 15px;
  position: relative; /* 让发送验证码按钮可以绝对定位 */
}

input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  font-size: 16px;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(3px);
  color: rgba(0, 0, 0, 0.8);
}

input::placeholder {
  color: rgba(0, 0, 0, 0.5);
}

/* --- 修改：调整 email 输入框样式以容纳按钮 --- */
.input-group input[type="email"] {
  /* 增加右侧内边距，给按钮留出空间，具体数值可能需要根据按钮实际宽度微调 */
  padding-right: 115px;
}

.send-code-btn {
  position: absolute;
  right: 1px;  /* 紧贴右边框 */
  top: 1px;    /* 紧贴上边框 */
  bottom: 1px; /* 紧贴下边框 */
  width: 100px; /* 固定宽度或根据内容调整 */
  padding: 0 10px;
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
  border: none;
  /* 确保按钮只在右侧有圆角 */
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-size: 14px;
  backdrop-filter: blur(3px);
  transition: all 0.3s ease;
  /* display: flex;
  align-items: center;
  justify-content: center; */
}

.send-code-btn:disabled {
  background-color: rgba(204, 204, 204, 0.5);
  cursor: not-allowed;
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

button:not(.send-code-btn) { /* 避免影响发送验证码按钮 */
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 4px;
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
  font-size: 16px;
  cursor: pointer;
  backdrop-filter: blur(3px);
  transition: all 0.3s ease;
}

button:not(.send-code-btn):hover {
  background-color: rgba(175, 76, 147, 0.8);
}

.password-hint {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.5);
  margin-top: 5px;
  text-align: left;
}
</style>
