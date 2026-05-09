<script setup>
import { ref, onMounted } from 'vue';
import request, { showToast } from '../utils/request';

const isLoggedIn = ref(false);
const activeTab = ref('dashboard');
const loading = ref(false);

// Login Form Data
const username = ref('');
const password = ref('');

// Data Lists
const stats = ref({
  userCount: 0,
  lostCount: 0,
  foundCount: 0,
  resolvedCount: 0
});
const users = ref([]);
const lostItems = ref([]);
const foundItems = ref([]);
const comments = ref([]);
const announcements = ref([]);

onMounted(() => {
  // Check if already logged in as admin? 
  // API doesn't have a specific "check admin login" but we can try fetching data
  // If 403/401, show login.
});

const handleLogin = async () => {
  if (!username.value || !password.value) {
    showToast('请输入管理员账号和密码', 'error');
    return;
  }
  loading.value = true;
  try {
    // Fallback logic from previous memory: if db fails, use hardcoded. 
    // But here we just call API.
    await request.post('/admin/login', { account: username.value, password: password.value });
    showToast('管理员登录成功', 'success');
    isLoggedIn.value = true;
    loadData();
  } catch (e) {
    // error
  } finally {
    loading.value = false;
  }
};

const logout = async () => {
  await request.post('/admin/logout');
  isLoggedIn.value = false;
  username.value = '';
  password.value = '';
};

const loadData = async () => {
  try {
    // Parallel requests
    const [u, l, f, c, a] = await Promise.all([
      request.get('/admin/user/list'),
      request.get('/admin/lost/list'),
      request.get('/admin/found/list'),
      request.get('/comment/list'),
      request.get('/announcement/list')
    ]);
    
    users.value = u || [];
    lostItems.value = l || [];
    foundItems.value = f || [];
    comments.value = c || [];
    announcements.value = a || [];
    
    // Calculate stats
    stats.value.userCount = users.value.length;
    stats.value.lostCount = lostItems.value.length;
    stats.value.foundCount = foundItems.value.length;
    stats.value.resolvedCount = [...lostItems.value, ...foundItems.value].filter(i => i.status === 2).length;
    
  } catch (e) {
    console.error("Failed to load admin data", e);
  }
};

const deleteItem = async (type, id) => {
  if(!confirm('确定要删除吗？')) return;
  try {
    if (type === 'announcement') {
        await request.delete(`/announcement/${id}`);
    } else {
        await request.delete(`/admin/${type}/${id}`);
    }
    showToast('删除成功', 'success');
    loadData();
  } catch (e) {}
};

const formatDate = (ts) => new Date(ts).toLocaleString();

// Modal State
const showUserModal = ref(false);
const isEditing = ref(false);
const userForm = ref({
  studentId: '',
  name: '',
  phone: '',
  password: '',
  status: 1
});

const showItemModal = ref(false);
const itemForm = ref({});

const showCommentModal = ref(false);
const commentForm = ref({});

const showAnnouncementModal = ref(false);
const announcementForm = ref({});

// User Actions
const openAddUser = () => {
  userForm.value = { status: 1 };
  isEditing.value = false;
  showUserModal.value = true;
};

const openEditUser = (u) => {
  userForm.value = { ...u, password: '' };
  isEditing.value = true;
  showUserModal.value = true;
};

const saveUser = async () => {
  try {
    if (isEditing.value) {
      if (!userForm.value.password) {
         delete userForm.value.password;
      }
      await request.post('/admin/user/update', userForm.value);
    } else {
      await request.post('/admin/user/add', userForm.value);
    }
    showToast('保存成功', 'success');
    showUserModal.value = false;
    loadData();
  } catch (e) {
    console.error(e);
  }
};

// Item Actions
const openAddItem = (type) => {
  itemForm.value = { type, status: 1 };
  showItemModal.value = true;
};

const openEditItem = (type, item) => {
  itemForm.value = { ...item, type };
  showItemModal.value = true;
};

