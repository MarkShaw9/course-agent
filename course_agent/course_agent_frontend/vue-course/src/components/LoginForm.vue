<template>

    
    <div class="login-container">
        <h5>Start to your learning</h5>
        <h2>Login to Course Agent</h2>

        <form @submit.prevent="handleLogin" class="form_lg">

            <div class="inputf">

                <input class="email"
                v-model="email"
                style="width: 300px"
                placeholder="Email"
                autocomplete="off"
                type="text"
                />
                <span class="label">Email</span>
            </div>
           

            <div class="inputf">
                <input class="password"
                v-model="password"
                type="password"
                style="width: 300px"
                placeholder="password"
                autocomplete="off"
                required 
                />
            <span class="label">Password</span>
            </div>
    

            
            <button type="submit">Login</button>

        </form>
    </div>
</template>









<script setup >

import { ElMessage } from 'element-plus'
import { ElNotification } from 'element-plus'
import { baseURL,Login_API } from '@/api/api';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const password = ref('');
const error = ref(false);
const message = ref('');
const email = ref('');

const router = useRouter();



const handleLogin = async () => {

    const url_login = baseURL+Login_API;

    if (!email.value || !password.value) {
            ElNotification({
            title: 'Warning',
            message: 'Please fill in email and password!',
            type: 'warning',
            position:'top-right'
        });
         return; 
    }
    error.value = false;

    try {
        const response = await fetch(url_login, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.value,
            pwd: password.value,  
            
        })
        });

        const data = await response.json();
        
        console.log(data.data);

        

        

        if (response.ok) {

            ElMessage({
                message: 'Login Succeed! ',
                type: 'success',
            });

            message.value = data || "successful";
            error.value = false;



            // clean the input
            email.value = '';
            password.value = '';
          

            localStorage.setItem('userToken', data.data.token);
            localStorage.setItem('userId', data.data.userId);
            localStorage.setItem('isAdmin', data.data.isAdmin);
            localStorage.setItem('userEmail',data.data.uname);
            localStorage.setItem('Uid',data.data.uid);

            setTimeout(() => {
            router.push('/course').then(() => {
                location.reload();
            });}, 500);



        } else {
            error.value = true;
            if (response.status === 401) {
                message.value = data.message || "Invalid username or password!";
            } 
            ElMessage({
                message:'Invalid username or password!',
                type:'error',
                
            })
        }
        }catch (err) {
            message.value = 'An unexpected error occurred!';
            error.value = true;
        }
}

</script>

<style scoped>

    h2{
        display: block;
        margin-bottom: 35px;
        margin-top: 0px;
    }

    h5{
        margin: 0;
    }

    .login-container {
    display: flex;
    flex-direction: column;  
    align-items: flex-start; 
    gap: 10px;               
    }


    .form_lg{
        display: flex;
        flex-direction: column;
      
    }

    .inputf{
        width: 100%;
        position: relative;
        margin-bottom: 35px;
        input {
            width: 100%;
            height: 35px;
            border: none;
            outline: 1.5px solid rgb(133, 133, 133);
            background: transparent;
            border-radius: 8px;
            font-size: 14px;
            padding: 0 10px;
            padding-right: 0;
            color: rgb(32, 32, 32);    
            &::placeholder{
                color: rgb(172, 170, 170);
            }
            
            &:focus{
                
                &::placeholder{
                    opacity: 0;
                }
                & + .label{
                    opacity: 1;
                    top: -25px;
                }
            }
            &:not(:placeholder-shown) +.label{

                    opacity: 1;
                    top: -25px;
                    
                }
        }
        .label{
            position: absolute;
            top: 0;
            left: 0;
            color: #2e91ee;
            font-size: 11px;
            font-weight: bold;
            transition: 0.25s ease-out;
            opacity: 0;

        }
    }

    button{
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

    .inputf input:valid {
        outline: 1.5px solid #2e91ee; 
    }

    .inputf input:invalid {
        outline: 1.5px solid rgb(133, 133, 133); 
    }
    .inputf input:focus {
        outline: 1.5px solid #2e91ee; 
    }

</style>