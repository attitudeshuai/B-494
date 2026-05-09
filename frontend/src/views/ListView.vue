<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import request from '../utils/request';

const route = useRoute();
const router = useRouter();
const type = ref(route.query.type || 'lost');
const items = ref([]);
const loading = ref(false);
const keyword = ref('');

const loadData = async () => {
  loading.value = true;
  try {
    const endpoint = type.value === 'lost' ? '/lost/list' : '/found/list';
    // The public list endpoint already defaults to status=1 (approved)
    const res = await request.get(endpoint, {
       params: { keyword: keyword.value }
    });
    items.value = res || [];
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadData();
});

watch(() => route.query.type, (newType) => {
  if (newType) {
    type.value = newType;
    loadData();
  }
});

const handleSearch = () => {
   loadData();
};

const formatDate = (timestamp) => {
  if (!timestamp) return '';
  return new Date(timestamp).toLocaleDateString();
};
</script>

<template>
  <div class="d-flex flex-column min-vh-100 bg-light">
    <Navbar />
    
    <div class="container py-5 mt-5">
       <div class="d-flex justify-content-between align-items-center mb-4">
          <div>
             <h2 class="fw-bold mb-1">{{ type === 'lost' ? '寻物启事' : '失物招领' }}</h2>
             <p class="text-muted mb-0">
                {{ type === 'lost' ? '希望能帮您找回心爱之物' : '感谢您的拾金不昧' }}
             </p>
          </div>
          <div class="d-flex gap-2">
             <div class="input-group">
                <input v-model="keyword" type="text" class="form-control" placeholder="搜索物品名称/地点..." @keyup.enter="handleSearch">
                <button class="btn btn-primary" @click="handleSearch"><i class="bi bi-search"></i></button>
             </div>
             <div class="btn-group">
                <button class="btn btn-outline-primary" :class="{active: type === 'lost'}" @click="router.push('/list?type=lost')">寻物</button>
                <button class="btn btn-outline-success" :class="{active: type === 'found'}" @click="router.push('/list?type=found')">招领</button>
             </div>
          </div>
       </div>

       <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
             <span class="visually-hidden">Loading...</span>
          </div>
       </div>

       <div v-else class="row g-4">
          <div v-for="item in items" :key="item.id" class="col-md-3">
             <div class="card border-0 shadow-sm h-100 hover-card rounded-4 overflow-hidden">
                <div class="position-relative" style="height: 200px;">
                   <span class="position-absolute top-0 start-0 m-3 badge rounded-pill shadow-sm" :class="type==='lost'?'bg-danger':'bg-success'">
                      {{ type === 'lost' ? '急寻' : '招领' }}
                   </span>
                   <img :src="item.imagePath || '/src/assets/placeholder.png'" class="w-100 h-100 object-fit-cover" alt="...">
                </div>
                <div class="card-body p-3">
                   <h6 class="card-title fw-bold text-truncate mb-2">{{ item.name }}</h6>
                   <div class="d-flex align-items-center text-muted small mb-2">
                      <i class="bi bi-geo-alt me-1"></i>
                      <span class="text-truncate">{{ item.place }}</span>
                   </div>
                   <div class="d-flex align-items-center text-muted small">
                      <i class="bi bi-clock me-1"></i>
                      <span>{{ formatDate(type === 'lost' ? item.lostTime : item.foundTime) }}</span>
                   </div>
                </div>
                <div class="card-footer bg-white border-0 p-3 pt-0">
                   <router-link :to="'/detail/' + item.id + '?type=' + type" class="btn w-100 rounded-pill btn-sm" :class="type==='lost'?'btn-outline-primary':'btn-outline-success'">查看详情</router-link>
                </div>
             </div>
          </div>
          
          <div v-if="items.length === 0" class="col-12 text-center py-5">
             <div class="py-5">
                <i class="bi bi-inbox display-1 text-muted opacity-25"></i>
                <p class="text-muted mt-3">暂无相关信息</p>
             </div>
          </div>
       </div>
    </div>
  </div>
</template>

<style scoped>
.hover-card {
  transition: transform 0.2s;
}
.hover-card:hover {
  transform: translateY(-5px);
}
</style>
