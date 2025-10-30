<template>
  <div class="my-courses">
    <h2 class="title">My Courses</h2>
    <div v-if="courses.length > 0" class="course-list">
      <div v-for="course in courses" :key="course.id" class="course-item">
        <h2 class="course-title">{{ course.course.courseName }}</h2>
        <p><strong>Code:   </strong> {{ course.course.courseId }}</p>
        <p><strong>Major:   </strong> {{ course.course.major }}</p>
        <p><strong>Lecturer:   </strong> {{ course.course.lecturer }}</p>
        <p><strong>Location:   </strong> {{ course.course.location }}</p>
        <p><strong>Timetable:   </strong> {{ course.course.timetable }}</p>
        <el-button type="danger" @click="confirmCancelCourse(course.courseId)">Cancel selected</el-button>
      </div>
    </div>
    <div v-else>
      <p class="no-courses">You didn't select any courses.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { baseURL } from '../api/api';

const courses = ref([]);

const fetchMyCourses = async () => {
  const token = localStorage.getItem('userToken');
  const userId = localStorage.getItem('Uid');

  try {
    const response = await axios.get(`${baseURL}/api/users/${userId}/courses`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    courses.value = response.data;
  } catch (error) {
    console.error('Failed to fetch courses:', error);
    ElMessage.error('Failed to load courses');
  }
};

const confirmCancelCourse = (courseId) => {
  ElMessageBox.confirm(
    'Are you sure you want to cancel this course?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    }
  )
    .then(() => {
      cancelCourse(courseId);
    })
    .catch(() => {
      ElMessage.info('Cancel');
    });
};

const cancelCourse = async (courseId) => {
  const token = localStorage.getItem('userToken');
  const userId = localStorage.getItem('Uid');

  try {
    await axios.delete(`${baseURL}/api/users/${userId}/courses/${courseId}/cancel`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  
    courses.value = courses.value.filter((course) => course.courseId !== courseId);
    ElMessage.success('Course has been successfully cancelled !');
  } catch (error) {
    console.error('Failed to cancel course:', error);
    ElMessage.error('Cancel failed');
  }
};

onMounted(() => {
  fetchMyCourses();
});
</script>

<style scoped>
.my-courses {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.course-list {
  margin-top: 20px;
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.course-item {
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.course-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.course-title {
  margin-left: 5px;
  margin-bottom: 10px;
  font-weight: bold;
  color: #2c3e50;
}

.el-button {
  margin-top: 15px;
  background-color: #e74c3c;
  color: #fff;
  border: none;
}

.el-button:hover {
  background-color: #c0392b;
}

.no-courses {
  text-align: center;
  font-size: 18px;
  color: #888;
}

</style>
