<script setup>
import { ref, onMounted } from 'vue';
import Navbar from '../components/Navbar.vue';
import Footer from '../components/Footer.vue';
import request from '../utils/request';

const lostItems = ref([]);
const foundItems = ref([]);
const announcements = ref([]);

onMounted(() => {
  loadData();
});

const loadData = async () => {
  try {
    const lostRes = await request.get('/lost/list');
    lostItems.value = lostRes || [];
    const foundRes = await request.get('/found/list');
    foundItems.value = foundRes || [];
    const annRes = await request.get('/announcement/list');
    announcements.value = annRes || [];
  } catch (e) {
    console.error(e);
  }
};

const formatDate = (timestamp) => {
  if (!timestamp) return '';
  return new Date(timestamp).toLocaleDateString();
};

import { showToast } from '../utils/request';
const contactAdmin = () => {
   showToast('管理员联系方式：admin@school.edu.cn', 'info');
};
</script>

<template>
  <div class="d-flex flex-column min-vh-100">
    <Navbar />
    
    <!-- Hero Section -->
    <div class="bg-primary bg-gradient text-white py-5 mb-5" style="margin-top: 56px;">
      <div class="container py-4">
        <div class="row align-items-center">
          <div class="col-lg-6">
            <h1 class="display-4 fw-bold mb-3">校园失物招领</h1>
            <p class="lead mb-4 opacity-75">让每一个丢失的物品都能找到回家的路。简单、高效、温暖的校园互助平台。</p>
            <div class="d-flex gap-3">
              <router-link to="/publish?type=lost" class="btn btn-light btn-lg rounded-pill px-4 text-primary fw-bold shadow-sm">
                <i class="bi bi-search me-2"></i>我丢了东西
              </router-link>
              <router-link to="/publish?type=found" class="btn btn-outline-light btn-lg rounded-pill px-4 fw-bold">
                <i class="bi bi-heart me-2"></i>我捡到东西
              </router-link>
            </div>
          </div>
          <div class="col-lg-6 d-none d-lg-block text-end">
            <i class="bi bi-box-seam text-white opacity-25" style="font-size: 15rem;"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="container mb-5">
      <div class="row">
        <!-- Main Content -->
        <div class="col-lg-12">
          
          <!-- Horizontal Info Cards -->
          <div class="row g-4 mb-5">
            <div class="col-md-4">
               <div class="card border-0 shadow-sm rounded-4 h-100">
                  <div class="card-header bg-white border-0 pt-4 pb-0">
                      <h5 class="card-title fw-bold mb-0"><i class="bi bi-megaphone-fill text-primary me-2"></i>最新公告</h5>
                  </div>
                  <div class="card-body">
                      <ul class="list-unstyled small text-muted mb-0">
                          <li v-for="ann in announcements" :key="ann.id" class="mb-3 border-bottom border-light pb-2">
                              <span class="badge bg-danger bg-opacity-10 text-danger mb-1">New</span>
                              <a href="#" class="d-block text-decoration-none text-dark fw-bold">{{ ann.title }}</a>
                              <span class="d-block text-muted" style="font-size: 0.8rem;">{{ ann.content }}</span>
                          </li>
                          <li v-if="announcements.length === 0" class="text-center text-muted py-3">暂无公告</li>
                      </ul>
                  </div>
              </div>
            </div>
            <div class="col-md-4">
               <div class="card border-0 shadow-sm rounded-4 h-100">
                  <div class="card-header bg-white border-0 pt-4 pb-0">
                      <h5 class="card-title fw-bold mb-0"><i class="bi bi-shield-check-fill text-success me-2"></i>防骗指南</h5>
                  </div>
                  <div class="card-body">
                      <p class="small text-muted mb-0">
                          1. 归还物品时请在公共场所进行<br>
                          2. 涉及金钱交易请务必谨慎<br>
                          3. 发现可疑情况请立即向管理员举报
                      </p>
                  </div>
              </div>
            </div>
            <div class="col-md-4">
               <div class="card border-0 shadow-sm rounded-4 h-100">
                  <div class="card-header bg-white border-0 pt-4 pb-0">
                      <h5 class="card-title fw-bold mb-0"><i class="bi bi-question-circle-fill text-primary me-2"></i>需要帮助？</h5>
                  </div>
                  <div class="card-body">
                      <p class="small text-muted mb-4">如果您在使用过程中遇到任何问题，请随时联系管理员。</p>
                      <button class="btn btn-primary rounded-pill btn-sm px-4 w-100" @click="contactAdmin">联系管理员</button>
                  </div>
              </div>
            </div>
          </div>

          <!-- Lost Items -->
          <div class="d-flex align-items-center justify-content-between mb-4">
            <h3 class="fw-bold text-dark border-start border-4 border-primary ps-3 mb-0">
              <i class="bi bi-search me-2 text-primary"></i>最新寻物启事
            </h3>
            <router-link to="/list?type=lost" class="text-decoration-none text-muted small">查看更多 <i class="bi bi-arrow-right"></i></router-link>
          </div>
          
          <div class="row g-4 mb-5">
             <div v-for="item in lostItems.slice(0, 4)" :key="item.id" class="col-md-3">
                <div class="card border-0 shadow-sm h-100 hover-card rounded-4 overflow-hidden">
                   <div class="position-relative" style="height: 200px;">
                      <span class="position-absolute top-0 start-0 m-3 badge bg-danger rounded-pill shadow-sm">急寻</span>
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
                         <span>{{ formatDate(item.lostTime) }}</span>
                      </div>
                   </div>
                   <div class="card-footer bg-white border-0 p-3 pt-0">
                      <router-link :to="'/detail/' + item.id + '?type=lost'" class="btn btn-outline-primary w-100 rounded-pill btn-sm">查看详情</router-link>
                   </div>
                </div>
             </div>
             <div v-if="lostItems.length === 0" class="col-12 text-center py-5">
                <div class="text-muted">暂无寻物信息</div>
             </div>
          </div>

          <!-- Found Items -->
          <div class="d-flex align-items-center justify-content-between mb-4">
            <h3 class="fw-bold text-dark border-start border-4 border-success ps-3 mb-0">
              <i class="bi bi-box-seam me-2 text-success"></i>最新招领信息
            </h3>
            <router-link to="/list?type=found" class="text-decoration-none text-muted small">查看更多 <i class="bi bi-arrow-right"></i></router-link>
          </div>
          
          <div class="row g-4">
             <div v-for="item in foundItems.slice(0, 4)" :key="item.id" class="col-md-3">
                <div class="card border-0 shadow-sm h-100 hover-card rounded-4 overflow-hidden">
                   <div class="position-relative" style="height: 200px;">
                      <span class="position-absolute top-0 start-0 m-3 badge bg-success rounded-pill shadow-sm">招领</span>
                      <img :src="item.imagePath || 'https://via.placeholder.com/300x200'" class="w-100 h-100 object-fit-cover" alt="...">
                   </div>
                   <div class="card-body p-3">
                      <h6 class="card-title fw-bold text-truncate mb-2">{{ item.name }}</h6>
                      <div class="d-flex align-items-center text-muted small mb-2">
                         <i class="bi bi-geo-alt me-1"></i>
                         <span class="text-truncate">{{ item.place }}</span>
                      </div>
                      <div class="d-flex align-items-center text-muted small">
                         <i class="bi bi-clock me-1"></i>
                         <span>{{ formatDate(item.foundTime) }}</span>
                      </div>
                   </div>
                   <div class="card-footer bg-white border-0 p-3 pt-0">
                      <router-link :to="'/detail/' + item.id + '?type=found'" class="btn btn-outline-success w-100 rounded-pill btn-sm">查看详情</router-link>
                   </div>
                </div>
             </div>
             <div v-if="foundItems.length === 0" class="col-12 text-center py-5">
                <div class="text-muted">暂无招领信息</div>
             </div>
          </div>

        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>
