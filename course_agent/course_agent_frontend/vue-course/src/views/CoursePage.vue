<template>
  <div class="conmain">
    <div class="search-bar">
      <el-input
          v-model="searchQuery"
          placeholder="Search for courses..."
          clearable
          class="search-input">
        <template #append>
          <el-button @click="clearSearch">Clear</el-button>
        </template>
      </el-input>
      <el-select v-model="selectedMajor" placeholder="Select Major" class="major-select">
        <el-option
            v-for="major in uniqueMajors"
            :key="major"
            :label="major"
            :value="major"
        />
      </el-select>
    </div>
    <div class="course-title">Courses</div>
    <el-main class="course-content">
      <el-row :gutter="20">
        <el-col :span="8" v-for="(course, index) in filteredCourses" :key="course.courseId">
          <el-card
              class="course-card"
              @click="goToCourseDetail(course.courseId, index)"
              shadow="hover"
          >
            <img
                :src="getImageForCourse(index)"
                alt="Course Image"
                class="course-image"
            />
            <div class="course-info">
              <h2 class="course-name">{{ course.courseName }}</h2>
              <p class="course-code">Code: {{ course.courseId }}</p>
              <!-- 【说明】描述两行省略，CSS 在 .course-description 中处理 -->
              <p class="course-description">{{ course.description }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { baseURL } from '../api/api';
import backgroundImage from '@/imgs/background.jpg';

import { createClient } from 'pexels'
import { ElMessage } from 'element-plus';

const client = createClient('CgPGGt6tb2LUqz3nnCu7dRhopPd14HgwwY9c8kZ8VVWPXjqm4IbC9pV6');
const query = 'Nature';

const photos = ref([]);
const router = useRouter();

const searchQuery = ref("");
const selectedMajor = ref("");
const courses = ref([]);
const majors = ref([]);
const errorMessage = ref("");

const searchPhotos = async () =>{
  try{
    const response = await client.photos.search({
      query: query,
      page: 1,
      per_page: 60,
      size:'medium'
    })
    photos.value = response.photos
  }catch (error){
    console.error('get error:', error);
    ElMessage("error");
  }
}

const getImageForCourse = (courseIndex) => {
  if (photos.value.length > 0) {
    return photos.value[courseIndex % photos.value.length]?.src?.small || backgroundImage;
  } else {
    return backgroundImage;
  }
};

const fetchCourses = async () => {
  const token = localStorage.getItem("userToken");
  try {
    const response = await axios.get(`${baseURL}/api/courses`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    courses.value = response.data;
    extractUniqueMajors();
  } catch (error) {
    console.error('Failed to fetch courses:', error);
    errorMessage.value = 'Failed to load courses.';
  }
};

const extractUniqueMajors = () => {
  const majorSet = new Set();
  courses.value.forEach(course => {
    if (course.major) {
      majorSet.add(course.major);
    }
  });
  majors.value = Array.from(majorSet);
};

const goToCourseDetail = (courseId, courseIndex) => {
  const courseImage = getImageForCourse(courseIndex);
  router.push({
    name: "CourseDetailPage",
    state: {
      courseId,
      courseImage
    }
  });
};

onMounted(() => {
  fetchCourses();
  if (photos.value.length === 0) {
    searchPhotos();
  }
});

// Get unique majors for the dropdown
const uniqueMajors = computed(() => {
  return [...new Set(courses.value.map(course => course.major))];
});

// Filter courses based on search query and selected major
const filteredCourses = computed(() => {
  return courses.value.filter(course => {
    const q = searchQuery.value.toLowerCase();
    const matchesQuery =
        course.courseName.toLowerCase().includes(q) ||
        course.description.toLowerCase().includes(q) ||
        course.courseId.toLowerCase().includes(q);
    const matchesMajor = selectedMajor.value ? course.major === selectedMajor.value : true;
    return matchesQuery && matchesMajor;
  });
});

// Clear search input and major selection
const clearSearch = () => {
  searchQuery.value = '';
  selectedMajor.value = '';
};
</script>

<style>
body {
  font-family: 'Roboto', sans-serif;
}

.conmain{
  min-width: 800px;
}

.search-bar {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  gap: 20px;
}

.search-input {
  width: 70%;
}

.major-select {
  width: 20%;
}

.course-title {
  margin-top: 5px;
  font-weight: bold;
  display: block;
  margin-left: 40px;
  font-size: 2em;
  color: #333;
}

.course-content {
  padding: 20px;
}

/* 【新增】让同一行的列等高（关键1：让 el-row 把子项拉伸到同高） */
.course-content .el-row {
  align-items: stretch; /* 使每个 el-col 在同一行被拉伸为相同高度 */
}

/* 【新增】列容器使用弹性布局，方便内部卡片撑满高度（关键2） */
.course-content .el-col {
  display: flex;
}

/* 【修改】卡片改为纵向弹性布局，并且高度100%（关键3：卡片填满列高度） */
.course-card {
  cursor: pointer;
  transition: transform 0.3s;

  /* 等高核心设置 */
  display: flex;           /* 让内部纵向排列 */
  flex-direction: column;
  height: 100%;            /* 卡片高度撑满所在列 */
}

.course-card:hover {
  transform: scale(1.05);
}

.course-image {
  width: 380px !important;
  height: 270px !important;
  object-fit: cover !important;
  border-radius: 10px !important;
  margin-bottom: 15px !important;
}

/* 【新增】信息区也用弹性布局，保证内容分布更稳定（可选） */
.course-info {
  text-align: left;
  display: flex;
  flex-direction: column;
  flex: 1;                 /* 占据卡片剩余空间，帮助等高 */
}

.course-name {
  font-size: 1.5em;
  font-weight: bold ;
  margin-bottom: 10px;
  color: #000000;
}

.course-code {
  font-size: 1.1em;
  color: #555;
  margin-bottom: 10px;
}

/* 【修改】两行省略：描述只展示两行，超出显示“...” */
.course-description {
  font-size: 1em;
  color: #333;

  /* 两行省略核心样式 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;

  /* 更稳健的断词/兼容处理 */
  white-space: normal;
  word-break: break-word;
  line-clamp: 2;
}
</style>
