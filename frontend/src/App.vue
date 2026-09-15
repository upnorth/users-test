<script setup>
import { reactive, ref, onMounted, watch } from 'vue';
import HeaderNav from './components/HeaderNav.vue';
import ActionHeader from './components/ActionHeader.vue';
import SearchBar from './components/SearchBar.vue';
import UserTable from './components/UserTable.vue';
import UserModal from './components/UserModal.vue';
import DeleteModal from './components/DeleteModal.vue';
import ToastNotification from './components/ToastNotification.vue';

import { useUsers } from './composables/useUsers';
import { usePagination } from './composables/usePagination';
import { useHealth } from './composables/useHealth';
import { createUser, updateUser, deleteUser } from './api/userClient';

const { users, loading, searchQuery, filteredUsers, loadUsers, getInitials } = useUsers();
const {
  currentPage,
  pageSize,
  pageSizeOptions,
  totalPages,
  paginatedUsers,
  showingStart,
  showingEnd,
  goToPage,
  prevPage,
  nextPage,
  resetPage
} = usePagination(filteredUsers, 5);

const { healthStatus, checkHealth } = useHealth();

const submitting = ref(false);

const toast = reactive({
  show: false,
  message: '',
  isError: false,
  timer: null
});

const modal = reactive({
  show: false,
  isEdit: false,
  id: null
});

const deleteModal = reactive({
  show: false,
  user: null
});

const form = reactive({
  name: '',
  email: '',
  telephone: '',
  address: ''
});

const formErrors = reactive({
  name: '',
  email: '',
  telephone: '',
  address: '',
  general: ''
});

function showToast(message, isError = false) {
  toast.message = message;
  toast.isError = isError;
  toast.show = true;
  if (toast.timer) clearTimeout(toast.timer);
  toast.timer = setTimeout(() => {
    toast.show = false;
  }, 4000);
}

async function fetchUsers() {
  try {
    await loadUsers();
    await checkHealth();
  } catch (e) {
    showToast(`Failed to fetch users: ${e.message}`, true);
  }
}

watch(searchQuery, () => {
  resetPage();
});

function openCreateModal() {
  modal.isEdit = false;
  modal.id = null;
  form.name = '';
  form.email = '';
  form.telephone = '';
  form.address = '';
  clearErrors();
  modal.show = true;
}

function openEditModal(user) {
  modal.isEdit = true;
  modal.id = user.id;
  form.name = user.name;
  form.email = user.email;
  form.telephone = user.telephone;
  form.address = user.address;
  clearErrors();
  modal.show = true;
}

function closeModal() {
  modal.show = false;
  clearErrors();
}

function clearErrors() {
  formErrors.name = '';
  formErrors.email = '';
  formErrors.telephone = '';
  formErrors.address = '';
  formErrors.general = '';
}

function validateForm() {
  clearErrors();
  let valid = true;

  if (!form.name || form.name.trim().length < 2) {
    formErrors.name = 'Name must be at least 2 characters';
    valid = false;
  }
  if (!form.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    formErrors.email = 'Valid email address is required';
    valid = false;
  }
  if (!form.telephone || form.telephone.trim().length < 6) {
    formErrors.telephone = 'Valid telephone number is required (min 6 characters)';
    valid = false;
  }
  if (!form.address || form.address.trim().length < 3) {
    formErrors.address = 'Address must be at least 3 characters';
    valid = false;
  }

  return valid;
}

async function saveUser() {
  if (!validateForm()) return;
  submitting.value = true;

  const payload = {
    name: form.name.trim(),
    email: form.email.trim(),
    telephone: form.telephone.trim(),
    address: form.address.trim()
  };

  try {
    if (modal.isEdit) {
      await updateUser(modal.id, payload);
      showToast('User updated successfully');
    } else {
      await createUser(payload);
      showToast('User created successfully');
    }
    closeModal();
    await fetchUsers();
  } catch (e) {
    formErrors.general = e.message;
  } finally {
    submitting.value = false;
  }
}

function openDeleteModal(user) {
  deleteModal.user = user;
  deleteModal.show = true;
}

async function confirmDelete() {
  if (!deleteModal.user) return;
  submitting.value = true;
  try {
    await deleteUser(deleteModal.user.id);
    showToast(`User ${deleteModal.user.name} was deleted`);
    deleteModal.show = false;
    await fetchUsers();
  } catch (e) {
    showToast(`Failed to delete user: ${e.message}`, true);
  } finally {
    submitting.value = false;
  }
}

onMounted(() => {
  fetchUsers();
});
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <!-- Top Navigation -->
    <HeaderNav :health-status="healthStatus" />

    <!-- Main Content Area -->
    <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Toast Notification Banner -->
      <ToastNotification :toast="toast" @close="toast.show = false" />

      <!-- Dashboard Stats / Action Header -->
      <ActionHeader
        :loading="loading"
        @refresh="fetchUsers"
        @open-create="openCreateModal"
      />

      <!-- Filter & Search Bar -->
      <SearchBar
        v-model="searchQuery"
        :filtered-count="filteredUsers.length"
        :total-count="users.length"
      />

      <!-- Users Table with Skeleton, Empty State, and Pagination -->
      <UserTable
        :users="users"
        :filtered-users="filteredUsers"
        :paginated-users="paginatedUsers"
        :loading="loading"
        :search-query="searchQuery"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-size-options="pageSizeOptions"
        :total-pages="totalPages"
        :showing-start="showingStart"
        :showing-end="showingEnd"
        :get-initials="getInitials"
        @edit="openEditModal"
        @delete="openDeleteModal"
        @open-create="openCreateModal"
        @update:page-size="pageSize = $event"
        @change-page="goToPage"
        @prev="prevPage"
        @next="nextPage"
      />
    </main>

    <!-- Footer -->
    <footer class="bg-white border-t border-slate-200 mt-auto py-4">
      <div class="max-w-7xl mx-auto px-4 text-center text-xs text-slate-500">
        Built with Java / Quarkus, PostgreSQL Database, OpenAPI & Vue 3 • REST endpoint: <code class="font-mono text-indigo-600">GET /digg/user</code>
      </div>
    </footer>

    <!-- User Create / Edit Modal -->
    <UserModal
      :show="modal.show"
      :is-edit="modal.isEdit"
      :form="form"
      :form-errors="formErrors"
      :submitting="submitting"
      @close="closeModal"
      @save="saveUser"
    />

    <!-- Delete Confirmation Modal -->
    <DeleteModal
      :show="deleteModal.show"
      :user="deleteModal.user"
      :submitting="submitting"
      @close="deleteModal.show = false"
      @confirm="confirmDelete"
    />
  </div>
</template>
