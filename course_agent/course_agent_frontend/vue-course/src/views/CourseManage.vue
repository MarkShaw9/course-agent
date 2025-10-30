<template>
    <el-table :data="courseData" height="77vh" style="min-width: 1200px">
      <el-table-column prop="courseId" label="Course Code" width="110" />
      <el-table-column prop="courseName" label="Course Name" width="180" />
      <el-table-column prop="major" label="Major" width="180" />
      <el-table-column prop="location" label="Location" width="200" />
      <el-table-column prop="description" label="Description" width="180" />
      <el-table-column prop="timetable" label="Timetable" width="180" />
      <el-table-column prop="lecturer" label="Lecturer" width="180" />
  
      <el-table-column label="Operations" width="200" align="center">
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
      title="Edit Course"
      v-model="editDialogVisible"
      width="50%"
    >
      <el-form :model="courseToEdit">
        <el-form-item label="Course Name">
          <el-input v-model="courseToEdit.courseName" />
        </el-form-item>
        <el-form-item label="Major">
          <el-input v-model="courseToEdit.major" />
        </el-form-item>
        <el-form-item label="Location">
          <el-input v-model="courseToEdit.location" />
        </el-form-item>
        <el-form-item label="Timetable">
          <el-input v-model="courseToEdit.timetable" />
        </el-form-item>
        <el-form-item label="Lecturer">
          <el-input v-model="courseToEdit.lecturer" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="doEdit">Save</el-button>
        </span>
      </template>
    </el-dialog>
  
    <el-dialog
      title="Add Course"
      v-model="addDialogVisible"
      width="50%"
    >
      <el-form :model="newCourse">
        <el-form-item label="Course Code">
          <el-input v-model="newCourse.courseId" />
        </el-form-item>
        <el-form-item label="Course Name">
          <el-input v-model="newCourse.courseName" />
        </el-form-item>
        <el-form-item label="Major">
          <el-input v-model="newCourse.major" />
        </el-form-item>
        <el-form-item label="Location">
          <el-input v-model="newCourse.location" />
        </el-form-item>
        <el-form-item label="Timetable">
          <el-input v-model="newCourse.timetable" />
        </el-form-item>
        <el-form-item label="Lecturer">
          <el-input v-model="newCourse.lecturer" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="newCourse.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="doAdd">Add</el-button>
        </span>
      </template>
    </el-dialog>
  
    <el-button type="primary" @click="handleAdd" style="margin-top: 20px;">
      Add Course
    </el-button>
  </template>
  
  <script setup lang="ts">
  import { baseURL, get_all_courses } from '@/api/api';
  import { ref, onMounted } from 'vue';
  import { ElMessageBox, ElMessage } from 'element-plus';
  
  interface Course {
 
    description:string;
    courseId:string;
    courseName: string;
    major: string;
    location: string;
    timetable: string;
    lecturer: string;
    
  }
  
  const courseData = ref<Course[]>([]);
  const Loadings = ref(true);
  const deleteDialogVisible = ref(false);
  const editDialogVisible = ref(false);
  const addDialogVisible = ref(false);
  const courseToEdit = ref<Course | null>(null);
  const newCourse = ref<Course>({

    description:'',
    courseId: '',
    courseName: '',
    major: '',
    location: '',
    timetable: '',
    lecturer: '',
  });
  
  // Function to get course data
  const getData = async () => {
    Loadings.value = true;
    const token = localStorage.getItem('userToken');
    if (!token) {
      ElMessage.error('Authentication token is missing');
      Loadings.value = false;
      return;
    }
    const url = baseURL + get_all_courses;
    try {
      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      const dataList = await response.json();
      courseData.value = dataList;
      console.log("Fetched Data:", courseData.value);
    } catch (err) {
      console.error('Get data failed:', err);
    } finally {
      Loadings.value = false;
    }
  };
  
  const handleEdit = (index: number, row: Course) => {
    if (row) {
      courseToEdit.value = { ...row };
      editDialogVisible.value = true;
    } else {
      ElMessage.error('Unable to edit: course data is missing');
    }
  };
  
  const doEdit = async () => {
    if (!courseToEdit.value) return;
    const token = localStorage.getItem('userToken');
    if (!token) {
      ElMessage.error('Authentication token is missing');
      return;
    }
  
    const url = `http://localhost:8080/api/courses/edit/${courseToEdit.value.courseId}`;
    try {
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(courseToEdit.value)
      });
      if (response.ok) {
        const courseIndex = courseData.value.findIndex(course => course.courseId === courseToEdit.value?.courseId);
        if (courseIndex !== -1) {
          courseData.value[courseIndex] = { ...courseToEdit.value };
        }
        ElMessage.success('Course updated successfully');
      } else {
        ElMessage.error('Failed to update course');
      }
    } catch (err) {
      console.error('Edit failed:', err);
      ElMessage.error('Failed to update course');
    } finally {
      editDialogVisible.value = false;
    }
  };
  
  const handleAdd = () => {
    addDialogVisible.value = true;
  };
  
  const doAdd = async () => {
    if (!newCourse.value) return;
    const token = localStorage.getItem('userToken');
    if (!token) {
      ElMessage.error('Authentication token is missing');
      return;
    }
  
    const url = `http://localhost:8080/api/courses/add`;
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(newCourse.value)
      });
      if (response.ok) {
        const addedCourse = await response.json();
        courseData.value.push(addedCourse);
        ElMessage.success('Course added successfully');
      } else {
        ElMessage.error('Failed to add course');
      }
    } catch (err) {
      console.error('Add failed:', err);
      ElMessage.error('Failed to add course');
    } finally {
      addDialogVisible.value = false;
    }
  };
  
  const handleDelete = async (row: Course) => {
    ElMessageBox.confirm(`Are you sure you want to delete course ${row.courseName}?`, 'Confirm Deletion', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }).then(() => {
      doDelete(row.courseId);
    }).catch(() => {
      ElMessage.info('Deletion cancelled');
    });
  };
  
  const doDelete = async (courseId: string) => {
    const token = localStorage.getItem('userToken');
    if (!token) {
      ElMessage.error('Authentication token is missing');
      return;
    }
  
    const url = `http://localhost:8080/api/courses/delete/${courseId}`;
  
    try {
      const response = await fetch(url, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.ok) {
        courseData.value = courseData.value.filter(course => course.courseId !== courseId);
        ElMessage.success('Course deleted successfully');
      } else {
        ElMessage.error('Failed to delete course');
      }
    } catch (err) {
      console.error('Delete failed:', err);
      ElMessage.error('Failed to delete course');
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
  