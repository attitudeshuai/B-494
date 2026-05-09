<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import request, { showToast } from '../utils/request';

const router = useRouter();
const route = useRoute();
const username = ref('');
const password = ref('');
const loading = ref(false);

const handleLogin = async () => {
  if (!username.value || !password.value) {
    showToast('请输入学号和密码', 'error');
    return;
  }
  
  loading.value = true;
  try {
    const data = await request.post('/user/login', {
      studentId: username.value,
      password: password.value
    });
    
    showToast(`欢迎回来，${data.name}`, 'success');
    // Store user info if needed, but cookie handles session
    const redirect = route.query.redirect || '/';
    router.push(redirect);
  } catch (e) {
    // Error handled by interceptor
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="d-flex min-vh-100 bg-light">
    <!-- Left Side - Hero -->
    <div class="d-none d-lg-flex flex-column justify-content-center align-items-center bg-primary text-white w-50 p-5">
      <div class="text-center">
         <i class="bi bi-search-heart display-1 mb-4"></i>
         <h1 class="display-4 fw-bold mb-3">校园失物招领</h1>
         <p class="lead opacity-75">连接每一份失落与重逢，让校园充满温暖。</p>
      </div>
    </div>

    <!-- Right Side - Form -->
    <div class="d-flex flex-column justify-content-center align-items-center w-100 w-lg-50 p-4">
      <div class="w-100" style="max-width: 400px;">
        <div class="text-center mb-5">
           <h2 class="fw-bold mb-2">账号登录</h2>
           <p class="text-muted">欢迎回来，请登录您的账号</p>
        </div>

        <form @submit.prevent="handleLogin">
          <div class="mb-4">
             <label class="form-label fw-bold">学号</label>
             <div class="input-group">
                <span class="input-group-text bg-white border-end-0"><i class="bi bi-person text-muted"></i></span>
                <input v-model="username" type="text" class="form-control border-start-0 py-2" placeholder="请输入学号" required>
             </div>
          </div>
          
          <div class="mb-4">
             <label class="form-label fw-bold">密码</label>
             <div class="input-group">
                <span class="input-group-text bg-white border-end-0"><i class="bi bi-lock text-muted"></i></span>
                <input v-model="password" type="password" class="form-control border-start-0 py-2" placeholder="请输入密码" required>
             </div>
          </div>

          <button type="submit" class="btn btn-primary w-100 py-2 rounded-pill shadow-sm mb-4" :disabled="loading">
             {{ loading ? '登录中...' : '立即登录' }}
          </button>
          
          <div class="text-center">
             <span class="text-muted">还没有账号？</span>
             <router-link to="/register" class="text-primary text-decoration-none fw-bold">立即注册</router-link>
             <span class="mx-2 text-muted">|</span>
             <router-link to="/reset-password" class="text-muted text-decoration-none">忘记密码？</router-link>
          </div>
          <div class="text-center mt-3">
             <router-link to="/admin" class="text-secondary small text-decoration-none">管理员登录</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
