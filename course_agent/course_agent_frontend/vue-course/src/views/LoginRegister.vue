<script setup>

  import LoginForm from '../components/LoginForm.vue';  
  import RegisterForm from '../components/RegisterForm.vue'; 


  import { ref } from 'vue';

  const isLogin = ref(true);




</script>

<template>

  <div class="background">
    <div class="container_lo">
    
 
      <component :is="isLogin ? LoginForm : RegisterForm" />

      <div class="btn_div">
        <span v-if="isLogin">Not a member yet? </span>
        <span v-else>Already have an account</span>
        <button class="btn_b"  v-if="isLogin" @click="isLogin = false" >Go to Register</button>
        <button class="btn_b"  v-else  @click="isLogin = true" >Go to Login</button>

      </div>


    </div>
  </div>

</template>



<style scoped>
  .background{
    width: 100%;
    height: 100vh; 
    background-image:url('../imgs/background2.jpg');
    background-size: cover;
    background-position: center;
    min-width: 1600px;
  }  
  .container_lo{
    display: flex;
    justify-content: center; 
    align-items: center;     
    width: 35%;
    height: 100%;
    background-color: white;
    border-right: 2px black solid;
        
    }
  .btn_div{
    position: fixed; 
    bottom: 20px;     
    left: 20px;       
    color: rgb(0, 0, 0);
    font-size: 12px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    }
  .btn_b{

    border: 0px;
    box-shadow:none;
    color: #2e91ee;
    background: none;
    cursor: pointer;
  }
</style>