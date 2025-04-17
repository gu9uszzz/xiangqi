<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const gameModes = [
  {
    name: '五分钟速战',
    duration: 5,
    description: '快速对战模式，每局5分钟'
  },
  {
    name: '十分钟普通战',
    duration: 10,
    description: '标准对战模式，每局10分钟'
  },
  {
    name: '二十分钟持久战',
    duration: 20,
    description: '持久对战模式，每局20分钟'
  }
]

const startGame = (mode) => {
  router.push({
    path: '/game',
    query: { mode }
  })
}

// 新增：返回上一页的函数
const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="game-lobby">
    <nav class="navbar">
      <button @click="goBack" class="back-btn nav-brand">返回</button>
    </nav>
    <div class="content">
      <h1>游戏大厅</h1>
      <div class="lobby-container">
        <h2>在线对战大厅</h2>
        <div class="mode-selection">
          <div
            v-for="mode in gameModes"
            :key="mode.duration"
            class="mode-card"
            @click="startGame(mode.duration)"
          >
            <h3>{{ mode.name }}</h3>
            <p>{{ mode.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.game-lobby {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-image: url('../pictures/1.webp');
  background-size: 100% 100%;
  background-position: center;
  background-repeat: no-repeat;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

/* 添加导航栏样式 */
.navbar {
  background-color: rgba(51, 51, 51, 0.3);
  padding: 1rem;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  backdrop-filter: blur(3px);
}

.nav-brand {
  color: white;
  font-size: 1rem;
  font-weight: bold;
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

/* 修改内容区域样式 */
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.lobby-container {
  max-width: 800px;
  width: 90%;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(3px);
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: rgba(0, 0, 0, 0.7);
  font-size: 2rem;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: rgba(0, 0, 0, 0.7);
  font-size: 24px;
}

.mode-selection {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.mode-card {
  flex: 1;
  padding: 20px;
  border: 1px solid rgba(175, 76, 147, 0.3);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.mode-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(175, 76, 147, 0.2);
  background-color: rgba(255, 255, 255, 0.6);
}

.mode-card h3 {
  color: rgba(175, 76, 147, 0.8);
  margin-bottom: 10px;
  font-size: 18px;
}

.mode-card p {
  color: rgba(0, 0, 0, 0.6);
  font-size: 14px;
  margin: 0;
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
</style>
