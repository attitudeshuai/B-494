<script setup>
import { ref, onMounted } from 'vue';
import Navbar from '../components/Navbar.vue';
import request, { showToast } from '../utils/request';
import { Modal } from 'bootstrap';

const activeTab = ref('info');
const user = ref({});
const myLostItems = ref([]);
const myFoundItems = ref([]);
const messages = ref([]);

// For reply
const replyContent = ref('');
const replyTarget = ref(null);

onMounted(() => {
  loadProfile();
});

const loadProfile = async () => {
  try {
    user.value = await request.get('/user/info');
  } catch (e) {
    // redirect?
  }
};

const loadMyItems = async (type) => {
  try {
    if (type === 'lost') {
      myLostItems.value = await request.get('/lost/my');
    } else {
      myFoundItems.value = await request.get('/found/my');
    }
  } catch (e) {}
};

const loadMessages = async () => {
  try {
    const res = await request.get('/message/my');
    messages.value = res.map(msg => ({
      ...msg,
      expanded: false
    }));
  } catch (e) {}
};

const formatDate = (timestamp) => {
  if (!timestamp) return '';
  return new Date(timestamp).toLocaleString();
};

const toggleExpand = (msg) => {
  msg.expanded = !msg.expanded;
};

const handleTabChange = (tab) => {
  activeTab.value = tab;
  if (tab === 'mylost') loadMyItems('lost');
  if (tab === 'myfound') loadMyItems('found');
  if (tab === 'messages') loadMessages();
};

const updateProfile = async () => {
  try {
    await request.post('/user/update', user.value);
    showToast('修改成功', 'success');
  } catch (e) {}
};

const toggleReply = async (msg) => {
  msg.showReply = !msg.showReply;
  if (msg.showReply) {
    msg.replyDraft = '';
    // Fetch chat history
    try {
      const otherId = msg.senderId === user.value.id ? msg.receiverId : msg.senderId;
      console.log('Fetching chat history for:', otherId);
      const res = await request.get(`/message/chat?otherUserId=${otherId}`);
      console.log('Chat history:', res);
      msg.chatHistory = res;
    } catch (e) {
      console.error('Failed to fetch chat history:', e);
      msg.chatHistory = [];
    }
  }
};

const sendInlineReply = async (msg) => {
  if (!msg.replyDraft) return;
  try {
    await request.post('/message/send', {
      receiverId: msg.senderId,
      content: msg.replyDraft
    });
    showToast('回复成功', 'success');
    // Refresh chat history
    const res = await request.get(`/message/chat?otherUserId=${msg.senderId}`);
    // Force update chat history by creating a new array reference
    msg.chatHistory = [...res];
    msg.replyDraft = '';
  } catch (e) {
    console.error('Failed to send reply:', e);
  }
};
</script>

