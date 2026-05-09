<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request, { showToast } from '../utils/request';

const router = useRouter();
const form = ref({
  account: '',
  name: '',
  phone: '',
  newPassword: '',
  confirmPassword: ''
});
const loading = ref(false);

const handleReset = async () => {
  if (form.value.newPassword !== form.value.confirmPassword) {
    showToast('两次输入的密码不一致', 'error');
    return;
  }
  
  loading.value = true;
  try {
    await request.post('/user/reset-password', {
      account: form.value.account,
      name: form.value.name,
      phone: form.value.phone,
      newPassword: form.value.newPassword
    });
    
    showToast('密码重置成功，请重新登录', 'success');
    router.push('/login');
  } catch (e) {
    // handled by interceptor, usually shows toast with error message
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
         <i class="bi bi-shield-lock display-1 mb-4"></i>
         <h1 class="display-4 fw-bold mb-3">安全中心</h1>
         <p class="lead opacity-75">找回密码，重置安全，继续您的校园互助之旅。</p>
      </div>
    </div>

    <!-- Right Side - Form -->
    <div class="d-flex flex-column justify-content-center align-items-center w-100 w-lg-50 p-4">
      <div class="w-100" style="max-width: 450px;">
        <div class="text-center mb-5">
           <h2 class="fw-bold mb-2">重置密码</h2>
           <p class="text-muted">请验证您的身份信息以设置新密码</p>
        </div>

        <form @submit.prevent="handleReset">
          <div class="row g-3">
             <div class="col-12">
                <label class="form-label fw-bold">账号/学号</label>
                <input v-model="form.account" type="text" class="form-control" placeholder="请输入您的登录账号" required>
             </div>
             
             <div class="col-md-6">
                <label class="form-label fw-bold">真实姓名</label>
                <input v-model="form.name" type="text" class="form-control" placeholder="验证身份" required>
             </div>
             <div class="col-md-6">
                <label class="form-label fw-bold">预留手机号</label>
                <input v-model="form.phone" type="tel" class="form-control" placeholder="验证身份" required>
             </div>

             <div class="col-12">
                <label class="form-label fw-bold">新密码</label>
                <input v-model="form.newPassword" type="password" class="form-control" placeholder="设置新的登录密码" required>
             </div>
             <div class="col-12">
                <label class="form-label fw-bold">确认新密码</label>
                <input v-model="form.confirmPassword" type="password" class="form-control" placeholder="再次输入新密码" required>
             </div>
          </div>

          <button type="submit" class="btn btn-primary w-100 py-2 rounded-pill shadow-sm mt-4 mb-4" :disabled="loading">
             {{ loading ? '提交中...' : '确认重置' }}
          </button>
          
          <div class="text-center">
             <router-link to="/login" class="text-muted text-decoration-none">返回登录</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
