<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request, { showToast } from '../utils/request';

const router = useRouter();
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  phone: ''
});
const loading = ref(false);

const handleRegister = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    showToast('两次输入的密码不一致', 'error');
    return;
  }
  
  loading.value = true;
  try {
    await request.post('/user/register', {
      studentId: form.value.username,
      password: form.value.password,
      name: form.value.name,
      phone: form.value.phone
    });
    
    showToast('注册成功，请登录', 'success');
    router.push('/login');
  } catch (e) {
    // handled by interceptor
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
         <i class="bi bi-person-plus display-1 mb-4"></i>
         <h1 class="display-4 fw-bold mb-3">加入我们</h1>
         <p class="lead opacity-75">注册成为会员，开启互助之旅。</p>
      </div>
    </div>

    <!-- Right Side - Form -->
    <div class="d-flex flex-column justify-content-center align-items-center w-100 w-lg-50 p-4">
      <div class="w-100" style="max-width: 450px;">
        <div class="text-center mb-5">
           <h2 class="fw-bold mb-2">创建账号</h2>
           <p class="text-muted">填写以下信息完成注册</p>
        </div>

        <form @submit.prevent="handleRegister">
          <div class="row g-3">
             <div class="col-12">
                <label class="form-label fw-bold">学号</label>
                <input v-model="form.username" type="text" class="form-control" placeholder="设置学号作为登录账号" required>
             </div>
             
             <div class="col-md-6">
                <label class="form-label fw-bold">密码</label>
                <input v-model="form.password" type="password" class="form-control" placeholder="设置登录密码" required>
             </div>
             <div class="col-md-6">
                <label class="form-label fw-bold">确认密码</label>
                <input v-model="form.confirmPassword" type="password" class="form-control" placeholder="再次输入密码" required>
             </div>

             <div class="col-12">
                <label class="form-label fw-bold">真实姓名</label>
                <input v-model="form.name" type="text" class="form-control" placeholder="便于物品归还确认" required>
             </div>

             <div class="col-12">
                <label class="form-label fw-bold">手机号码</label>
                <input v-model="form.phone" type="tel" class="form-control" placeholder="便于联系（仅互助时可见）" required>
             </div>
          </div>

          <button type="submit" class="btn btn-primary w-100 py-2 rounded-pill shadow-sm mt-4 mb-4" :disabled="loading">
             {{ loading ? '注册中...' : '立即注册' }}
          </button>
          
          <div class="text-center">
             <span class="text-muted">已有账号？</span>
             <router-link to="/login" class="text-primary text-decoration-none fw-bold">直接登录</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
