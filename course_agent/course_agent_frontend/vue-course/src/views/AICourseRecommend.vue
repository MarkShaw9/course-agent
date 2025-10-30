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
                <el-main class="main-content">
                    <SelectMajorPopup :visible="isMajorDialogVisible" @confirm="handleMajorConfirm" @close="closePopup" />
                    <div v-for="(message, index) in messages" :key="index" class="chat-message">
                        <span class="chat-user">{{ message.user }}:</span>
                        <div class="chat-text">
                            <template v-if="message.courseInfo">
                                <div v-for="(info, idx) in message.courseInfo" :key="idx">
                                    <strong>{{ info.label }}:</strong> {{ info.value }}
                                </div>
                            </template>
                            <template v-else>
                                <div>{{ message.text }}</div>
                            </template>
                        </div>
                    </div>
                </el-main>

                <el-footer class="chat-footer">
                    <div class="message-input-container">
                        <el-input v-model="userMessage" placeholder="Send a message..." class="message-input"
                            @keyup.enter="sendMessage" clearable>
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
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import SelectMajorPopup from '@/components/SelectMajorPopup.vue';

const router = useRouter();

const userMessage = ref('');
const messages = ref([]);
const isMajorDialogVisible = ref(false);
const selectedMajor = ref('');

onMounted(() => {
    setTimeout(() => {
        isMajorDialogVisible.value = true;
    }, 300);
});

const handleMajorConfirm = (major) => {
    selectedMajor.value = major;
    fetchCourseRecommendations();
};

const closePopup = () => {
    isMajorDialogVisible.value = false;
};

const fetchCourseRecommendations = async () => {
    if (!selectedMajor.value) {
        ElMessage({
            message: 'Please select a major before proceeding.',
            type: 'warning',
        });
        return;
    }

    try {
        const token = localStorage.getItem('userToken');
        if (!token) {
            ElMessage({
                message: 'Authorization token is missing!',
                type: 'warning',
            });
            return;
        }

        const response = await axios.get('http://localhost:8080/api/courses/recommendations', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
            params: {
                major: selectedMajor.value,
            },
        });

        const recommendedCourses = response.data;

        if (Array.isArray(recommendedCourses) && recommendedCourses.length > 0) {
            recommendedCourses.forEach((course) => {
                messages.value.push({
                    user: 'AI',
                    courseInfo: [
                        { label: 'Course ID', value: course.courseId },
                        { label: 'Course', value: course.courseName },
                        { label: 'Major', value: course.major },
                        { label: 'Location', value: course.location },
                        { label: 'Description', value: course.description },
                        { label: 'Lecturer', value: course.lecturer },
                    ],
                });
            });
        } else {
            messages.value.push({
                user: 'AI',
                text: 'No course recommendations found for the selected major.',
            });
        }
    } catch (error) {
        messages.value.push({
            user: 'AI',
            text: 'Failed to fetch course recommendations, please try again later.',
        });
    }
};

const sendMessage = async () => {
    if (!userMessage.value.trim()) {
        ElMessage({
            message: 'Message cannot be empty!',
            type: 'warning',
        });
        return;
    }

    const messageToSend = userMessage.value;
    userMessage.value = '';

    messages.value.push({ user: 'You', text: messageToSend });

    try {
        const response = await axios.post('http://localhost:8080/api/chat', {
            message: messageToSend,
        });

        const aiReply = response.data.choices?.[0]?.message?.content;

        if (aiReply) {
            messages.value.push({ user: 'AI', text: aiReply });
        } else {
            messages.value.push({
                user: 'AI',
                text: 'The AI did not return a valid response.',
            });
        }
    } catch (error) {
        messages.value.push({
            user: 'AI',
            text: 'Failed to connect to AI service, please try again later.',
        });
    }
};

const handleExit = () => {
    ElMessage({
        message: 'Exiting to Home...',
        type: 'info',
    });
    router.push('/');
};
</script>

<style scoped>
.chat-text {
    margin-left: 10px;
    white-space: pre-wrap;
}

.ai-chat-home {
    margin-top: 11px;
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
    flex-direction: column;
    padding: 20px;
}

.chat-container {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
    background-color: #f9f9f9;
    border: 1px solid #e0e0e0;
    border-radius: 10px;
    margin-bottom: 20px;
}

.chat-message {
    margin-bottom: 10px;
}

.chat-user {
    font-weight: bold;
    margin-right: 5px;
}

.message-input-container {
    width: 90%;
    border-radius: 30px;
    overflow: hidden;
    border: 5px solid #409eff;
    padding: 10px;
    position: fixed;
    bottom: 6px;
    left: 52%;
    transform: translateX(-50%);
    background: #fff;
    box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
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
}</style>
