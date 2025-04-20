<template>
  <div class="profile-container">
    <nav class="navbar">
      <div class="nav-brand">象棋游戏</div>
      <div class="nav-links">
        <a @click="navigateTo('/home')">首页</a>
        <a @click="navigateTo('/profile')" class="active">个人信息</a>
        <a @click="navigateTo('/rankings')">排行榜</a>
        <a @click="navigateTo('/settings')">设置</a>
      </div>
    </nav>

    <main class="main-content">
       <button @click="goBack" class="back-btn top-left-back-btn">返回</button>
      <h1>个人信息</h1>
      <div class="profile-info">
        <!-- 头像区域 -->
        <div class="avatar-section">
          <div class="avatar">
            <img src="../pictures/2.webp" alt="用户头像">
          </div>
        </div>

        <!-- 信息区域 -->
        <div class="info">
          <!-- 显示模式 -->
          <template v-if="!isEditing">
            <h2>{{ username }}</h2>
            <p><strong>用户ID：</strong> {{ userId }}</p>
            <p><strong>邮箱：</strong> {{ email }}</p> <!-- 从 手机号 改为 邮箱 -->
            <p><strong>所在地：</strong> {{ location }}</p>
            <p><strong>个性签名：</strong> {{ bio }}</p>
            <hr>
            <h3>棋力统计</h3>
            <p><strong>等级分：</strong> {{ eloRating }} ({{ rankTitle }})</p>
            <p><strong>总对局：</strong> {{ gamesPlayed }} （胜 {{ wins }} / 负 {{ losses }} / 和 {{ draws }}）</p>
            <p><strong>胜率：</strong> {{ winRate }}%</p>
            <!-- 已移除: 最高连胜显示 -->
            <hr>
            <h3>账户信息</h3>
            <p><strong>注册时间：</strong> {{ registrationDate }}</p>
            <p><strong>上次登录：</strong> {{ lastLogin }}</p>
            <!-- 已移除: 成就显示 -->
          </template>

          <!-- 编辑模式 -->
          <template v-else>
            <h2>编辑信息</h2>
            <div class="form-group">
              <label for="username">用户名：</label>
              <input id="username" v-model="editedUsername" type="text">
            </div>
             <div class="form-group">
              <label for="email">邮箱：</label> <!-- 从 手机号 改为 邮箱 -->
              <input id="email" v-model="editedEmail" type="email" placeholder="请输入邮箱地址"> <!-- 使用 type="email" -->
            </div>
             <div class="form-group">
              <label for="location">所在地：</label>
              <input id="location" v-model="editedLocation" type="text">
            </div>
             <div class="form-group form-group-textarea">
              <label for="bio">个性签名：</label>
              <textarea id="bio" v-model="editedBio" rows="3"></textarea>
            </div>
            <hr>
            <!-- 编辑模式下也显示不可编辑的统计数据 -->
            <h3>棋力统计（不可编辑）</h3>
            <p><strong>用户ID：</strong> {{ userId }}</p>
            <p><strong>等级分：</strong> {{ eloRating }} ({{ rankTitle }})</p>
            <p><strong>总对局：</strong> {{ gamesPlayed }} （胜 {{ wins }} / 负 {{ losses }} / 和 {{ draws }}）</p>
             <p><strong>注册时间：</strong> {{ registrationDate }}</p>
             <p><strong>上次登录：</strong> {{ lastLogin }}</p>
             <!-- 已移除: 最高连胜和成就在此区域原本就不显示 -->
          </template>

          <!-- 操作按钮 -->
          <div class="action-buttons">
             <!-- 显示模式下的按钮 -->
             <template v-if="!isEditing">
                <button @click="startEditing" class="btn btn-edit">修改信息</button>
                
             </template>
             <!-- 编辑模式下的按钮 -->
             <template v-else>
              <button @click="saveChanges" class="btn btn-save">保存</button>
              <button @click="cancelEdit" class="btn btn-cancel">取消</button>
            </template>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// --- 个人信息状态 ---
const username = ref('玩家001');
const email = ref('player001@example.com'); // 从 phone 改为 email
const registrationDate = ref('2023-01-01');
const gamesPlayed = ref(128); // 可根据胜负和计算
const winRate = ref(65); // 可计算

// --- 新增的详细个人信息 ---
const eloRating = ref(1650);
const rankTitle = ref('棋师'); // 根据 ELO 计算
const wins = ref(83);
const losses = ref(40);
const draws = ref(5);
// 已移除: const highestWinStreak = ref(12);
const bio = ref('热爱象棋，以棋会友！'); // 可编辑
const location = ref('广西桂林'); // 可编辑
const userId = ref('UID-a1b2c3d4e5'); // 通常不可编辑
const lastLogin = ref('2024-03-15 10:30'); // 通常由系统更新
// 已移除: const achievementsUnlocked = ref(25);

