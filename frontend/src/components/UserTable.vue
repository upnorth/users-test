<script setup>
import PaginationControls from './PaginationControls.vue';

defineProps({
  users: {
    type: Array,
    required: true
  },
  filteredUsers: {
    type: Array,
    required: true
  },
  paginatedUsers: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  searchQuery: {
    type: String,
    default: ''
  },
  currentPage: {
    type: Number,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  },
  pageSizeOptions: {
    type: Array,
    default: () => [5, 10, 20]
  },
  totalPages: {
    type: Number,
    required: true
  },
  showingStart: {
    type: Number,
    required: true
  },
  showingEnd: {
    type: Number,
    required: true
  },
  getInitials: {
    type: Function,
    required: true
  }
});

defineEmits(['edit', 'delete', 'openCreate', 'update:pageSize', 'changePage', 'prev', 'next']);
</script>

<template>
  <div>
    <!-- Loading Skeleton -->
    <div v-if="loading && users.length === 0" class="space-y-3">
      <div v-for="n in 3" :key="n" class="bg-white p-6 rounded-xl border border-slate-200 animate-pulse flex justify-between items-center">
        <div class="space-y-2.5 w-1/2">
          <div class="h-4 bg-slate-200 rounded w-1/3"></div>
          <div class="h-3 bg-slate-200 rounded w-2/3"></div>
        </div>
        <div class="h-8 bg-slate-200 rounded w-24"></div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else-if="filteredUsers.length === 0" class="bg-white rounded-xl border border-dashed border-slate-300 p-12 text-center">
      <div class="w-12 h-12 bg-indigo-50 text-indigo-500 rounded-full flex items-center justify-center mx-auto mb-3">
        <i class="fa-solid fa-user-slash text-xl"></i>
      </div>
      <h3 class="text-base font-semibold text-slate-800 mb-1">No users found</h3>
      <p class="text-sm text-slate-500 max-w-sm mx-auto mb-4">
        {{ searchQuery ? 'Try adjusting your search query to find what you are looking for.' : 'No users currently exist in the store.' }}
      </p>
      <button v-if="!searchQuery" @click="$emit('openCreate')" class="inline-flex items-center space-x-2 text-sm bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition">
        <i class="fa-solid fa-plus"></i>
        <span>Create the First User</span>
      </button>
    </div>

    <!-- Users Table Card -->
    <div v-else class="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-200 text-left">
          <thead class="bg-slate-50">
            <tr>
              <th scope="col" class="px-6 py-3.5 text-xs font-semibold text-slate-500 uppercase tracking-wider">User Details</th>
              <th scope="col" class="px-6 py-3.5 text-xs font-semibold text-slate-500 uppercase tracking-wider">Address</th>
              <th scope="col" class="px-6 py-3.5 text-xs font-semibold text-slate-500 uppercase tracking-wider">Contact</th>
              <th scope="col" class="px-6 py-3.5 text-xs font-semibold text-slate-500 uppercase tracking-wider text-right">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200 bg-white">
            <tr v-for="user in paginatedUsers" :key="user.id" class="hover:bg-slate-50 transition-colors">
              <!-- Name & ID -->
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 text-white font-bold flex items-center justify-center text-sm shadow-sm">
                    {{ getInitials(user.name) }}
                  </div>
                  <div>
                    <div class="text-sm font-semibold text-slate-900">{{ user.name }}</div>
                    <div class="text-xs font-mono text-slate-400">ID: {{ user.id }}</div>
                  </div>
                </div>
              </td>

              <!-- Address -->
              <td class="px-6 py-4">
                <div class="text-sm text-slate-700 max-w-xs truncate flex items-start space-x-2">
                  <i class="fa-solid fa-location-dot text-slate-400 text-xs mt-1"></i>
                  <span :title="user.address">{{ user.address }}</span>
                </div>
              </td>

              <!-- Contact info -->
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="space-y-1">
                  <div class="text-xs text-slate-700 flex items-center space-x-2">
                    <i class="fa-solid fa-envelope text-indigo-400 w-3"></i>
                    <a :href="'mailto:' + user.email" class="hover:underline text-indigo-600">{{ user.email }}</a>
                  </div>
                  <div class="text-xs text-slate-600 flex items-center space-x-2">
                    <i class="fa-solid fa-phone text-emerald-500 w-3"></i>
                    <span>{{ user.telephone }}</span>
                  </div>
                </div>
              </td>

              <!-- Action Buttons -->
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex items-center justify-end space-x-2">
                  <button @click="$emit('edit', user)" class="p-2 text-slate-600 hover:text-indigo-600 hover:bg-indigo-50 rounded-lg transition" title="Edit User">
                    <i class="fa-solid fa-pen-to-square"></i>
                  </button>
                  <button @click="$emit('delete', user)" class="p-2 text-slate-600 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition" title="Delete User">
                    <i class="fa-solid fa-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination Controls -->
      <PaginationControls
        :current-page="currentPage"
        :page-size="pageSize"
        :page-size-options="pageSizeOptions"
        :total-pages="totalPages"
        :showing-start="showingStart"
        :showing-end="showingEnd"
        :total-filtered="filteredUsers.length"
        @update:page-size="$emit('update:pageSize', $event)"
        @change-page="$emit('changePage', $event)"
        @prev="$emit('prev')"
        @next="$emit('next')"
      />
    </div>
  </div>
</template>
