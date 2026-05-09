<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import request, { showToast } from '../utils/request';

const router = useRouter();
const route = useRoute();
const type = ref(route.query.type || 'lost'); // 'lost' or 'found'

// Watch for route query changes to update type dynamically
import { watch } from 'vue';
watch(() => route.query.type, (newType) => {
  if (newType) {
    type.value = newType;
  }
});

const form = ref({
  name: '',
  place: '',
  time: '',
  description: ''
});
const imageFile = ref(null);
const imagePreview = ref('');
const loading = ref(false);

onMounted(() => {
   // Check for saved form data
   const savedData = localStorage.getItem('publishFormData');
   if (savedData) {
      const parsed = JSON.parse(savedData);
      // Restore basic fields
      form.value = parsed.form;
      type.value = parsed.type;
      // Note: File input cannot be programmatically restored due to security
      if (parsed.hasImage) {
         showToast('请重新上传图片', 'info');
      }
      localStorage.removeItem('publishFormData');
   }
});

const handleFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    imageFile.value = file;
    imagePreview.value = URL.createObjectURL(file);
  }
};

const handleSubmit = async () => {
  if (!form.value.name || !form.value.place || !form.value.time) {
    showToast('请填写必要信息', 'error');
    return;
  }
  
  // Check login status by trying to fetch user info or checking cookie/session
  // Since we rely on server session, we can try a lightweight call or just proceed.
  // If request fails with 401/Not Login, interceptor handles it? 
  // But interceptor just shows toast. We need to catch it and redirect.
  // A better way is to check login status explicitly before submitting heavy data.
  
  try {
      await request.get('/user/info', { silent: true });
  } catch (e) {
      // Not logged in
      showToast('请先登录', 'info');
      
      // Save form data
      const dataToSave = {
         form: form.value,
         type: type.value,
         hasImage: !!imageFile.value
      };
      localStorage.setItem('publishFormData', JSON.stringify(dataToSave));
      
      router.push({ path: '/login', query: { redirect: '/publish?type=' + type.value } });
      return;
  }
  
  loading.value = true;
  try {
    let imagePath = '';
    if (imageFile.value) {
      const formData = new FormData();
      formData.append('file', imageFile.value);
      const res = await request.post('/file/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      imagePath = res; // Assuming res is the path string
    }

    const payload = {
      name: form.value.name,
      place: form.value.place,
      description: form.value.description,
      imagePath: imagePath,
      status: 1 // Default to published
    };
    
    // Different fields for lost vs found time
    if (type.value === 'lost') {
      payload.lostTime = new Date(form.value.time).getTime();
      await request.post('/lost/publish', payload);
    } else {
      payload.foundTime = new Date(form.value.time).getTime();
      await request.post('/found/publish', payload);
    }
    
    showToast('发布成功，等待管理员审核', 'success');
    router.push('/');
  } catch (e) {
    // handled
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="d-flex flex-column min-vh-100 bg-light">
    <Navbar />
    <div class="container py-5 mt-5">
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
            <div class="card-header bg-white border-0 p-4 pb-0">
               <div class="d-flex align-items-center">
                  <div class="icon-box bg-primary bg-opacity-10 text-primary rounded-circle p-3 me-3">
                     <i class="bi bi-pencil-square fs-3"></i>
                  </div>
                  <div>
                     <h4 class="fw-bold mb-1">{{ type === 'lost' ? '发布寻物启事' : '发布招领信息' }}</h4>
                     <p class="text-muted small mb-0">请详细填写物品信息，以便更快找回或归还</p>
                  </div>
               </div>
            </div>
            <div class="card-body p-4">
               <form @submit.prevent="handleSubmit">
                  <div class="row g-3">
                     <div class="col-12">
                        <label class="form-label fw-bold">物品名称 <span class="text-danger">*</span></label>
                        <input v-model="form.name" type="text" class="form-control" placeholder="例如：黑色双肩包、校园卡" required>
                     </div>
                     <div class="col-md-6">
                        <label class="form-label fw-bold">地点 <span class="text-danger">*</span></label>
                        <input v-model="form.place" type="text" class="form-control" placeholder="例如：第二教学楼301" required>
                     </div>
                     <div class="col-md-6">
                        <label class="form-label fw-bold">时间 <span class="text-danger">*</span></label>
                        <input v-model="form.time" type="datetime-local" class="form-control" required>
                     </div>
                     <div class="col-12">
                        <label class="form-label fw-bold">物品描述</label>
                        <textarea v-model="form.description" class="form-control" rows="4" placeholder="请描述物品的特征..."></textarea>
                     </div>
                     <div class="col-12">
                        <label class="form-label fw-bold">上传图片</label>
                        <div class="text-center p-4 border border-2 border-dashed rounded-3 bg-light position-relative">
                           <input type="file" class="form-control d-none" id="imageFile" accept="image/*" @change="handleFileChange">
                           <label for="imageFile" class="cursor-pointer w-100 h-100 d-block">
                              <div v-if="!imagePreview">
                                 <i class="bi bi-cloud-upload display-4 text-muted"></i>
                                 <div class="text-muted mt-2">点击上传图片（可选）</div>
                              </div>
                              <img v-else :src="imagePreview" class="img-fluid rounded shadow-sm" style="max-height: 200px;">
                           </label>
                        </div>
                     </div>
                  </div>
                  <div class="d-grid gap-2 mt-4">
                     <button type="submit" class="btn btn-primary btn-lg rounded-pill shadow-sm" :disabled="loading">
                        {{ loading ? '发布中...' : '立即发布' }}
                     </button>
                     <router-link to="/" class="btn btn-light btn-lg rounded-pill text-muted">取消返回</router-link>
                  </div>
               </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
