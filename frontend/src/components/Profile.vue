<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// --- State for Profile Data ---
const username = ref('玩家0001');
const phone = ref('138****8888'); // Keep masked for display
const registrationDate = ref('2023-01-01');
const gamesPlayed = ref(128); // Could be calculated from W+L+D
const winRate = ref(65); // Could be calculated

// --- NEW Enhanced Profile Data ---
const eloRating = ref(1650);
const rankTitle = ref('棋师'); // Calculated based on ELO
const wins = ref(83);
const losses = ref(40);
const draws = ref(5);
const highestWinStreak = ref(12);
const bio = ref('热爱象棋，以棋会友！'); // Editable
const location = ref('广西桂林'); // Editable
const userId = ref('UID-a1b2c3d4e5'); // Usually non-editable
const lastLogin = ref('2024-03-15 10:30'); // Usually system-updated
const achievementsUnlocked = ref(25); // Example count

// --- State for Editing Mode ---
const isEditing = ref(false);

// --- Temporary Refs for Edits ---
const editedUsername = ref('');
const editedPhone = ref(''); // Might need separate logic for editing phone (verification)
const editedBio = ref('');
const editedLocation = ref('');

// --- Navigation ---
const navigateTo = (path) => {
  router.push(path);
};

// --- 新增：返回上一页的函数 ---
const goBack = () => {
  router.back();
};

// --- Placeholder Navigation/Actions ---
const viewMatchHistory = () => {
  console.log('Navigating to match history...');
  // router.push('/match-history'); // Example navigation
  alert('功能暂未开放：查看对战历史');
};

const viewAchievements = () => {
  console.log('Navigating to achievements...');
  // router.push('/achievements'); // Example navigation
  alert('功能暂未开放：查看成就');
};


// --- Edit Logic ---
const startEditing = () => {
  editedUsername.value = username.value;
  editedPhone.value = phone.value; // Consider fetching unmasked phone if editing allowed
  editedBio.value = bio.value;
  editedLocation.value = location.value;
  isEditing.value = true;
};

const saveChanges = () => {
  // --- Placeholder for actual save logic ---
  // In a real app, send data to your backend API
  console.log('Saving changes:', {
    username: editedUsername.value,
    phone: editedPhone.value, // Handle phone update carefully
    bio: editedBio.value,
    location: editedLocation.value,
  });

  // --- Update the main refs with edited values ---
  username.value = editedUsername.value;
  phone.value = editedPhone.value; // Update display phone (might need re-masking)
  bio.value = editedBio.value;
  location.value = editedLocation.value;

  isEditing.value = false;
  // Optionally: show a success message
  alert('信息已保存！'); // Simple feedback
};

const cancelEdit = () => {
  isEditing.value = false;
};
</script>

<template>
  <div class="profile-container">
    <nav class="navbar">
      <button @click="goBack" class="back-btn nav-brand">返回</button>
      <div class="nav-links">
        <a @click="navigateTo('/home')">首页</a>
        <a @click="navigateTo('/profile')" class="active">个人信息</a>
        <a @click="navigateTo('/rankings')">排行榜</a>
        <a @click="navigateTo('/settings')">设置</a>
      </div>
    </nav>

    <main class="main-content">
      <h1>个人信息</h1>
      <div class="profile-info">
        <!-- Avatar Section -->
        <div class="avatar-section">
          <div class="avatar">
            <img src="../pictures/2.webp" alt="用户头像">
          </div>
          <!-- Add Edit Avatar Button/Logic if needed -->
        </div>

        <!-- Info Section -->
        <div class="info">
          <!-- Display Mode -->
          <template v-if="!isEditing">
            <h2>{{ username }}</h2>
            <p><strong>用户ID：</strong> {{ userId }}</p>
            <p><strong>手机号：</strong> {{ phone }}</p>
            <p><strong>所在地：</strong> {{ location }}</p>
            <p><strong>个性签名：</strong> {{ bio }}</p>
            <hr>
            <h3>棋力统计</h3>
            <p><strong>等级分：</strong> {{ eloRating }} ({{ rankTitle }})</p>
            <p><strong>总对局：</strong> {{ gamesPlayed }} （胜 {{ wins }} / 负 {{ losses }} / 和 {{ draws }}）</p>
            <p><strong>胜率：</strong> {{ winRate }}%</p>
            <p><strong>最高连胜：</strong> {{ highestWinStreak }}</p>
            <hr>
            <h3>账户信息</h3>
            <p><strong>注册时间：</strong> {{ registrationDate }}</p>
            <p><strong>上次登录：</strong> {{ lastLogin }}</p>
            <p><strong>已获成就：</strong> {{ achievementsUnlocked }} 个</p>
          </template>

          <!-- Edit Mode -->
          <template v-else>
            <h2>编辑信息</h2>
            <div class="form-group">
              <label for="username">用户名：</label>
              <input id="username" v-model="editedUsername" type="text">
            </div>
             <div class="form-group">
              <label for="phone">手机号：</label>
              <input id="phone" v-model="editedPhone" type="tel" placeholder="（谨慎修改）">
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
            <!-- Display non-editable stats even in edit mode -->
            <h3>棋力统计（不可编辑）</h3>
            <p><strong>用户ID：</strong> {{ userId }}</p>
            <p><strong>等级分：</strong> {{ eloRating }} ({{ rankTitle }})</p>
            <p><strong>总对局：</strong> {{ gamesPlayed }} （胜 {{ wins }} / 负 {{ losses }} / 和 {{ draws }}）</p>
             <p><strong>注册时间：</strong> {{ registrationDate }}</p>
             <p><strong>上次登录：</strong> {{ lastLogin }}</p>
          </template>

          <!-- Action Buttons -->
          <div class="action-buttons">
             <!-- Buttons visible in display mode -->
             <template v-if="!isEditing">
                <button @click="startEditing" class="btn btn-edit">修改信息</button>
                <button @click="viewMatchHistory" class="btn btn-secondary">查看对战历史</button>
                <button @click="viewAchievements" class="btn btn-secondary">查看成就</button>
             </template>
             <!-- Buttons visible in edit mode -->
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

