<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import request, { showToast } from '../utils/request';
import { Modal } from 'bootstrap';

const route = useRoute();
const id = route.params.id;
const type = route.query.type || 'lost';

const item = ref({});
const comments = ref([]);
const commentContent = ref('');
const messageContent = ref('');

// Reply state
const activeReplyId = ref(null);
const replyDraft = ref('');

const processedComments = computed(() => {
  const roots = comments.value.filter(c => !c.parentId);
  return roots.map(root => {
    const children = comments.value
      .filter(c => c.parentId === root.id || (c.parentId && findRootId(c.parentId) === root.id))
      .sort((a, b) => a.createTime - b.createTime);
    return { ...root, children };
  });
});

// Helper to find root ID if multiple levels exist (though we enforce flat replies now)
const findRootId = (parentId) => {
  const parent = comments.value.find(c => c.id === parentId);
  if (!parent) return parentId;
  return parent.parentId ? findRootId(parent.parentId) : parent.id;
};

onMounted(() => {
  loadDetail();
  loadComments();
});

const loadDetail = async () => {
  try {
    const res = await request.get(`/${type}/detail`, { params: { id } });
    item.value = res;
  } catch (e) {}
};

const loadComments = async () => {
  try {
    const itemType = type === 'lost' ? 1 : 2;
    const res = await request.get(`/comment/list`, { params: { itemType, itemId: id } });
    comments.value = res || [];
  } catch (e) {}
};

const postComment = async () => {
  if (!commentContent.value) return;
  try {
    await request.post('/comment/add', {
      itemId: id,
      itemType: type === 'lost' ? 1 : 2,
      content: commentContent.value
    });
    showToast('留言成功，等待管理员审核', 'success');
    commentContent.value = '';
    // loadComments(); // Pending comments won't show up immediately
  } catch (e) {}
};

const toggleReply = (comment) => {
  if (activeReplyId.value === comment.id) {
    activeReplyId.value = null;
    replyDraft.value = '';
  } else {
    activeReplyId.value = comment.id;
    replyDraft.value = '';
  }
};

const submitReply = async (targetComment) => {
  if (!replyDraft.value) return;
  try {
    // Force 2-level structure: if target has parentId, use that (sibling reply), else use target.id (child reply)
    const finalParentId = targetComment.parentId || targetComment.id;
    
    await request.post('/comment/add', {
      itemId: id,
      itemType: type === 'lost' ? 1 : 2,
      content: replyDraft.value,
      parentId: finalParentId
    });
    showToast('回复成功，等待管理员审核', 'success');
    replyDraft.value = '';
    activeReplyId.value = null;
  } catch (e) {}
};

const sendMessage = async () => {
  if (!messageContent.value) return;
  try {
    await request.post('/message/send', {
      receiverId: item.value.userId,
      content: messageContent.value
    });
    showToast('发送成功', 'success');
    // close modal
    const modalEl = document.getElementById('messageModal');
    const modalInstance = Modal.getInstance(modalEl);
    if (modalInstance) {
        modalInstance.hide();
    } else {
        // Fallback if instance not found
        new Modal(modalEl).hide();
    }
    
    // Force cleanup backdrop residue
    setTimeout(() => {
        const backdrops = document.querySelectorAll('.modal-backdrop');
        backdrops.forEach(backdrop => backdrop.remove());
        document.body.classList.remove('modal-open');
        document.body.style.overflow = '';
        document.body.style.paddingRight = '';
    }, 300);

  } catch (e) {}
};
</script>

