<template>

  
    <div class="reg-container">
        <h2>Welecome to join us!</h2>

        <form @submit.prevent="handleRegister" class="form_re">

            
            <el-input class="inputs"
                v-model="username"
                style="width: 300px"
                placeholder="name"
                
            />
            <el-input class="inputs"
                v-model="password"
                style="width: 300px"
                placeholder="password"
            
            />

            <el-input class="inputs"
                v-model="passwordagn"
                style="width: 300px"
                placeholder="Confirm password"
                
            
            />
            <el-input class="inputs"
                v-model="email"
                style="width: 300px"
                placeholder="email"
        
            />
            <button class="btn_res" type="submit">Register now</button>

        </form>


    </div>

</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus'; 
import { ElNotification } from 'element-plus';

import { baseURL, Register_API } from '@/api/api';

const username = ref('');
const email = ref('');
const password = ref('');
const message = ref('');
const error = ref(false);
const passwordagn = ref('');


const handleRegister = async () => {

    if (!username.value || !password.value || !email.value) {
            ElNotification({
            title: 'Warning',
            message: 'Username or password or email cannot be empty!',
            type: 'warning',
            position:'top-right'
        });
         return; 
    }

    if (password.value!=passwordagn.value) {
            ElNotification({
            title: 'Warning',
            message: 'Please make sure the password you enter twice is the same!',
            type: 'warning',
            position:'top-right'
        });
         return; 
    }

    const url_register = baseURL+Register_API;

    try {
        const response = await fetch(url_register, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username.value,
            pwd: password.value,  
            email: email.value
        })
        });

        const data = await response.json();
        
        console.log(data);
        

        if (response.ok) {

            ElMessage({
                message: 'Register Succeed! ',
                type: 'success',
            });

            message.value = data || "successful";
            error.value = false;


            // clean the input
            username.value = '';
            password.value = '';
            email.value = '';
            passwordagn.value = '';



        } else {
            error.value = true;
            if (response.status === 409) {
                message.value = data.message || "Email already exists!";
            } 
            ElMessage({
                message:message.value,
                type:'error',
                
            })
        }
        }catch (err) {
        message.value = 'An unexpected error occurred!';
        error.value = true;
        }
    };
</script>

<style scoped>

    .reg-container{
        display: flex;
        flex-direction: column;  
        align-items: flex-start; 
        gap: 10px;               
    }

    .form_re{
        display: flex;
        flex-direction: column;
        gap: 40px; 
    }

    .inputs{
        height: 35px;
    }


    .btn_res{
        width: 101%;
        height: 35px;
        background: #2e91ee;
        color: white;
        border:none;
        outline: none;
        border-radius: 5px;
        font-weight: bold;
        font-size: 16px;
        cursor: pointer;
    }
 

</style>