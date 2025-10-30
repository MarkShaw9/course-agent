<template>
  <div>
    <h2>My Comments</h2>

    <div v-if="comments.length > 0">
      <div class="comments-section">
        <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-box"
        >
          <div class="comment-content">
            <p><strong>Course: {{ comment.courseId }}</strong></p>
            <p>{{ comment.content }}</p>
          </div>

          <button
              class="delete-btn"
              :disabled="isDeleting(comment.id)"
              @click="handleDelete(comment)"
              aria-label="Delete this comment"
              title="Delete"
          >
            {{ isDeleting(comment.id) ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>

    <div v-else>
      <p>You haven't posted any comments yet.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

/**
 * 后端服务基础地址
 * 如果后端不是本机 8080，请在这里改
 */
const API_BASE = 'http://localhost:8080';

/**
 * 后端接口路径
 * - GET 我的评论: /api/courses/user/comments
 * - DELETE 指定评论: /api/courses/comments/{commentId}
 */
const ENDPOINTS = {
  GET_MY_COMMENTS: '/api/courses/user/comments',
  DELETE_COMMENT: (id) => `/api/courses/comments/${id}`,
};

/**
 * 鉴权方案
 * - 后端期望的请求头格式是：
 *   Authorization: Bearer <token>
 * - 我们会从 localStorage 里读取登录时存下的 token
 */
const AUTH_SCHEME = 'Bearer';

export default {
  name: 'MyComments',

  data() {
    return {
      comments: [],            // 当前用户的所有评论
      deletingIds: new Set(),  // 正在删除的 comment.id 集合（用于按钮禁用/Loading）
    };
  },

  created() {
    this.fetchComments();
  },

  methods: {
    /**
     * 组装鉴权头
     * 要求 localStorage 里存的是纯 token，例如：
     *   localStorage.setItem('userToken', loginResponse.data.data.token)
     */
    getAuthHeader() {
      const token = localStorage.getItem('userToken');
      return token
          ? { Authorization: `${AUTH_SCHEME} ${token}` }
          : {};
    },

    /**
     * 后端Comment对象 -> 前端展示用对象
     * 主要是把命名固定下来，避免模板里取不到字段
     * 假设后端返回类似：
     * {
     *   "id": 1,
     *   "content": "nb",
     *   "courseId": "COMP5047",
     *   "likes": null,
     *   "userId": 1
     * }
     */
    mapFromServer(raw) {
      return {
        id: raw.id,
        content: raw.content ?? '',
        courseId: raw.courseId ?? '(unknown)',
        likes: raw.likes ?? 0,
        userId: raw.userId ?? raw.user_id ?? null,
      };
    },

    /**
     * 获取“我发过的所有评论”
     * GET /api/courses/user/comments
     */
    async fetchComments() {
      try {
        const resp = await axios.get(
            `${API_BASE}${ENDPOINTS.GET_MY_COMMENTS}`,
            { headers: this.getAuthHeader() }
        );

        // resp.data 预期是一个数组
        const list = Array.isArray(resp.data) ? resp.data : [];
        this.comments = list.map(this.mapFromServer);
      } catch (error) {
        console.error('Error fetching comments:', error);
        alert('Failed to load comments. Please log in again.');
      }
    },

    /**
     * 判断一条评论是否正在删除中
     */
    isDeleting(id) {
      return this.deletingIds.has(id);
    },

    /**
     * 点击“Delete”后：
     * 1. 调后端 DELETE /api/courses/comments/{commentId}
     * 2. 本地把这条评论从 comments 移除
     */
    async handleDelete(comment) {
      if (this.isDeleting(comment.id)) return; // 防止重复点
      this.deletingIds.add(comment.id);

      try {
        await axios.delete(
            `${API_BASE}${ENDPOINTS.DELETE_COMMENT(comment.id)}`,
            { headers: this.getAuthHeader() }
        );

        // 从本地列表移除这条评论
        this.comments = this.comments.filter(
            (c) => c.id !== comment.id
        );

        alert('Comment deleted successfully!');
      } catch (err) {
        console.error('Delete failed:', err);
        alert('Delete failed. Please try again.');
      } finally {
        this.deletingIds.delete(comment.id);
      }
    },
  },
};
</script>

<style scoped>
h2 {
  text-align: center;
  margin-top: 20px;
  font-weight: 600;
  font-size: 20px;
  color: #222;
}

.comments-section {
  margin-top: 20px;
}

.comment-box {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  background: #f5f5f5;
  padding: 12px 16px;
  border-radius: 8px;
}

.comment-content {
  flex-grow: 1;
  min-width: 0;
}

.comment-content p {
  margin: 4px 0;
  word-break: break-word;
}

/* 删除按钮样式 */
.delete-btn {
  margin-left: 12px;
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px solid #ef4444;
  background: transparent;
  color: #ef4444;
  cursor: pointer;
  font-weight: 600;
  white-space: nowrap;
}

.delete-btn:hover {
  background: rgba(239, 68, 68, 0.08);
}

.delete-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
