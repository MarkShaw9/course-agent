<template>
    <el-container>
        <el-header v-if="!isLoginPage">
            <div class="nav-bar">
                <div class="nav-left">
                    <span class="name">Course Agent</span>
                    <el-menu v-if="isLoggedIn" mode="horizontal" class="el-menu-nav" :default-active="activeIndex" @select="handleSelect">
                        <el-menu-item index="1">Course</el-menu-item>
                        <el-menu-item index="2">AI-chat</el-menu-item>
                        <el-menu-item index="3" v-if="!isAdmin">Personal-Info</el-menu-item>
                        <el-menu-item index="4" v-if="isAdmin">Management</el-menu-item>
                    </el-menu>


                </div>
                <el-button v-if="!isLoggedIn" class="btn" type="primary" size="large" @click="toLogin">Login / Register</el-button>
                <el-button v-else class="btn" type="primary" size="large" @click="logout">{{ userName }}
                    <span v-if="isAdmin" class="admin-circle">A</span>
                    <span v-else class="student-circle">S</span>
                </el-button>
            </div>
        </el-header>
    </el-container>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { ref, onMounted, watch } from 'vue';

import { front_api } from "@/api/api.js"

const isLoggedIn = ref(false);
const isLoginPage = ref(false);
const route = useRoute();
const activeIndex = ref(localStorage.getItem('activeIndex') || '1'); 
const userName = ref('');
const isAdmin = ref(false);

let Mylocation = { value: null };

let ipKey =ref(null);
let isp = ref(null);






const handleSelect = (index) => {
    activeIndex.value = index;
    localStorage.setItem('activeIndex', index); 
    if (index === '1') {
        router.push('/course');
    } else if (index === '2') {
        router.push('/AIchat');
    } else if (index === '3') {
        router.push('/personaldashboard');
    } else if (index === '4') {
        router.push('/management');
    }
};

const checkIfLoginPage = () => {
    isLoginPage.value = route.path === '/login';
};

onMounted(() => {
    const storedUserId = localStorage.getItem('userId');
    const token = localStorage.getItem('userToken');
    const adminStatus = localStorage.getItem('isAdmin');
    const Umails = localStorage.getItem('userEmail');

    if (storedUserId && token) {
        isLoggedIn.value = true;
        userName.value = Umails;
        isAdmin.value = adminStatus === 'true';
    }

    checkLoginStatus();
    checkIfLoginPage();
});

watch(route, () => {
    checkIfLoginPage();
});

const checkLoginStatus = () => {
    const token = localStorage.getItem('userToken');
    isLoggedIn.value = token !== null;
};
const router = useRouter();
const toLogin = () => {
    router.push('/login');
};

const logout = () => {
    localStorage.removeItem('userToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('activeIndex');
    isLoggedIn.value = false;
    router.push('/');
};
</script>

<style>
.nav-bar {
    z-index: 999; 
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;
    background-color: white;
}

.nav-left {
    display: flex;
    align-items: center;
}

.name {
    padding-right: 20px;
    font-size: 24px;
    font-weight: bold;
    color: #409eff;
    border-right: 2px solid rgb(133, 133, 133);
    margin-right: 20px;
}

.el-menu-nav {
    display: flex;
    align-items: center;
    min-width: 800px;
}

.btn {
    font-weight: bold;
}

.admin-circle {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 16px;
    height: 16px;
    background-color: rgb(250, 75, 75);
    border-radius: 50%;
    margin-left: 8px;
    color: white;
    font-size: 10px;
}

.student-circle {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 16px;
    height: 16px;
    background-color: rgb(77, 218, 77);
    border-radius: 50%;
    margin-left: 8px;
    color: white;
    font-size: 10px;
    font-weight: bold;
}
</style>
