<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path) => {
  router.push(path)
}

// 新增：返回上一页的函数
const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="rankings-container">
    <nav class="navbar">
      <button @click="goBack" class="back-btn nav-brand">返回</button>
      <div class="nav-links">
        <a @click="navigateTo('/home')">首页</a>
        <a @click="navigateTo('/profile')">个人信息</a>
        <a @click="navigateTo('/rankings')" class="active">排行榜</a>
        <a @click="navigateTo('/settings')">设置</a>
      </div>
    </nav>
    <main class="main-content">
      <h1>排行榜</h1>
      <div class="ranking-tabs">
        <button class="tab-btn active">总榜</button>
        <button class="tab-btn">周榜</button>
        <button class="tab-btn">月榜</button>
      </div>
      <div class="ranking-list-container">
        <div class="ranking-list">
          <div class="ranking-header">
            <span class="rank">排名</span>
            <span class="player">玩家</span>
            <span class="score">积分</span>
            <span class="wins">胜场</span>
          </div>
          <div class="ranking-item top-1">
            <span class="rank">1</span>
            <span class="player">玩家001</span>
            <span class="score">1280</span>
            <span class="wins">128</span>
          </div>
          <div class="ranking-item top-2">
            <span class="rank">2</span>
            <span class="player">玩家002</span>
            <span class="score">1150</span>
            <span class="wins">115</span>
          </div>
          <div class="ranking-item top-3">
            <span class="rank">3</span>
            <span class="player">玩家003</span>
            <span class="score">980</span>
            <span class="wins">98</span>
          </div>
          <div class="ranking-item">
            <span class="rank">4</span>
            <span class="player">玩家004</span>
            <span class="score">850</span>
            <span class="wins">85</span>
          </div>
          <div class="ranking-item">
            <span class="rank">5</span>
            <span class="player">玩家005</span>
            <span class="score">720</span>
            <span class="wins">72</span>
          </div>
          <div class="ranking-item">
            <span class="rank">6</span>
            <span class="player">玩家006</span>
            <span class="score">680</span>
            <span class="wins">68</span>
          </div>
          <div class="ranking-item">
            <span class="rank">7</span>
            <span class="player">玩家007</span>
            <span class="score">650</span>
            <span class="wins">65</span>
          </div>
          <div class="ranking-item">
            <span class="rank">8</span>
            <span class="player">玩家008</span>
            <span class="score">620</span>
            <span class="wins">62</span>
          </div>
          <div class="ranking-item">
            <span class="rank">9</span>
            <span class="player">玩家009</span>
            <span class="score">590</span>
            <span class="wins">59</span>
          </div>
          <div class="ranking-item">
            <span class="rank">10</span>
            <span class="player">玩家010</span>
            <span class="score">560</span>
            <span class="wins">56</span>
          </div>
          <div class="ranking-item">
            <span class="rank">11</span>
            <span class="player">玩家011</span>
            <span class="score">530</span>
            <span class="wins">53</span>
          </div>
          <div class="ranking-item">
            <span class="rank">12</span>
            <span class="player">玩家012</span>
            <span class="score">500</span>
            <span class="wins">50</span>
          </div>
          <div class="ranking-item">
            <span class="rank">13</span>
            <span class="player">玩家013</span>
            <span class="score">470</span>
            <span class="wins">47</span>
          </div>
          <div class="ranking-item">
            <span class="rank">14</span>
            <span class="player">玩家014</span>
            <span class="score">440</span>
            <span class="wins">44</span>
          </div>
          <div class="ranking-item">
            <span class="rank">15</span>
            <span class="player">玩家015</span>
            <span class="score">410</span>
            <span class="wins">41</span>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.rankings-container {
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

h1 {
  margin-bottom: 2rem;
  color: rgba(0, 0, 0, 0.8);
  text-align: center;
}

.ranking-tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  justify-content: center;
}

.tab-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background-color: rgba(255, 255, 255, 0.3);
  color: rgba(0, 0, 0, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-btn.active {
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
}

.tab-btn:hover:not(.active) {
  background-color: rgba(255, 255, 255, 0.4);
}

.ranking-list-container {
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(2px);
}

.ranking-list {
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  overflow: hidden;
}

.ranking-header {
  display: grid;
  grid-template-columns: 0.5fr 2fr 1fr 1fr;
  padding: 1rem;
  background-color: rgba(175, 76, 147, 0.6);
  color: white;
  font-weight: bold;
}

.ranking-item {
  display: grid;
  grid-template-columns: 0.5fr 2fr 1fr 1fr;
  padding: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: background-color 0.3s;
}

.ranking-item:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.top-1 {
  background-color: rgba(255, 215, 0, 0.3);
}

.top-2 {
  background-color: rgba(192, 192, 192, 0.3);
}

.top-3 {
  background-color: rgba(205, 127, 50, 0.3);
}

.rank, .player, .score, .wins {
  display: flex;
  align-items: center;
}

.rank {
  font-weight: bold;
}

.back-btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  background-color: rgba(100, 100, 100, 0.6);
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

.top-left-back-btn {
  position: absolute;
  top: 1.5rem;
  left: 1.5rem;
  z-index: 10;
}
</style>
