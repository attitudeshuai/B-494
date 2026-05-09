<script setup>
const props = defineProps({
  title: String,
  visible: Boolean
});

const emit = defineEmits(['update:visible']);

const close = () => {
  emit('update:visible', false);
};
</script>

<template>
  <div v-if="visible" class="modal fade show d-block" tabindex="-1" role="dialog" style="background-color: rgba(0,0,0,0.5);">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content rounded-4 border-0 shadow-lg">
        <div class="modal-header border-0">
          <h5 class="modal-title fw-bold text-primary">{{ title }}</h5>
          <button type="button" class="btn-close" @click="close"></button>
        </div>
        <div class="modal-body text-secondary">
          <slot></slot>
        </div>
        <div class="modal-footer border-0">
          <button type="button" class="btn btn-primary rounded-pill px-4" @click="close">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>