<template>
  <div class="d-flex flex-column min-vh-100 bg-light">
    <Navbar />
    <div class="container mt-5 pt-5">
      <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="row g-0">
           <div class="col-md-5">
              <img :src="item.imagePath || '/src/assets/placeholder.png'" class="img-fluid rounded-start-4 h-100 object-fit-cover" style="min-height: 300px;">
           </div>
           <div class="col-md-7">
              <div class="card-body p-4">
                 <h3 class="card-title fw-bold mb-3">{{ item.name }}</h3>
                 <div class="mb-3">
                    <span class="badge bg-primary me-2">地点</span>
                    <span class="text-muted">{{ item.place }}</span>
                 </div>
                 <div class="mb-3">
                    <span class="badge bg-info text-dark me-2">时间</span>
                    <span class="text-muted">{{ new Date(item.lostTime || item.foundTime).toLocaleString() }}</span>
                 </div>
                 <div class="p-3 bg-light rounded-3 mb-4">
                    <p class="card-text text-secondary mb-0">{{ item.description || '暂无描述' }}</p>
                 </div>
                 
                 <div class="d-flex gap-2">
                    <button class="btn btn-primary rounded-pill px-4" data-bs-toggle="modal" data-bs-target="#messageModal">
                       <i class="bi bi-chat-dots me-2"></i>私信发布者
                    </button>
                    <router-link to="/" class="btn btn-outline-secondary rounded-pill px-4">返回列表</router-link>
                 </div>
              </div>
           </div>
        </div>
      </div>

      <div class="row">
         <div class="col-md-8">
            <div class="card border-0 shadow-sm rounded-4 mb-5">
               <div class="card-body p-4">
                  <h5 class="fw-bold mb-4"><i class="bi bi-chat-square-text me-2"></i>留言区</h5>
                  <div class="mb-4">
                     <textarea v-model="commentContent" class="form-control bg-light border-0 rounded-3 mb-2" rows="3" placeholder="提供线索或询问详情..."></textarea>
                     <div class="text-end">
                        <button class="btn btn-primary rounded-pill px-4" @click="postComment">发表留言</button>
                     </div>
                  </div>
                  <div class="list-group list-group-flush">
                     <div v-for="c in processedComments" :key="c.id" class="list-group-item px-0">
                        <div class="d-flex justify-content-between">
                           <strong>{{ c.userName || '匿名用户' }}</strong>
                           <small class="text-muted">{{ new Date(c.createTime).toLocaleString() }}</small>
                        </div>
                        <p class="mb-1">{{ c.content }}</p>
                        
                        <div class="text-end mb-2">
                           <button class="btn btn-sm btn-link text-decoration-none p-0" @click="toggleReply(c)">回复</button>
                        </div>

                        <!-- Reply Input -->
                        <div v-if="activeReplyId === c.id" class="mb-3 ms-4">
                           <div class="input-group">
                              <input v-model="replyDraft" type="text" class="form-control form-control-sm" placeholder="回复此留言...">
                              <button class="btn btn-primary btn-sm" @click="submitReply(c)">发送</button>
                           </div>
                        </div>

                        <!-- Children -->
                        <div v-if="c.children && c.children.length > 0" class="ms-4 ps-3 border-start">
                           <div v-for="child in c.children" :key="child.id" class="mb-3 bg-light p-2 rounded-3">
                               <div class="d-flex justify-content-between">
                                  <strong>{{ child.userName || '匿名用户' }}</strong>
                                  <small class="text-muted">{{ new Date(child.createTime).toLocaleString() }}</small>
                               </div>
                               <p class="mb-1 text-secondary">{{ child.content }}</p>
                               <div class="text-end">
                                  <button class="btn btn-sm btn-link text-decoration-none p-0" @click="toggleReply(child)">回复</button>
                               </div>
                               <!-- Reply Input Child -->
                               <div v-if="activeReplyId === child.id" class="mt-2">
                                   <div class="input-group">
                                      <input v-model="replyDraft" type="text" class="form-control form-control-sm" placeholder="回复...">
                                      <button class="btn btn-primary btn-sm" @click="submitReply(child)">发送</button>
                                   </div>
                               </div>
                           </div>
                        </div>

                     </div>
                     <div v-if="processedComments.length===0" class="text-center py-4 text-muted">暂无留言</div>
                  </div>
               </div>
            </div>
         </div>
      </div>
    </div>

    <!-- Message Modal -->
    <div class="modal fade" id="messageModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">发送私信</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <textarea v-model="messageContent" class="form-control" rows="3"></textarea>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
            <button type="button" class="btn btn-primary" @click="sendMessage">发送</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
