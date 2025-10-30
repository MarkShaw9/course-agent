

<template>
  

  <div class="container">

      <div class="text-container">
        <h1>
          <span class="highlighted-text">Course Agent</span> AI assistant
        </h1>
        <p>AI-assisted course management platform</p>


        <div class="button-container">
          <el-button type="primary" class="btn-primary" @click="toLogin1">Start now</el-button>
          <el-button type="default" class="btn-secondary" @click="toLogin2">AI Chat</el-button>
        </div>
      </div>
    </div>
  <RouterView />
</template>


<script setup >
import { RouterView} from 'vue-router';
import { useRouter } from 'vue-router';
import { ElNotification } from 'element-plus'; 
import NavigationBar from '@/components/Navigationbar.vue'
const router = useRouter();

const toLogin1 = () =>{


  ElNotification({
      title: 'Oops',
      message: 'You are not logged in yet, please log in first!',
      type: 'warning',
      duration: 2000, 
      position: 'top-right',
      onClose: () => {
        router.push('/login');
      }
    });

}

const toLogin2 = () =>{

  ElNotification({
      title: 'Oops',
      message: 'You are not logged in yet, please log in first!',
      type: 'warning',
      duration: 2000, 
      position: 'top-right',
      onClose: () => {
        router.push('/login');
      }
    });

}
</script>

<style>



.container {
  margin-top: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 100px); 
  overflow: hidden;
  background-image: url('../imgs/backg.png'); 
  background-size: cover; 
  background-position: center; 
  background-repeat: no-repeat; 
}

.text-container {
  position: absolute;
  top: 20%;
  right: 45%;
  max-width: 600px;
  padding: 20px;


}

h1 {
  font-size: 2rem;
  font-weight: bold;
  color: #1b1b1b;
  margin-bottom: 1rem;
}

.highlighted-text {
  color: #409eff; 
}

p {
  font-size: 1.2rem;
  color: #58483a;
  margin-bottom: 2rem;
}

.button-container {
  display: flex;
  gap: 40px;
}

.btn-primary {
  background-color: #409eff;
  color: #fff;
  border: none;
  width: 140px;
  font-weight: bold;
}

.btn-secondary {
  font-weight: bold;
  width: 140px;
  background-color: #1b1b1b;
  color: #fff;
  border: none;
}
</style>
