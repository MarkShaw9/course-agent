import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from '../views/LoginRegister.vue'

import CoursePage from '@/views/CoursePage.vue'
import AIchat from '@/views/AIchat.vue'
import HomePage from '../views/HomePage.vue'
import AIChat2 from '../views/AIChat2.vue' 
import AICourseRecommend from '../views/AICourseRecommend.vue' 
import Management from '@/views/AdministerManagementDashboard.vue'
import UsersList from '@/views/UsersList.vue'

import PersonalDashboard from '@/views/PersonalDashboard.vue'
import PersonalDetails from '@/views/PersonalDetails.vue'
import MyComments from '@/views/MyComments.vue'
import MyCourses from '@/views/MyCourses.vue'
import CourseDetailPage from '@/views/CourseDetailPage.vue'
import CourseManage from '@/views/CourseManage.vue'
import AICourseSuggest from '@/views/AICourseSuggest.vue'
import LoginRecords from '@/views/LoginRecords.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path:'/home',
      name:'homepage',
      component:HomePage
    },
    {
      path:'/course',
      name: 'CoursePageList',
      component: CoursePage
    },

    {
      path:'/coursedetailpage',
      name:'CourseDetailPage',
      component: CourseDetailPage
    },

 

    {
      path:'/AIchat',
      name:'AI',
      component: AIchat
    },
    {
      path: '/AIchat2', 
      name: 'AIChat2',
      component: AIChat2
    },
        {
      path: '/AICourseRecommend', 
      name: 'AICourseRecommend',
      component: AICourseRecommend
    },
    {
      path:'/AIsuggestion',
      name:'AICourseSuggest',
      component:AICourseSuggest
    },
    {
      path: '/management',
      name: 'management',
      component: Management,
      children: [
        {
          path: 'usersList',
          name: 'usersList',
          component: UsersList,
        },
        {
          path:'/loginRd',
          name:'LoginRecords',
          component: LoginRecords
        },
        {
          path: 'courseManage',
          name: 'courseManage',
          component: CourseManage,
        }
        
      ],
    },
    {
      path: '/personalDashboard',
      name: 'personalDashboard',
      component: PersonalDashboard,
      children:[
        {
          path: 'personal',
          name: 'personalDetails',
          component: PersonalDetails,
        },
        {
          path: 'myCourses',
          name: 'MyCourses',
          component: MyCourses,
        },
        {
          path: 'myComments',
          name: 'MyComments',
          component: MyComments,
        },
      ],

 
    },



]})

export default router
