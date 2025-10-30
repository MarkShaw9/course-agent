<template>
  <el-container class="ai-chat-home">



    
    <el-container class="main-layout">
      
      <el-aside width="60px" class="sidebar">
        <el-menu default-active="1" class="el-menu-vertical">
          <el-menu-item index="1">
            <img src="@/imgs/message.png" alt="Message Icon" class="menu-icon" />
          </el-menu-item>
          <el-menu-item index="2">
            <img src="@/imgs/setting.png" alt="Setting Icon" class="menu-icon" />
          </el-menu-item>
          <el-menu-item index="3" class="bottom-item" @click="handleExit">
            <img src="@/imgs/exit.png" alt="Exit Icon" class="menu-icon" />
          </el-menu-item>
        </el-menu>
      </el-aside>

      
      <el-container class="main-container">
        <el-header class="header-title">
          <h2>Start with AI</h2>
        </el-header>

        <el-main class="main-content">
          <div class="card-container">
            
            <el-card class="feature-card" @click="startChat">
              <img src="@/imgs/talk.png" alt="Talk Icon" class="feature-icon" />
              <p>Start to chat with AI</p>
            </el-card>

            
            <el-card class="feature-card" @click="courseSuggestions">
              <img src="@/imgs/suggestion.png" alt="Suggestion Icon" class="feature-icon" />
              <p>Course selection suggestions</p>
            </el-card>

            
            <el-card class="feature-card" @click="studySuggestions">
              <img src="@/imgs/plan.png" alt="Plan Icon" class="feature-icon" />
              <p>Study suggestions for selected courses</p>
            </el-card>
          </div>
        </el-main>

        <el-footer class="chat-footer">
          <div class="message-input-container">
            <el-input
              v-model="userMessage"
              placeholder="Send a message..."
              class="message-input"
              @keyup.enter="sendMessage"
              clearable
            >
              <template #append>
                <el-button type="primary" class="send-button" @click="sendMessage">
                  <img src="@/imgs/send.png" alt="Send Icon" class="send-icon" />
                </el-button>
              </template>
            </el-input>
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userMessage = ref('');





const handleExit = () => {
  ElMessage({
    message: 'Exiting to Home...',
    type: 'info',
  });
  router.push('/course'); 
};

const startChat = () => {
  router.push('/AIchat2'); 
};


const courseSuggestions = () => {
  router.push('/AICourseRecommend'); 
};

const studySuggestions = () => {
  router.push('/AIsuggestion');
};


const sendMessage = () => {
  if (!userMessage.value.trim()) {
    ElMessage({
      message: 'Message cannot be empty!',
      type: 'warning',
    });
    return;
  }
  router.push({
    path: '/AIchat2',
    query: { userMessage: userMessage.value }
  }); 
};
</script>

<style scoped>
.ai-chat-home {
  min-width: 1100px;
  margin-top: 10px;
  height: 90vh; 
  display: flex;
  flex-direction: column;
  overflow: hidden; 
}

.nav-header {
  background: #fff;
  padding: 0 20px; 
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  height: 70px; 
}

.nav-logo {
  height: 40px;
  width: auto; 
  margin-right: 20px; 
}

.nav-bar {
  display: flex;
  align-items: center; 
  justify-content: flex-start; 
  gap: 20px; 
}

.nav-title {
  margin-right: 40px; 
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center; 
}


.nav-links a {
  text-decoration: none; 
  color: #333; 
  font-weight: bold;
  font-size: 14px; 
  position: relative; 
}

.nav-links a:hover {
  color: #409eff; 
}

.nav-links a:hover::after {
  content: ""; 
  position: absolute;
  left: 0;
  right: 0;
  bottom: -2px; 
  height: 2px; 
  background-color: #409eff; 
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto; 
}

.main-layout {
  display: flex;
  flex: 1;
  height: calc(100vh - 70px); 
}

.sidebar {
  background: #409eff; 
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); 
  width: 60px; 
  height: 100%; 
}

.el-menu-vertical {
  border: none;
  background: #409eff; 
  display: flex;
  flex-direction: column; 
  flex: 1; 
  width: 100%; 
  padding: 20px 0; 
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #409eff;
  color: #ffffff; 
  transition: background 0.3s, box-shadow 0.3s; 
  border: none; 
  padding: 15px;
  cursor: pointer; 
  box-shadow: none;
}


.el-menu-item:hover {
  background: #87cefa; 
  box-shadow: none; 
}


.menu-icon {
  width: 30px;
  height: 30px; 
  margin: 5px 0; 
  filter: invert(100%) brightness(100%);
}

.feature-icon {
  width: 40px;
  height: 40px; 
  margin-bottom: 10px; 
  filter: brightness(0.8) contrast(1.2);
}

.bottom-item {
  margin-top: auto; 
  margin-bottom: 20px; 
}

.main-container {

  display: flex;
  flex-direction: column;
  flex: 1;
  background: #f0f0f0; 
}

.header-title {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #9e9e9e;
  height: 100px;
}

.main-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-container {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
  margin-top: -150px; 
}

.feature-card {
  width: 250px;
  text-align: center;
  padding: 20px;
  cursor: pointer;
  border-radius: 30px; 
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-weight: bold; 
  margin-top: -10px; 
}

.feature-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transform: translateY(-5px);
}

.chat-footer {
  position: fixed; 
  bottom: 15%; 
  left: 50%; 
  transform: translateX(-50%); 
  width: 60%;
  padding: 0;
}

.message-input-container {
  width: 100%;
  border-radius: 30px; 
  overflow: hidden; 
  border: 5px solid #409eff; 
}

.message-input :deep(.el-input__inner) {
  border: none !important;
  box-shadow: none !important; 
  outline: none !important; 
  height: 50px; 
  padding: 15px; 
  font-size: 16px; 
  border-radius: 30px; 
  background-color: #fff !important; 
}

.send-button {
  width: 60px; 
  height: 60px; 
  background-color: #409eff !important; 

  cursor: pointer; 
}

.send-button:hover {
  background-color: #1e8fdb !important; 
}

.send-button:focus {
  outline: none; 
  box-shadow: none; 
}

.send-icon {
  width: 30px; 
  height: 30px; 
  filter: invert(100%) brightness(100%); 
}
</style>
