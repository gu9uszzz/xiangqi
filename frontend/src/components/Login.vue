<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
// --- 修改：将 phoneNumber 改为 username ---
const username = ref('')
const password = ref('')

// --- 移除：验证码、发送状态、倒计时、登录类型相关的 ref ---
// const verificationCode = ref('')
// const isCodeSent = ref(false)
// const countdown = ref(0)
// const loginType = ref('password') // 不再需要，固定为用户名密码登录
// let countdownTimer = null; // 不再需要

// 后端 API 基础 URL (保持不变)
const API_BASE_URL = 'http://localhost:9090' // 确保这个地址正确

// --- 移除：手机号验证函数 ---
// const validatePhoneNumber = (phone) => { ... }

// --- 移除：倒计时函数 ---
// const startCountdown = () => { ... }

// --- 移除：发送验证码函数 ---
// const sendVerificationCode = async () => { ... }

// --- 修改：登录处理函数 ---
const handleLogin = async () => {
  // --- 修改：校验用户名和密码是否为空 ---
  if (!username.value) {
    alert('请输入用户名')
    return
  }
  if (!password.value) {
    alert('请输入密码')
    return
  }

  // --- 修改：固定使用用户名密码登录 ---
  const url = `${API_BASE_URL}/user/login` // 对接后端 /user/login 接口
  const payload = {
    username: username.value, // 发送 username
    password: password.value
  }

  console.log('发送登录请求, URL:', url);
  console.log('发送登录请求, Body:', JSON.stringify(payload));

  // 执行登录请求
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // 如果后续接口需要认证，可以在这里添加 Authorization Header
        // 'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      },
      body: JSON.stringify(payload)
    })

    console.log('收到登录响应:', response);
    console.log('响应状态码 (status):', response.status);
    console.log('响应是否成功 (ok):', response.ok); // true if status 200-299

    let data = {};
    try {
        const responseClone = response.clone();
        data = await response.json();
        console.log('解析后的 JSON 数据 (data):', data);
    } catch (jsonError) {
        console.error('解析 JSON 失败:', jsonError);
        try {
             const text = await responseClone.text();
             console.error('原始响应文本:', text);
             alert(`登录失败：服务器响应格式错误 (HTTP ${response.status})。响应内容请查看控制台。`);
        } catch (textError) {
            console.error('读取响应文本也失败:', textError);
             alert(`登录失败：无法读取服务器响应 (HTTP ${response.status})。`);
        }
        return;
    }


    // --- 修改：根据后端 Result 结构判断登录结果 ---
    // 关键判断：HTTP 状态码 200-299 且 后端业务代码为 '200'
    if (response.ok && data.code === '200') {
      console.log('判断为登录成功');

      // --- 重要：处理登录成功后的 Token 和用户信息 ---
      // 假设后端成功时返回 { code: '200', msg: '...', data: { user: {...}, token: '...' } }
      if (data.data && data.data.token) {
         const token = data.data.token;
         const user = data.data.user;
         localStorage.setItem('authToken', token); // 将 token 存储在 localStorage
         console.log('登录成功，Token 已存储:', token);

         // 可选：同时存储用户信息 (注意保护敏感信息，后端不应返回密码)
         if (user) {
           localStorage.setItem('userInfo', JSON.stringify(user));
           console.log('用户信息已存储:', user);
         }

         // --- 保持跳转到首页的逻辑 ---
         router.push('/home'); // 登录成功后跳转到首页
         console.log('已执行跳转到 /home');

      } else {
         console.error('登录成功，但响应数据中缺少 token 或 data 结构不符合预期:', data);
         alert('登录成功，但获取用户信息失败，请联系管理员。');
         // 根据你的应用逻辑，决定没有 token 是否能继续，这里选择提示错误
      }
      // --- Token 和用户信息处理结束 ---

    } else {
      // 登录失败或后端返回业务错误代码
      const errorMsg = data.msg || `未知错误`;
      const errorCode = data.code || '无业务代码';
      console.error(`登录失败详情 - HTTP Status: ${response.status}, Business Code: ${errorCode}, Message: ${errorMsg}`);
      alert(`登录失败: ${errorMsg} (Code: ${errorCode})`); // 显示后端返回的错误信息
    }
  } catch (error) {
    console.error('登录请求 fetch 失败:', error)
    alert('登录请求失败，请检查网络连接或联系管理员。')
  }
}

// 跳转到注册页函数 (保持不变)
const goToRegister = () => {
  router.push('/register')
}

// --- 移除：切换登录类型函数 ---
// const switchLoginType = () => { ... }

// --- 移除：组件卸载时清除定时器 ---
// import { onUnmounted } from 'vue';
// onUnmounted(() => { ... });

</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <h2>登录</h2>
      <div class="input-group">
        <!-- --- 修改：改为用户名输入框 --- -->
        <input
          type="text"
          v-model="username"
          placeholder="请输入用户名"
        >
      </div>

      <!-- --- 修改：保留密码输入框，移除 v-if 添加回车登录--- -->
      <div class="input-group">
        <input
          type="password"
          v-model="password"
          placeholder="请输入密码"
          @keyup.enter="handleLogin"  
        >
      </div>

      <!-- --- 移除：验证码输入和发送按钮 --- -->
      <!--
      <div v-else class="input-group">
        ... 验证码输入 ...
      </div>
      -->

      <!-- --- 移除：登录类型切换链接 --- -->
      <!--
      <div class="login-type-switch">
        <a @click="switchLoginType"> ... </a>
      </div>
       -->

      <div class="button-group">
        <button @click="handleLogin">登录</button>
        <button @click="goToRegister">注册</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 样式保持不变 */
.login-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;/* 居中对齐 */
  background-image: url('../pictures/1.webp');
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.login-container {
  max-width: 400px;
  width: 90%;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: rgba(255, 255, 255, 0.3);
  position: relative;/* 用于内部元素定位（如发送验证码按钮）*/
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
  position: relative;
}

input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box; /* 确保 padding 不会撑大输入框 */
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

/* --- 移除：发送验证码按钮的样式 --- */
/* .send-code-btn { ... } */
/* .send-code-btn:disabled { ... } */
/* .input-group input[type="text"] { padding-right: 110px; } */ /* 不再需要给文本框留空间 */


/* --- 移除：登录类型切换的样式 --- */
/* .login-type-switch { ... } */
/* .login-type-switch a { ... } */
/* .login-type-switch a:hover { ... } */

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 20px; /* 可以稍微增加按钮组的上边距 */
}

button {
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

button:hover {
  background-color: rgba(175, 76, 147, 0.8);
}
</style>
