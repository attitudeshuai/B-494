import axios from 'axios';

const service = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true // Important for session cookies
});

// Response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== 200) {
      if (!response.config.silent) {
        showToast(res.msg || 'Error', 'error');
      }
      return Promise.reject(new Error(res.msg || 'Error'));
    }
    return res.data;
  },
  error => {
    console.error('Request Error:', error);
    showToast(error.message || 'Request Failed', 'error');
    return Promise.reject(error);
  }
);

export function showToast(message, type = 'info') {
  let container = document.querySelector('.toast-container-custom');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container-custom';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast-custom ${type}`;
  
  let iconClass = 'bi-info-circle-fill';
  if (type === 'success') iconClass = 'bi-check-circle-fill';
  if (type === 'error') iconClass = 'bi-exclamation-triangle-fill';

  toast.innerHTML = `<i class="bi ${iconClass}"></i><span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.4s ease';
    setTimeout(() => toast.remove(), 400);
  }, 3000);
}

export default service;