const saveItem = async () => {
  try {
    const url = itemForm.value.id 
      ? `/admin/${itemForm.value.type}/update`
      : `/admin/${itemForm.value.type}/add`;
    await request.post(url, itemForm.value);
    showToast('保存成功', 'success');
    showItemModal.value = false;
    loadData();
  } catch (e) {
    console.error(e);
  }
};

// Comment Actions
const openEditComment = (c) => {
  commentForm.value = { ...c };
  showCommentModal.value = true;
};

const saveComment = async () => {
  try {
    await request.post('/admin/comment/update', commentForm.value);
    showToast('保存成功', 'success');
    showCommentModal.value = false;
    loadData();
  } catch (e) {
    console.error(e);
  }
};

const approveComment = async (c) => {
    try {
        await request.post('/admin/comment/update', { ...c, status: 1 });
        showToast('审核通过', 'success');
        loadData();
    } catch (e) { console.error(e); }
};

// Announcement Actions
const openAddAnnouncement = () => {
  announcementForm.value = {};
  showAnnouncementModal.value = true;
};

const openEditAnnouncement = (ann) => {
  announcementForm.value = { ...ann };
  showAnnouncementModal.value = true;
};

const saveAnnouncement = async () => {
  try {
    const url = announcementForm.value.id ? '/announcement/update' : '/announcement/add';
    await request.post(url, announcementForm.value);
    showToast('保存成功', 'success');
    showAnnouncementModal.value = false;
    loadData();
  } catch (e) {
    console.error(e);
  }
};

</script>

