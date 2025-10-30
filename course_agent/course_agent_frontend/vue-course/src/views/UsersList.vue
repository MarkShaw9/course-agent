<template>
  <el-table :data="ListData" height="80vh" style="min-width: 1000px">
    <el-table-column prop="username" label="Username" width="180" />
    <el-table-column prop="email" label="Email" width="180" />
    <el-table-column prop="admin" label="Admin" width="100" />
    <el-table-column prop="pwd" label="Password" width="180" />

    <el-table-column label="Operations" align="center" width="160" >
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
          Edit
        </el-button>
        <el-button
          size="small"
          type="danger"
          @click="handleDelete(scope.row)"
        >
          Delete
        </el-button>
      </template>
    </el-table-column>
  </el-table>


  <el-dialog
  title="Edit User"
  v-model="editDialogVisible"
  width="50%"
>
  <el-form :model="userToEdit">
    <el-form-item label="Username">
      <el-input v-model="userToEdit.username" />
    </el-form-item>
    <el-form-item label="Email">
      <el-input v-model="userToEdit.email" />
    </el-form-item>
    <el-form-item label="Admin">
      <el-switch v-model="userToEdit.admin" />
    </el-form-item>
    <el-form-item label="Password">
      <el-input v-model="userToEdit.pwd" type="password" />
    </el-form-item>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="editDialogVisible = false">Cancel</el-button>
      <el-button type="primary" @click="doEdit">Save</el-button>
    </span>
  </template>
</el-dialog>


  
</template>

<script setup lang="ts">
import { baseURL, get_all_users } from '@/api/api';
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

// Define the User interface
interface User {
  userId: number;
  email: string;
  username: string;
  admin: boolean;
  pwd: string;
}

// Used for storing table data and loading state
const ListData = ref<User[]>([]);
const Loadings = ref(true);
const deleteDialogVisible = ref(false);
const editDialogVisible = ref(false);
const userToEdit = ref<User | null>(null);


// Function to get user data
const getData = async () => {
  Loadings.value = true;
  const url = baseURL + get_all_users;
  try {
    const response = await fetch(url);
    const dataList = await response.json();
    // Assign fetched data to ListData
    ListData.value = dataList;
    console.log("Fetched Data:", ListData.value);
  } catch (err) {
    console.error('Get data failed:', err);
  } finally {
    Loadings.value = false;
  }
};

const handleEdit = (index: number, row: User) => {
  if (row) {
    userToEdit.value = { ...row };
    editDialogVisible.value = true;
  } else {
    ElMessage.error('Unable to edit: user data is missing');
  }
};



const doEdit = async () => {
  if (!userToEdit.value) return;
  const token = localStorage.getItem('userToken');
  if (!token) {
    ElMessage.error('Authentication token is missing');
    return;
  }

  const url = `http://localhost:8080/api/users/edit/${userToEdit.value.userId}`;
  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(userToEdit.value)
    });
    if (response.ok) {
      // Update the user in ListData
      const userIndex = ListData.value.findIndex(user => user.userId === userToEdit.value.userId);
      if (userIndex !== -1) {
        ListData.value[userIndex] = { ...userToEdit.value };
      }
      ElMessage.success('User updated successfully');
    } else {
      ElMessage.error('Failed to update user');
    }
  } catch (err) {
    console.error('Edit failed:', err);
    ElMessage.error('Failed to update user');
  } finally {
    editDialogVisible.value = false;
  }
};


































const handleDelete = async ( row: User) => {

  ElMessageBox.confirm(`Are you sure you want to delete user ${row.username}?`, 'Confirm Deletion', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning',
  }).then(() => {
    console.log("URL", row.userId,row.username);
    doDelete(row.userId);


  }).catch(() => {
    ElMessage.info('Deletion cancelled');
  });
};

const doDelete = async (userId: number) => {
  const token = localStorage.getItem('userToken');
  if (!token) {
    ElMessage.error('Authentication token is missing');
    return;
  }

  const url = `http://localhost:8080/api/users/delete/${userId}`;
  
  console.log("URL",userId);

  try {
    const response = await fetch(url, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
    });
    if (response.ok) {
      // Remove the user from ListData
      ListData.value = ListData.value.filter(user => user.userId !== userId);
      ElMessage.success('User deleted successfully');
    } else {
      ElMessage.error('Failed to delete user');
    }
  } catch (err) {
    console.error('Delete failed:', err);
    ElMessage.error('Failed to delete user');
  } finally {
    deleteDialogVisible.value = false;
  }
};

onMounted(() => {
  getData();
});
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>
