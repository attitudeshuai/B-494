<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import request, { showToast } from '../utils/request';

const router = useRouter();
const user = ref(null);
const userMenuOpen = ref(false);
const publishMenuOpen = ref(false);

const closeMenu = () => {
  userMenuOpen.value = false;
  publishMenuOpen.value = false;
};

onMounted(() => {
  checkLogin();
  window.addEventListener('click', closeMenu);
});

onUnmounted(() => {
  window.removeEventListener('click', closeMenu);
});

const toggleUserMenu = (event) => {
  event.stopPropagation();
  userMenuOpen.value = !userMenuOpen.value;
  publishMenuOpen.value = false; // close other menu
};

const togglePublishMenu = (event) => {
  event.stopPropagation();
  publishMenuOpen.value = !publishMenuOpen.value;
  userMenuOpen.value = false; // close other menu
};

const checkLogin = async () => {
  try {
    user.value = await request.get('/user/info', { silent: true });
  } catch (e) {
    user.value = null;
  }
};

const logout = async () => {
  try {
    await request.post('/user/logout');
    showToast('已安全退出', 'success');
    user.value = null;
    router.push('/login');
  } catch (e) {
    // ignore
  }
};
</script>

<template>
  <nav class="navbar navbar-expand-lg navbar-light fixed-top bg-white shadow-sm">
    <div class="container">
      <router-link class="navbar-brand" to="/">
        <i class="bi bi-search-heart text-primary"></i> 校园失物招领
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto ms-4">
          <li class="nav-item"><router-link class="nav-link" to="/">首页</router-link></li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="javascript:void(0)" role="button" @click.stop="togglePublishMenu">发布信息</a>
            <ul class="dropdown-menu border-0 shadow-sm rounded-4 mt-2" :class="{ show: publishMenuOpen }" style="display: block;" v-show="publishMenuOpen" @click.stop>
              <li><router-link class="dropdown-item py-2" to="/publish?type=lost" @click="publishMenuOpen = false"><i class="bi bi-megaphone me-2"></i>我丢了东西</router-link></li>
              <li><router-link class="dropdown-item py-2" to="/publish?type=found" @click="publishMenuOpen = false"><i class="bi bi-heart me-2"></i>我捡到东西</router-link></li>
            </ul>
          </li>
        </ul>
        <div class="d-flex">
          <div v-if="user" class="dropdown">
            <button class="btn btn-outline-primary dropdown-toggle rounded-pill px-3" type="button" @click="toggleUserMenu">
              {{ user.name }}
            </button>
            <ul class="dropdown-menu dropdown-menu-end border-0 shadow mt-2 rounded-4" :class="{ show: userMenuOpen }" @click.stop>
              <li><router-link class="dropdown-item py-2" to="/user" @click="userMenuOpen = false"><i class="bi bi-person me-2"></i>个人中心</router-link></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item py-2 text-danger" href="#" @click.prevent="logout"><i class="bi bi-box-arrow-right me-2"></i>退出登录</a></li>
            </ul>
          </div>
          <div v-else>
            <router-link to="/login" class="btn btn-outline-primary me-2 px-4">登录</router-link>
            <router-link to="/register" class="btn btn-primary px-4">注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>