<template>
  <div class="min-vh-100 bg-light">
    
    <!-- Admin Login -->
    <div v-if="!isLoggedIn" class="d-flex align-items-center justify-content-center min-vh-100 bg-primary bg-gradient">
       <div class="card border-0 shadow-lg rounded-4 overflow-hidden" style="width: 400px;">
          <div class="card-body p-5">
             <div class="text-center mb-4">
                <i class="bi bi-shield-lock-fill text-primary display-4"></i>
                <h4 class="fw-bold mt-3">管理员登录</h4>
             </div>
             <form @submit.prevent="handleLogin">
                <div class="mb-3">
                   <input v-model="username" type="text" class="form-control form-control-lg bg-light border-0" placeholder="账号" required>
                </div>
                <div class="mb-4">
                   <input v-model="password" type="password" class="form-control form-control-lg bg-light border-0" placeholder="密码" required>
                </div>
                <button type="submit" class="btn btn-primary btn-lg w-100 rounded-pill shadow-sm" :disabled="loading">
                   {{ loading ? '登录中...' : '进入后台' }}
                </button>
             </form>
             <div class="text-center mt-3">
                <router-link to="/" class="text-muted small text-decoration-none">返回首页</router-link>
             </div>
          </div>
       </div>
    </div>

    <!-- Dashboard -->
    <div v-else class="d-flex">
       <!-- Sidebar -->
       <div class="admin-sidebar" style="width: 280px; position: fixed; top: 0; bottom: 0; overflow-y: auto;">
          <div class="p-4 mb-4">
             <h4 class="fw-bold mb-0"><i class="bi bi-speedometer2 me-2"></i>管理控制台</h4>
          </div>
          <nav>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'dashboard'}" @click.prevent="activeTab = 'dashboard'"><i class="bi bi-grid me-2"></i>数据概览</a>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'users'}" @click.prevent="activeTab = 'users'"><i class="bi bi-people me-2"></i>用户管理</a>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'lost'}" @click.prevent="activeTab = 'lost'"><i class="bi bi-search me-2"></i>寻物管理</a>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'found'}" @click.prevent="activeTab = 'found'"><i class="bi bi-box-seam me-2"></i>招领管理</a>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'comments'}" @click.prevent="activeTab = 'comments'"><i class="bi bi-chat-dots me-2"></i>留言管理</a>
             <a href="#" class="sidebar-link" :class="{active: activeTab === 'announcement'}" @click.prevent="activeTab = 'announcement'"><i class="bi bi-megaphone me-2"></i>公告管理</a>
             <div class="mt-5 px-4">
                <button class="btn btn-outline-light w-100 rounded-pill" @click="logout"><i class="bi bi-box-arrow-left me-2"></i>退出登录</button>
             </div>
          </nav>
       </div>

       <!-- Main Content -->
       <div class="flex-grow-1 p-4" style="margin-left: 280px;">
          
          <!-- Dashboard Tab -->
          <div v-if="activeTab === 'dashboard'">
             <h3 class="fw-bold mb-4">系统概况</h3>
             <div class="row g-4 mb-4">
                <div class="col-md-3">
                   <div class="card border-0 shadow-sm rounded-4 bg-primary text-white h-100">
                      <div class="card-body p-4">
                         <h6 class="opacity-75">总用户数</h6>
                         <h2 class="fw-bold mb-0">{{ stats.userCount }}</h2>
                      </div>
                   </div>
                </div>
                <div class="col-md-3">
                   <div class="card border-0 shadow-sm rounded-4 bg-info text-white h-100">
                      <div class="card-body p-4">
                         <h6 class="opacity-75">寻物启事</h6>
                         <h2 class="fw-bold mb-0">{{ stats.lostCount }}</h2>
                      </div>
                   </div>
                </div>
                <div class="col-md-3">
                   <div class="card border-0 shadow-sm rounded-4 bg-success text-white h-100">
                      <div class="card-body p-4">
                         <h6 class="opacity-75">失物招领</h6>
                         <h2 class="fw-bold mb-0">{{ stats.foundCount }}</h2>
                      </div>
                   </div>
                </div>
                <div class="col-md-3">
                   <div class="card border-0 shadow-sm rounded-4 bg-warning text-white h-100">
                      <div class="card-body p-4">
                         <h6 class="opacity-75">已解决</h6>
                         <h2 class="fw-bold mb-0">{{ stats.resolvedCount }}</h2>
                      </div>
                   </div>
                </div>
             </div>
          </div>

          <!-- Users Tab -->
          <div v-if="activeTab === 'users'">
             <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold mb-0">用户管理</h3>
                <button class="btn btn-primary rounded-pill px-4" @click="openAddUser"><i class="bi bi-plus-lg me-2"></i>添加用户</button>
             </div>
             <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body">
                   <table class="table table-hover align-middle">
                      <thead><tr><th>ID</th><th>学号</th><th>姓名</th><th>手机</th><th>状态</th><th>操作</th></tr></thead>
                      <tbody>
                         <tr v-for="u in users" :key="u.id">
                            <td>{{ u.id }}</td>
                            <td>{{ u.studentId }}</td>
                            <td>{{ u.name }}</td>
                            <td>{{ u.phone }}</td>
                            <td>
                               <span v-if="u.status===1" class="badge bg-success">正常</span>
                               <span v-else class="badge bg-danger">禁用</span>
                            </td>
                            <td>
                               <button class="btn btn-sm btn-outline-primary rounded-pill me-2" @click="openEditUser(u)">编辑</button>
                               <button class="btn btn-sm btn-outline-danger rounded-pill" @click="deleteItem('user', u.id)">删除</button>
                            </td>
                         </tr>
                      </tbody>
                   </table>
                </div>
             </div>
          </div>

          <!-- Lost Tab -->
          <div v-if="activeTab === 'lost'">
             <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold mb-0">寻物信息管理</h3>
                <button class="btn btn-primary rounded-pill px-4" @click="openAddItem('lost')"><i class="bi bi-plus-lg me-2"></i>添加寻物</button>
             </div>
             <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body">
                   <table class="table table-hover align-middle">
                      <thead><tr><th>图片</th><th>物品</th><th>地点</th><th>状态</th><th>操作</th></tr></thead>
                      <tbody>
                         <tr v-for="item in lostItems" :key="item.id">
                            <td><img :src="item.imagePath || 'https://via.placeholder.com/50'" width="40" height="40" class="rounded object-fit-cover"></td>
                            <td>{{ item.name }}</td>
                            <td>{{ item.place }}</td>
                            <td>
                               <span v-if="item.status===0" class="badge bg-secondary">待审核</span>
                               <span v-else-if="item.status===1" class="badge bg-primary">发布中</span>
                               <span v-else class="badge bg-success">已找到</span>
                            </td>
                            <td>
                               <button class="btn btn-sm btn-outline-primary rounded-pill me-2" @click="openEditItem('lost', item)">编辑</button>
                               <button class="btn btn-sm btn-outline-danger rounded-pill" @click="deleteItem('lost', item.id)">删除</button>
                            </td>
                         </tr>
                      </tbody>
                   </table>
                </div>
             </div>
          </div>

          <!-- Found Tab -->
          <div v-if="activeTab === 'found'">
             <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold mb-0">招领信息管理</h3>
                <button class="btn btn-primary rounded-pill px-4" @click="openAddItem('found')"><i class="bi bi-plus-lg me-2"></i>添加招领</button>
             </div>
             <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body">
                   <table class="table table-hover align-middle">
                      <thead><tr><th>图片</th><th>物品</th><th>地点</th><th>状态</th><th>操作</th></tr></thead>
                      <tbody>
                         <tr v-for="item in foundItems" :key="item.id">
                            <td><img :src="item.imagePath || 'https://via.placeholder.com/50'" width="40" height="40" class="rounded object-fit-cover"></td>
                            <td>{{ item.name }}</td>
                            <td>{{ item.place }}</td>
                            <td>
                               <span v-if="item.status===0" class="badge bg-secondary">待审核</span>
                               <span v-else-if="item.status===1" class="badge bg-primary">发布中</span>
                               <span v-else class="badge bg-success">已归还</span>
                            </td>
                            <td>
                               <button class="btn btn-sm btn-outline-primary rounded-pill me-2" @click="openEditItem('found', item)">编辑</button>
                               <button class="btn btn-sm btn-outline-danger rounded-pill" @click="deleteItem('found', item.id)">删除</button>
                            </td>
                         </tr>
                      </tbody>
                   </table>
                </div>
             </div>
          </div>

          <!-- Comments Tab -->
          <div v-if="activeTab === 'comments'">
             <h3 class="fw-bold mb-4">留言管理</h3>
             <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body">
                   <table class="table table-hover align-middle">
                      <thead><tr><th>内容</th><th>状态</th><th>时间</th><th>操作</th></tr></thead>
                      <tbody>
                         <tr v-for="c in comments" :key="c.id">
                            <td>{{ c.content }}</td>
                            <td>
                                <span v-if="c.status === 1" class="badge bg-success">已发布</span>
                                <span v-else class="badge bg-secondary">待审核</span>
                            </td>
                            <td>{{ formatDate(c.createTime) }}</td>
                            <td>
                               <button v-if="c.status !== 1" class="btn btn-sm btn-outline-success rounded-pill me-2" @click="approveComment(c)">通过</button>
                               <button class="btn btn-sm btn-outline-primary rounded-pill me-2" @click="openEditComment(c)">编辑</button>
                               <button class="btn btn-sm btn-outline-danger rounded-pill" @click="deleteItem('comment', c.id)">删除</button>
                            </td>
                         </tr>
                      </tbody>
                   </table>
                </div>
             </div>
          </div>

          <!-- Announcement Tab -->
          <div v-if="activeTab === 'announcement'">
             <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold mb-0">公告管理</h3>
                <button class="btn btn-primary rounded-pill px-4" @click="openAddAnnouncement"><i class="bi bi-plus-lg me-2"></i>发布公告</button>
             </div>
             <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body">
                   <table class="table table-hover align-middle">
                      <thead><tr><th>标题</th><th>内容</th><th>时间</th><th>操作</th></tr></thead>
                      <tbody>
                         <tr v-for="ann in announcements" :key="ann.id">
                            <td>{{ ann.title }}</td>
                            <td>{{ ann.content }}</td>
                            <td>{{ formatDate(ann.createTime) }}</td>
                            <td>
                               <button class="btn btn-sm btn-outline-primary rounded-pill me-2" @click="openEditAnnouncement(ann)">编辑</button>
                               <button class="btn btn-sm btn-outline-danger rounded-pill" @click="deleteItem('announcement', ann.id)">删除</button>
                            </td>
                         </tr>
                      </tbody>
                   </table>
                </div>
             </div>
          </div>
       </div>
    </div>
    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
       <div class="card border-0 shadow-lg rounded-4" style="width: 400px;">
          <div class="card-body p-4 text-center">
             <div class="mb-3 text-warning">
                <i class="bi bi-exclamation-circle display-1"></i>
             </div>
             <h4 class="fw-bold mb-3">确定要删除吗？</h4>
             <p class="text-muted mb-4">此操作无法撤销，请谨慎操作。</p>
             <div class="d-flex justify-content-center gap-3">
                <button class="btn btn-light rounded-pill px-4" @click="showDeleteModal = false">取消</button>
                <button class="btn btn-danger rounded-pill px-4" @click="confirmDelete">确定删除</button>
             </div>
          </div>
       </div>
    </div>

    <!-- Modals -->
    <!-- User Modal -->
    <div v-if="showUserModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
       <div class="card border-0 shadow-lg rounded-4" style="width: 500px;">
          <div class="card-header bg-white border-0 pt-4 px-4 d-flex justify-content-between align-items-center">
             <h5 class="fw-bold mb-0">{{ isEditing ? '编辑用户' : '添加用户' }}</h5>
             <button type="button" class="btn-close" @click="showUserModal = false"></button>
          </div>
          <div class="card-body p-4">
             <form @submit.prevent="saveUser">
                <div class="mb-3">
                   <label class="form-label">学号</label>
                   <input v-model="userForm.studentId" type="text" class="form-control" required :disabled="isEditing">
                </div>
                <div class="mb-3">
                   <label class="form-label">姓名</label>
                   <input v-model="userForm.name" type="text" class="form-control" required>
                </div>
                <div class="mb-3">
                   <label class="form-label">手机号</label>
                   <input v-model="userForm.phone" type="text" class="form-control">
                </div>
                <div class="mb-3">
                   <label class="form-label">密码</label>
                   <input v-model="userForm.password" type="password" class="form-control" :placeholder="isEditing ? '不修改请留空' : '请输入密码'" :required="!isEditing">
                </div>
                 <div class="mb-3">
                   <label class="form-label">状态</label>
                   <select v-model="userForm.status" class="form-select">
                      <option :value="1">正常</option>
                      <option :value="0">禁用</option>
                   </select>
                </div>
                <div class="d-flex justify-content-end gap-2 mt-4">
                   <button type="button" class="btn btn-light rounded-pill px-4" @click="showUserModal = false">取消</button>
                   <button type="submit" class="btn btn-primary rounded-pill px-4">保存</button>
                </div>
             </form>
          </div>
       </div>
    </div>

    <!-- Item Modal -->
    <div v-if="showItemModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
       <div class="card border-0 shadow-lg rounded-4" style="width: 500px;">
          <div class="card-header bg-white border-0 pt-4 px-4 d-flex justify-content-between align-items-center">
             <h5 class="fw-bold mb-0">{{ itemForm.id ? '编辑' : '添加' }}物品信息</h5>
             <button type="button" class="btn-close" @click="showItemModal = false"></button>
          </div>
          <div class="card-body p-4">
             <form @submit.prevent="saveItem">
                <div class="mb-3">
                   <label class="form-label">物品名称</label>
                   <input v-model="itemForm.name" type="text" class="form-control" required>
                </div>
                <div class="mb-3">
                   <label class="form-label">地点</label>
                   <input v-model="itemForm.place" type="text" class="form-control" required>
                </div>
                <div class="mb-3">
                   <label class="form-label">关联用户ID (Student ID)</label>
                   <input v-model="itemForm.userId" type="number" class="form-control" placeholder="为空则为管理员发布">
                </div>
                <div class="mb-3">
                   <label class="form-label">图片URL</label>
                   <input v-model="itemForm.imagePath" type="text" class="form-control" placeholder="http://...">
                </div>
                <div class="mb-3">
                   <label class="form-label">描述</label>
                   <textarea v-model="itemForm.description" class="form-control" rows="3"></textarea>
                </div>
                <div class="mb-3">
                   <label class="form-label">状态</label>
                   <select v-model="itemForm.status" class="form-select">
                      <option :value="0">待审核</option>
                      <option :value="1">发布中</option>
                      <option :value="2">{{ itemForm.type === 'lost' ? '已找到' : '已归还' }}</option>
                   </select>
                </div>
                <div class="d-flex justify-content-end gap-2 mt-4">
                   <button type="button" class="btn btn-light rounded-pill px-4" @click="showItemModal = false">取消</button>
                   <button type="submit" class="btn btn-primary rounded-pill px-4">保存</button>
                </div>
             </form>
          </div>
       </div>
    </div>

    <!-- Comment Modal -->
    <div v-if="showCommentModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
       <div class="card border-0 shadow-lg rounded-4" style="width: 500px;">
          <div class="card-header bg-white border-0 pt-4 px-4 d-flex justify-content-between align-items-center">
             <h5 class="fw-bold mb-0">编辑留言</h5>
             <button type="button" class="btn-close" @click="showCommentModal = false"></button>
          </div>
          <div class="card-body p-4">
             <form @submit.prevent="saveComment">
                <div class="mb-3">
                   <label class="form-label">内容</label>
                   <textarea v-model="commentForm.content" class="form-control" rows="3" required></textarea>
                </div>
                <div class="d-flex justify-content-end gap-2 mt-4">
                   <button type="button" class="btn btn-light rounded-pill px-4" @click="showCommentModal = false">取消</button>
                   <button type="submit" class="btn btn-primary rounded-pill px-4">保存</button>
                </div>
             </form>
          </div>
       </div>
    </div>

    <!-- Announcement Modal -->
    <div v-if="showAnnouncementModal" class="modal-backdrop-custom d-flex align-items-center justify-content-center">
       <div class="card border-0 shadow-lg rounded-4" style="width: 500px;">
          <div class="card-header bg-white border-0 pt-4 px-4 d-flex justify-content-between align-items-center">
             <h5 class="fw-bold mb-0">{{ announcementForm.id ? '编辑' : '添加' }}公告</h5>
             <button type="button" class="btn-close" @click="showAnnouncementModal = false"></button>
          </div>
          <div class="card-body p-4">
             <form @submit.prevent="saveAnnouncement">
                <div class="mb-3">
                   <label class="form-label">标题</label>
                   <input v-model="announcementForm.title" type="text" class="form-control" required>
                </div>
                <div class="mb-3">
                   <label class="form-label">内容</label>
                   <textarea v-model="announcementForm.content" class="form-control" rows="5" required></textarea>
                </div>
                <div class="d-flex justify-content-end gap-2 mt-4">
                   <button type="button" class="btn btn-light rounded-pill px-4" @click="showAnnouncementModal = false">取消</button>
                   <button type="submit" class="btn btn-primary rounded-pill px-4">保存</button>
                </div>
             </form>
          </div>
       </div>
    </div>

  </div>
</template>

<style scoped>
.modal-backdrop-custom {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1050;
}
</style>