// --- 编辑模式状态 ---
const isEditing = ref(false);

// --- 用于编辑的临时 Ref ---
const editedUsername = ref('');
const editedEmail = ref(''); // 从 editedPhone 改为 editedEmail
const editedBio = ref('');
const editedLocation = ref('');

// --- 导航 ---
const navigateTo = (path) => {
  router.push(path);
};

// --- 返回上一页的函数 ---
const goBack = () => {
  router.back();
};



// --- 模拟数据 (实际应用中需要从后端获取) ---
// --- 编辑逻辑 ---
const startEditing = () => {
  editedUsername.value = username.value;
  editedEmail.value = email.value; // 更新为 email
  editedBio.value = bio.value;
  editedLocation.value = location.value;
  isEditing.value = true;
};

const saveChanges = () => {
  // --- 实际保存逻辑的占位符 ---
  // 在真实应用中，这里会发送数据到后端 API
  console.log('保存更改:', {
    username: editedUsername.value,
    email: editedEmail.value, // 更新为 email
    bio: editedBio.value,
    location: editedLocation.value,
  });

  // --- 使用编辑后的值更新主要的 Ref ---
  username.value = editedUsername.value;
  email.value = editedEmail.value; // 更新为 email
  bio.value = editedBio.value;
  location.value = editedLocation.value;

  isEditing.value = false;
  alert('信息已保存！'); // 简单反馈
};

const cancelEdit = () => {
  isEditing.value = false;
};
</script>


<style scoped>
/* --- 现有样式 (Navbar, Container 等 - 保持不变) --- */
.profile-container {
  width: 100vw;
  height: 100vh;
  background-image: url('../pictures/3.webp');
  background-size: cover; /* 使用 cover 更好地缩放 */
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  /* 新增：为绝对定位的子元素提供基准 */
  position: relative;
}

.navbar {
  background-color: rgba(51, 51, 51, 0.4); /* 轻微增加不透明度 */
  padding: 1rem 1.5rem; /* 更多内边距 */
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  backdrop-filter: blur(4px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.nav-brand {
  color: white;
  font-size: 1.6rem; /* 稍大字体 */
  font-weight: bold;
  margin-right: auto;
  text-shadow: 1px 1px 3px rgba(0,0,0,0.3); /* 添加细微阴影 */
}

.nav-links {
  display: flex;
  gap: 1.2rem; /* 更大间距 */
  margin-right: 2rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  cursor: pointer;
  padding: 0.6rem 1.2rem; /* 调整内边距 */
  border-radius: 5px; /* 更圆的边角 */
  transition: all 0.3s ease;
  position: relative;
  font-size: 1.05rem; /* 稍大字体 */
}

.nav-links a:hover {
  background-color: rgba(85, 85, 85, 0.75);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.nav-links a::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  width: 0;
  height: 2px;
  background-color: white;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-links a:hover::after {
  width: 70%; /* 调整宽度 */
}

.nav-links a.active {
  background-color: rgba(175, 76, 147, 0.7); /* 更强的激活颜色 */
  color: white;
  font-weight: bold;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.25);
}

.nav-links a.active::after {
  width: 70%; /* 匹配悬停效果 */
  background-color: white;
}

/* --- 主要内容 & 个人信息特定样式 --- */
.main-content {
  flex: 1;
  padding: 2rem 2.5rem; /* 更多内边距 */
  max-width: 1000px; /* 调整最大宽度 */
  width: 85%; /* 调整宽度 */
  margin: 2.5rem auto; /* 更多上下外边距 */
  background-color: rgba(255, 255, 255, 0.35); /* 更不透明 */
  border-radius: 15px; /* 更圆的边角 */
  backdrop-filter: blur(6px); /* 增加模糊 */
  color: rgba(0, 0, 0, 0.9); /* 更深的文字颜色 */
  box-shadow: 0 6px 20px rgba(0,0,0,0.15); /* 增强阴影 */
  overflow-y: auto; /* 确保内容过长时可滚动 */
  /* 新增：如果按钮放在这里，也需要相对定位 */
  position: relative;
}

h1 {
  /* 新增：为按钮留出空间 */
  margin-top: 3rem;
  margin-bottom: 2rem; /* 更多间距 */
  color: rgba(0, 0, 0, 0.9);
  text-align: center;
  font-weight: 600;
  font-size: 2.2rem; /* 更大标题 */
}

h2 {
    margin-top: 0;
    margin-bottom: 1.2rem;
    font-size: 1.6rem;
    color: #333;
    border-bottom: 1px solid rgba(0,0,0,0.1);
    padding-bottom: 0.5rem;
}

h3 {
    margin-top: 1.5rem;
    margin-bottom: 1rem;
    font-size: 1.3rem;
    color: #555;
}

hr {
    border: none;
    border-top: 1px solid rgba(0, 0, 0, 0.15);
    margin: 1.5rem 0;
}


.profile-info {
  display: flex;
  flex-wrap: wrap;
  gap: 2.5rem; /* 增加间距 */
  margin-top: 1rem; /* 由于 h1 有更多下边距，减少这里的上边距 */
  align-items: flex-start;
}

.avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    flex-basis: 150px; /* 给头像区域一个基础宽度 */
    flex-shrink: 0;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.6); /* 更粗的边框 */
  box-shadow: 0 4px 10px rgba(0,0,0,0.25);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info {
  flex: 1;
  min-width: 300px; /* 确保信息区域不会变得太小 */
}

.info p, .form-group {
  margin-bottom: 0.8rem; /* 微调间距 */
  font-size: 1.05rem; /* 稍大的基础字体 */
  line-height: 1.6;
}

.info strong, .form-group label {
    display: inline-block; /* 确保布局一致 */
    min-width: 90px; /* 调整标签宽度 */
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);
    margin-right: 0.5rem; /* 标签和值/输入框之间的间距 */
    vertical-align: top; /* 对多行文本域，标签顶部对齐 */
}
.info strong {
    color: rgba(0, 0, 0, 0.65); /* 显示模式下标签颜色稍浅 */
}