<template>
  <div class="d-flex flex-column min-vh-100 bg-light">
    <Navbar />
    <div class="container mt-5 pt-5">
      <div class="row g-4">
        <div class="col-md-3">
           <div class="card border-0 shadow-sm rounded-4">
              <div class="card-body p-0">
                 <div class="list-group list-group-flush rounded-4">
                    <button class="list-group-item list-group-item-action border-0 py-3" :class="{active: activeTab==='info'}" @click="handleTabChange('info')"><i class="bi bi-person me-2"></i>个人资料</button>
                    <button class="list-group-item list-group-item-action border-0 py-3" :class="{active: activeTab==='mylost'}" @click="handleTabChange('mylost')"><i class="bi bi-search me-2"></i>我的失物</button>
                    <button class="list-group-item list-group-item-action border-0 py-3" :class="{active: activeTab==='myfound'}" @click="handleTabChange('myfound')"><i class="bi bi-heart me-2"></i>我的招领</button>
                    <button class="list-group-item list-group-item-action border-0 py-3" :class="{active: activeTab==='messages'}" @click="handleTabChange('messages')"><i class="bi bi-chat-dots me-2"></i>我的私信</button>
                 </div>
              </div>
           </div>
        </div>
        <div class="col-md-9">
           <div class="card border-0 shadow-sm rounded-4 min-vh-50">
              <div class="card-body p-4">
                 
                 <div v-if="activeTab==='info'">
                    <h4 class="fw-bold text-primary mb-4">修改资料</h4>
                    <form @submit.prevent="updateProfile">
                       <div class="mb-3"><label class="form-label">姓名</label><input v-model="user.name" type="text" class="form-control rounded-pill"></div>
                       <div class="mb-3"><label class="form-label">手机</label><input v-model="user.phone" type="text" class="form-control rounded-pill"></div>
                       <div class="mb-3"><label class="form-label">邮箱</label><input v-model="user.email" type="email" class="form-control rounded-pill"></div>
                       <button type="submit" class="btn btn-primary rounded-pill px-5 mt-3">保存修改</button>
                    </form>
                 </div>

                 <div v-if="activeTab==='mylost'">
                    <h4 class="fw-bold text-primary mb-4">我的失物发布</h4>
                    <ul class="list-group list-group-flush">
                       <li v-for="item in myLostItems" :key="item.id" class="list-group-item px-0">
                          <div class="d-flex justify-content-between align-items-center">
                             <div>
                                <h6 class="fw-bold">{{ item.name }}</h6>
                                <small class="text-muted">{{ item.place }} | {{ new Date(item.createTime).toLocaleDateString() }}</small>
                             </div>
                             <span class="badge" :class="item.status===1?'bg-primary':'bg-secondary'">{{ item.status===1?'发布中':'已结束' }}</span>
                          </div>
                       </li>
                       <li v-if="myLostItems.length===0" class="list-group-item text-muted text-center py-4">暂无发布</li>
                    </ul>
                 </div>

                 <div v-if="activeTab==='myfound'">
                    <h4 class="fw-bold text-primary mb-4">我的招领发布</h4>
                     <ul class="list-group list-group-flush">
                       <li v-for="item in myFoundItems" :key="item.id" class="list-group-item px-0">
                          <div class="d-flex justify-content-between align-items-center">
                             <div>
                                <h6 class="fw-bold">{{ item.name }}</h6>
                                <small class="text-muted">{{ item.place }} | {{ new Date(item.createTime).toLocaleDateString() }}</small>
                             </div>
                             <span class="badge" :class="item.status===1?'bg-primary':'bg-secondary'">{{ item.status===1?'发布中':'已结束' }}</span>
                          </div>
                       </li>
                       <li v-if="myFoundItems.length===0" class="list-group-item text-muted text-center py-4">暂无发布</li>
                    </ul>
                 </div>

                 <div v-if="activeTab==='messages'">
                    <h4 class="fw-bold text-primary mb-4">我的私信</h4>
                    <div v-for="msg in messages" :key="msg.id" class="alert alert-light border mb-3">
                       <div class="d-flex justify-content-between">
                          <strong>{{ msg.senderName || '未知用户' }}</strong>
                          <small class="text-muted">{{ formatDate(msg.sendTime || msg.createTime) }}</small>
                       </div>
                       <div class="mt-2">
                          <p class="mb-0" :class="{'text-truncate-multiline': !msg.expanded}">{{ msg.content }}</p>
                          <button v-if="msg.content && msg.content.length > 100" 
                                  class="btn btn-link btn-sm p-0 text-decoration-none" 
                                  @click="toggleExpand(msg)">
                            {{ msg.expanded ? '收起' : '全文' }}
                          </button>
                       </div>
                       <div class="text-end mt-2">
                          <button class="btn btn-sm btn-outline-primary rounded-pill" @click="toggleReply(msg)">
                            {{ msg.showReply ? '收起对话' : '查看对话/回复' }}
                          </button>
                       </div>
                       
                       <!-- Inline Reply / Chat Box -->
                       <div v-if="msg.showReply" class="mt-3 bg-white p-3 rounded-3 border">
                          <!-- Chat History -->
                          <div class="mb-3 p-2 bg-light rounded" style="max-height: 300px; overflow-y: auto;">
                            <div v-for="h in msg.chatHistory" :key="h.id" class="mb-2 d-flex flex-column" :class="h.senderId === user.id ? 'align-items-end' : 'align-items-start'">
                               <small class="text-muted mb-1" style="font-size: 0.75rem;">
                                 {{ h.senderName || (h.senderId === user.id ? '我' : '对方') }} - {{ formatDate(h.sendTime) }}
                               </small>
                               <div class="p-2 rounded" :class="h.senderId === user.id ? 'bg-primary text-white' : 'bg-white border'">
                                 {{ h.content }}
                               </div>
                            </div>
                            <div v-if="!msg.chatHistory || msg.chatHistory.length === 0" class="text-center text-muted small py-3">
                              暂无历史记录
                            </div>
                          </div>
                          
                          <textarea v-model="msg.replyDraft" class="form-control mb-2" rows="2" placeholder="请输入回复内容..."></textarea>
                          <div class="text-end">
                            <button class="btn btn-sm btn-primary rounded-pill" @click="sendInlineReply(msg)">发送回复</button>
                          </div>
                       </div>

                    </div>
                    <div v-if="messages.length===0" class="text-muted text-center py-4">暂无私信</div>
                 </div>

              </div>
           </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.text-truncate-multiline {
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
