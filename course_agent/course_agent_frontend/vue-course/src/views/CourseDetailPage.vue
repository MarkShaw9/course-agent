<template>
  <div>
    <div class="course-detail" v-if="course">
      <div class="course-header-title">
        <h2 class="course-name">{{ course.courseName }}</h2>
        <p class="course-code">Code: {{ course.courseId }}</p>
      </div>

      <hr class="divider" />

      <div class="course-header">
        <div class="course-image-wrapper">
          <img :src="course.image ? course.image : backgroundImage" alt="Course Image" class="course-image" />
        </div>
        <div class="course-header-info">
          <p class="course-text"><strong>Lecturer:</strong> <em>{{ course.lecturer }}</em></p>
          <p class="course-text"><strong>Location:</strong> <em>{{ course.location }}</em></p>
          <p class="course-text"><strong>Timetable:</strong> <em>{{ course.timetable }}</em></p>
          <div class="button-group">
            <el-button class="select-btn" plain @click="dialogVisible = true">Select</el-button>

            <el-button class="analysis-btn" @click="handleAIAnalysis">AI Analysis</el-button>

            <el-dialog v-model="aiDialogVisible" title="AI Analysis Result" width="600">
              <p class="ai-result">{{ aiAnalysisResult }}</p>
              <template #footer>
                <el-button @click="aiDialogVisible = false">Close</el-button>
              </template>
            </el-dialog>

            <el-button class="comments-btn" @click="openCommentDialog">Comment</el-button>

            <el-dialog v-model="isCommentDialogVisible" title="Add Comment">
              <el-input type="textarea" v-model="commentContent" placeholder="Enter your comment here..." />
              <div slot="footer" class="dialog-footer_com">
                <el-button @click="isCommentDialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="handleComments">Submit</el-button>
              </div>
            </el-dialog>

            <el-dialog v-model="dialogVisible" title="Tips" width="500">
              <span>Are you sure to choose this course?</span>
              <template #footer>
                <div class="dialog-footer">
                  <el-button @click="dialogVisible = false">Cancel</el-button>
                  <el-button type="primary" @click="handleselectcourse">Confirm</el-button>
                </div>
              </template>
            </el-dialog>
          </div>
        </div>
      </div>

      <h2>Description</h2>
      <hr class="divider" />
      <p class="description-text"><em>{{ course.description }}</em></p>

      <div class="comments-section">
        <h3>Comments</h3>
        <div v-for="comment in comments" :key="comment.id" class="comment-box">
          <div class="comment-content">
            <p><strong> Anonymous</strong></p>
            <p>{{ comment.content }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';  
import { baseURL } from '../api/api';
import backgroundImage from '@/imgs/background.jpg';

const aiAnalysisResult = ref(""); 

const dialogVisible = ref(false)

const aiDialogVisible = ref(false);
const comments = ref([]);

const course = ref(null);
const errorMessage = ref("");

const commentContent = ref('');
const isCommentDialogVisible = ref(false);

const fetchCourse = async (courseId) => {
  try {
    const token = localStorage.getItem('userToken');
    console.log('JWT Token:', token);

    if (!courseId) {
      throw new Error('Course ID is undefined.');
    }

    const response = await axios.get(`${baseURL}/api/courses/${courseId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    console.log('Course data:', response.data);
    
    course.value = {
      ...response.data,
      image: course.value.image || backgroundImage
    };
  } catch (error) {
    console.error('Failed to fetch course:', error);
    errorMessage.value = 'Failed to load course.';
  }
};

const fetchComments = async (courseId) => {
  try {
    const token = localStorage.getItem('userToken');
    const response = await axios.get(`${baseURL}/api/courses/${courseId}/comments`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    console.log('comments data:', response.data); 
    comments.value = response.data;

  } catch (error) {
    console.error('Failed to fetch comments:', error);
    errorMessage.value = 'Failed to load comments.';
  }
};

function openCommentDialog() {
  isCommentDialogVisible.value = true;
}

const handleComments = async () => {
  const courseId = course.value.courseId; 
  const token = localStorage.getItem('userToken');

  try {
    const response = await fetch(`${baseURL}/api/courses/${courseId}/add-comments`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        content: commentContent.value
      })
    });

    if (!response.ok) {
      const errorData = await response.json(); 
      throw new Error(errorData.message || 'Network response was not ok');
    }

    const responseData = await response.json();
    console.log('Comment added:', responseData);

    ElMessage.success("Comment added successfully!");
    isCommentDialogVisible.value = false;

    fetchComments(courseId);

  } catch (error) {
    console.error('Error adding comment:', error);
    ElMessage.error(error.message || "An error occurred while adding the comment.");
  }
};

const handleselectcourse = async () => {
  const userId = localStorage.getItem("Uid"); 
  const courseId = course.value.courseId; 
  const token = localStorage.getItem('userToken');

  console.log('teeestst',userId+courseId+token);

  try {
    const url = `${baseURL}/api/courses/${courseId}/select`;

    const response = await axios.post(url, null, {
      params: { userId }, 
      headers: {
        Authorization: `Bearer ${token}`, 
        'Content-Type': 'application/json' 
      }
    });

    console.log(response.data); 
    dialogVisible.value = false; 
    ElMessage.success("Course selected successfully!");

  } catch (error) {
    if (error.response && error.response.status === 409) {
        ElMessage.error("User already selected this course");
    } else {
        console.error("Error selecting course:", error);
        ElMessage.error("An error occurred while selecting the course.");
    }
  }
};

const handleAIAnalysis = async () => {
  const courseId = course.value.courseId; 
  const token = localStorage.getItem('userToken');

  try {
    const url = `${baseURL}/api/courses/${courseId}/analyze`;

    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    console.log('AI Analysis Data:', response.data);
    aiAnalysisResult.value = response.data.data; 
    aiDialogVisible.value = true;

    ElMessage.success("AI analysis completed!");

  } catch (error) {
    console.error("Error performing AI analysis:", error);
    ElMessage.error("An error occurred during AI analysis.");
  }
};

onMounted(() => {
  const { courseId, courseImage } = history.state;

  course.value = {
    ...course.value,
    image: courseImage || backgroundImage
  };

  console.log("?????",courseImage);

  fetchCourse(courseId);
  fetchComments(courseId);
});
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

body {
  font-family: 'Roboto', sans-serif;
}

.ai-result{
  white-space: pre-wrap;
}

.divider {
  border: none;
  border-top: 2px solid #e5e5e5;
  margin: 20px 0;
}

.course-detail {
  min-width: 1000px;
  padding: 20px;
}

.course-header-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.course-name {
  font-size: 2em;
  color: #333;
}

.course-code {
  font-size: 1.2em;
  color: #666;
}

.course-header {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 20px;
}

.course-image-wrapper {
  flex: 1;
  max-width: 400px;
}

.course-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
  object-fit: cover;
}

.course-header-info {
  flex: 2;
  max-width: 600px;
}

.course-text {
  font-size: 1.4em;
  line-height: 1.6;
  margin-bottom: 15px;
  color: #333;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.select-btn, .analysis-btn, .comments-btn {
  background-color: #409eff;
  color: #fff;
  border: none;
  padding: 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1.2em;
}

.analysis-btn {
  background-color: orange;
}

.comments-btn{
  background-color: rgb(18, 170, 43);
  margin-left: 15px;
}

.description-text {
  font-size: 1.4em;
  line-height: 1.6;
  color: #333;
  font-style: normal;
}

.comments-section {
  margin-top: 20px;
}

.comment-box {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background: #f5f5f5;
  padding: 10px;
  border-radius: 8px;
}

.comment-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}

.comment-content {
  flex-grow: 1;
}

.comment-interaction {
  margin-top: 10px;
}

.dialog-footer_com{
  margin-top: 10px;
}
</style>
