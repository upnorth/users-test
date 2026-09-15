<script setup>
defineProps({
  show: {
    type: Boolean,
    default: false
  },
  user: {
    type: Object,
    default: null
  },
  submitting: {
    type: Boolean,
    default: false
  }
});

defineEmits(['close', 'confirm']);
</script>

<template>
  <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
    <div class="bg-white rounded-2xl shadow-xl border border-slate-200 max-w-md w-full p-6 text-center">
      <div class="w-12 h-12 bg-rose-100 text-rose-600 rounded-full flex items-center justify-center mx-auto mb-4">
        <i class="fa-solid fa-triangle-exclamation text-xl"></i>
      </div>
      <h3 class="text-lg font-bold text-slate-900 mb-2">Delete User</h3>
      <p class="text-sm text-slate-600 mb-6">
        Are you sure you want to delete <span class="font-bold text-slate-900">{{ user?.name }}</span>? This action will remove the user from the database.
      </p>
      <div class="flex items-center justify-center space-x-3">
        <button
          type="button"
          @click="$emit('close')"
          class="px-4 py-2 rounded-lg border border-slate-300 text-slate-700 hover:bg-slate-100 text-sm font-medium transition"
        >
          Cancel
        </button>
        <button
          type="button"
          @click="$emit('confirm')"
          :disabled="submitting"
          class="px-5 py-2 rounded-lg bg-rose-600 hover:bg-rose-700 text-white text-sm font-semibold transition flex items-center space-x-2 shadow-sm"
        >
          <i v-if="submitting" class="fa-solid fa-spinner animate-spin"></i>
          <span>Confirm Delete</span>
        </button>
      </div>
    </div>
  </div>
</template>