<style scoped>
/* --- Existing Styles (Navbar, Container etc. - Keep As Is) --- */
.profile-container {
  width: 100vw;
  height: 100vh;
  background-image: url('../pictures/3.webp');
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
}

.navbar {
  background-color: rgba(51, 51, 51, 0.3);
  padding: 1rem;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  backdrop-filter: blur(3px);
  z-index: 10;
}

.nav-brand {
  color: white;
  font-size: 1rem;
  font-weight: bold;
  margin-right: auto;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-brand:hover {
  background-color: rgba(85, 85, 85, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.nav-links {
  display: flex;
  gap: 1rem;
  margin-right: 2rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s;
  position: relative;
}

.nav-links a:hover {
  background-color: rgba(85, 85, 85, 0.7);
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.nav-links a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background-color: white;
  transition: all 0.3s;
  transform: translateX(-50%);
}

.nav-links a:hover::after {
  width: 80%;
}

.nav-links a.active {
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.nav-links a.active::after {
  width: 80%;
  background-color: white;
}

/* --- Main Content & Profile Specific Styles --- */
.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  width: 90%;
  margin: 2rem auto;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  backdrop-filter: blur(3px);
  color: rgba(0, 0, 0, 0.8);
  overflow-y: auto;
}

/* 添加滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 10px;
}

.main-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 5px;
}

.main-content::-webkit-scrollbar-thumb {
  background: rgba(255, 192, 203, 0.7);  /* 粉色背景 */
  border-radius: 5px;
  border: 2px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 182, 193, 0.9);  /* 浅粉色，悬停时加深 */
}

/* Firefox 滚动条样式 */
.main-content {
  scrollbar-width: thin;
  scrollbar-color: rgba(255, 192, 203, 0.7) rgba(255, 255, 255, 0.1);
}

h1 {
  margin-bottom: 2rem;
  color: rgba(0, 0, 0, 0.8);
  text-align: center;
}

.profile-info {
  display: flex;
  gap: 2rem;
  padding: 0rem;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  backdrop-filter: blur(3px);
}

.avatar-section {
  flex-shrink: 0;
}

.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.3);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info {
  flex: 1;
  padding: 1rem;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 8px;
}

.info h2 {
  margin-bottom: 1rem;
  color: rgba(0, 0, 0, 0.8);
}

.info p {
  margin: 0.5rem 0;
  color: rgba(0, 0, 0, 0.7);
}

.info hr {
  margin: 1rem 0;
  border: none;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-edit {
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
}

.btn-edit:hover {
  background-color: rgba(175, 76, 147, 0.8);
}

.btn-secondary {
  background-color: rgba(100, 100, 100, 0.6);
  color: white;
}

.btn-secondary:hover {
  background-color: rgba(100, 100, 100, 0.8);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: rgba(0, 0, 0, 0.7);
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 4px;
  background-color: rgba(255, 255, 255, 0.3);
  color: rgba(0, 0, 0, 0.8);
}

.form-group-textarea textarea {
  resize: vertical;
  min-height: 100px;
}

.btn-save {
  background-color: rgba(76, 175, 80, 0.6);
  color: white;
}

.btn-save:hover {
  background-color: rgba(76, 175, 80, 0.8);
}

.btn-cancel {
  background-color: rgba(244, 67, 54, 0.6);
  color: white;
}

.btn-cancel:hover {
  background-color: rgba(244, 67, 54, 0.8);
}

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
