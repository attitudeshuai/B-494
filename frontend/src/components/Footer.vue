<script setup>
import { ref } from 'vue';
import BaseModal from './BaseModal.vue';

const modalVisible = ref(false);
const modalTitle = ref('');
const modalContent = ref('');

const contents = {
  intro: '本平台致力于为全校师生提供一个高效、便捷、温暖的失物招领服务。\n\n我们希望通过互联网技术，让每一次遗失都能重逢，让爱心在校园传递。平台支持失物挂失、拾物招领、在线留言及私信沟通等功能。',
  terms: '1. 用户需对自己发布的信息真实性负责。\n2. 禁止发布虚假、诈骗、违法或与失物招领无关的信息。\n3. 平台仅提供信息发布与沟通渠道，不承担线下交易或物品保管的风险与责任。\n4. 恶意骚扰他人将被禁用账号。',
  guide: '1. 注册并登录账号。\n2. 点击顶部导航栏“发布信息”，选择“我丢了东西”或“我捡到东西”。\n3. 填写物品详细特征、时间、地点，并上传清晰图片。\n4. 提交后等待管理员审核（通常在24小时内）。\n5. 审核通过后信息将公开显示。',
  contact: '如有任何问题、建议或举报，请联系我们：\n\n邮箱：service@school-lostfound.com\n电话：021-12345678\n办公地点：学生活动中心 101 室\n工作时间：周一至周五 9:00 - 17:00'
};

const openModal = (type) => {
  const titles = {
    intro: '平台简介',
    terms: '使用条款',
    guide: '发布指南',
    contact: '联系客服'
  };
  modalTitle.value = titles[type];
  modalContent.value = contents[type];
  modalVisible.value = true;
};
</script>

<template>
  <footer class="bg-white py-5 mt-auto border-top">
    <div class="container">
      <div class="row g-4">
        <div class="col-md-4">
          <h5 class="fw-bold mb-3"><i class="bi bi-search-heart text-primary me-2"></i>校园失物招领</h5>
          <p class="text-muted small">
            致力于为全校师生提供一个高效、便捷、温暖的失物招领平台。让每一次遗失都能重逢，让爱心在校园传递。
          </p>
        </div>
        <div class="col-md-2">
          <h6 class="fw-bold mb-3">关于我们</h6>
          <ul class="list-unstyled small text-muted">
            <li class="mb-2"><a href="#" class="text-decoration-none text-muted" @click.prevent="openModal('intro')">平台简介</a></li>
            <li class="mb-2"><a href="#" class="text-decoration-none text-muted" @click.prevent="openModal('terms')">使用条款</a></li>
          </ul>
        </div>
        <div class="col-md-2">
          <h6 class="fw-bold mb-3">帮助中心</h6>
          <ul class="list-unstyled small text-muted">
            <li class="mb-2"><a href="#" class="text-decoration-none text-muted" @click.prevent="openModal('guide')">发布指南</a></li>
            <li class="mb-2"><a href="#" class="text-decoration-none text-muted" @click.prevent="openModal('contact')">联系客服</a></li>
          </ul>
        </div>
        <div class="col-md-4">
          <div class="d-flex align-items-center justify-content-between">
             <p class="mb-0 text-muted small">&copy; 2026 校园失物招领系统. All rights reserved.</p>
             <div>
                <router-link to="/admin" class="text-decoration-none text-muted small me-2">管理员登录</router-link>
             </div>
          </div>
        </div>
      </div>
    </div>
    
    <BaseModal 
      :visible="modalVisible" 
      :title="modalTitle" 
      @update:visible="modalVisible = $event"
    >
      <div style="white-space: pre-wrap;">{{ modalContent }}</div>
    </BaseModal>
  </footer>
</template>
