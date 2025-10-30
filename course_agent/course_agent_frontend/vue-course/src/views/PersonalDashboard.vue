<template>
    <div>

      <el-container class="">
   
        <el-aside width="280px" class="sidebar" style="height: 224px;">
          <el-menu @select="handleMenuSelect">
            <el-menu-item index="personal_details">Personal Details</el-menu-item>
            <el-menu-item index="my_courses">My Courses</el-menu-item>

            <el-menu-item index="my_comments">My Comments</el-menu-item>
          </el-menu>
        </el-aside>
  
     
        <el-main>
          <router-view /> 
        </el-main>
      </el-container>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      goToLogin() {
        this.$router.push({ name: 'login' }); 
      },
      handleMenuSelect(index) {
        switch (index) {
          case 'personal_details':
            this.$router.push({ name: 'personalDetails' });
            break;
          case 'my_courses':
            this.$router.push({ name: 'MyCourses' }); 
            break;

          case 'my_comments':
            this.$router.push({ name: 'MyComments' }); 
            break;
        }
      }
    }
  };
  </script>
  
  <style>
  @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');
  
  body {
    
    font-family: 'Roboto', sans-serif;
  }
  
 
  .sidebar {
  
    margin-top:10px ;
  }
  
  
  .el-menu-item {
    font-size: 1.2em;
    color: #333;
  }
  
  .el-menu-item:hover {
    background-color: #e6f7ff;
    color: #409eff;
  }
  </style>
  