/* 编辑模式样式 */
.form-group {
    display: flex;
    align-items: center; /* 垂直对齐标签和输入框 */
    gap: 0.5rem;
    flex-wrap: wrap; /* 需要时允许换行 */
}
.form-group.form-group-textarea {
    align-items: flex-start; /* 文本域标签顶部对齐 */
}

.form-group label {
    flex-shrink: 0;
}

.form-group input[type="text"],
.form-group input[type="tel"], /* 保留 tel 的样式规则以防万一 */
.form-group input[type="email"], /* 为 email 添加样式规则 */
.form-group textarea {
    flex-grow: 1;
    padding: 0.7rem 1rem; /* 输入框内更多内边距 */
    border: 1px solid rgba(0, 0, 0, 0.25);
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.8); /* 更不透明的输入框背景 */
    font-size: 1rem;
    color: #222; /* 更深的输入文字颜色 */
    transition: border-color 0.3s, box-shadow 0.3s;
    min-width: 200px; /* 防止输入框变得太小 */
}
.form-group textarea {
    resize: vertical; /* 允许垂直调整大小 */
    min-height: 80px; /* 文本域最小高度 */
}

.form-group input[type="text"]:focus,
.form-group input[type="tel"]:focus,
.form-group input[type="email"]:focus, /* 为 email 添加焦点样式 */
.form-group textarea:focus {
    outline: none;
    border-color: rgba(175, 76, 147, 0.9);
    box-shadow: 0 0 0 3px rgba(175, 76, 147, 0.25);
}

/* 操作按钮 */
.action-buttons {
  margin-top: 2.5rem; /* 按钮上方更多空间 */
  display: flex;
  flex-wrap: wrap; /* 允许按钮在小屏幕上换行 */
  gap: 1rem;
}

.btn {
  padding: 0.8rem 1.8rem; /* 更大的按钮 */
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0,0,0,0.15);
  text-align: center;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.2);
}
.btn:active {
    transform: translateY(0px);
    box-shadow: 0 1px 3px rgba(0,0,0,0.2);
}

.btn-edit {
  background-color: #4a90e2;
  color: white;
}
.btn-edit:hover { background-color: #3a7bc8; }

.btn-save {
  background-color: #50e3c2;
  color: #333;
}
.btn-save:hover { background-color: #38c9a8; }

.btn-cancel {
  background-color: #f5a623;
  color: white;
}
.btn-cancel:hover { background-color: #d9901f; }

.btn-secondary {
  background-color: #777;
  color: white;
}
.btn-secondary:hover { background-color: #666; }

/* 新增：返回按钮样式 */
.back-btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  background-color: rgba(100, 100, 100, 0.6); /* 中性灰色 */
  color: white;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.back-btn:hover {
  background-color: rgba(120, 120, 120, 0.8);
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0,0,0,0.15);
}

/* 新增：返回按钮定位（左上角） */
.top-left-back-btn {
  position: absolute;
  top: 1.5rem; /* 根据需要调整 */
  left: 1.5rem; /* 根据需要调整 */
  z-index: 10; /* 确保在其他元素之上 */
}
</style>