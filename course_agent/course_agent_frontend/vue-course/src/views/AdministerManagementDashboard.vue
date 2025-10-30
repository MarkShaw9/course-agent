<template>
  <el-container class="mainbox">
    
    <el-aside width="280px" class="sidebar">
      <el-menu @select="handleMenuSelect" class="menu_el" :default-active="defaultActive">
        <el-menu-item index="users_list">Users List</el-menu-item>
        <el-menu-item index="course_management">Course Management</el-menu-item>

      </el-menu>
    </el-aside>

    
    <el-main>
      <router-view /> 
    </el-main>
  </el-container>
</template>

<script>


export default {
  name: 'AdministerManagementDashboard',
  data() {
    return {
      defaultActive: 'users_list' 
    };
  },
  methods: {
    handleMenuSelect(index) {
      switch (index) {
        case 'users_list':
          this.$router.push({ name: 'usersList' }); 
          break;
        case 'course_management':
          this.$router.push({ name: 'courseManage' }); 
          break;

      }
    }
  },
  mounted() {
  
    this.handleMenuSelect(this.defaultActive);
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

body {
  font-family: 'Roboto', sans-serif;
}


.sidebar {
  margin-top: 16px;
  background: #f5f5f5;
  padding-top: 8px;
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
