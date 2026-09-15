<script setup>
defineProps({
  loading: {
    type: Boolean,
    default: false
  }
});

defineEmits(['refresh', 'openCreate']);
</script>

<template>
  <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-6">
    <div>
      <h2 class="text-2xl font-bold text-slate-900">Registered Users</h2>
      <p class="text-sm text-slate-500">
        Manage user directory via REST endpoint
        <code class="bg-slate-200 text-slate-700 px-1.5 py-0.5 rounded text-xs font-mono">/digg/user</code>
      </p>
    </div>
    <div class="flex items-center space-x-3 w-full sm:w-auto">
      <button
        @click="$emit('refresh')"
        :disabled="loading"
        class="px-3.5 py-2 rounded-lg bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 text-sm font-medium transition flex items-center space-x-2 shadow-sm"
      >
        <i class="fa-solid fa-arrows-rotate" :class="{ 'animate-spin': loading }"></i>
        <span>Refresh</span>
      </button>
      <button
        @click="$emit('openCreate')"
        class="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-semibold transition flex items-center space-x-2 shadow-sm"
      >
        <i class="fa-solid fa-user-plus"></i>
        <span>Add New User</span>
      </button>
    </div>
  </div>
</template>
