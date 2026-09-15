<script setup>
defineProps({
  toast: {
    type: Object,
    required: true
  }
});

defineEmits(['close']);
</script>

<template>
  <transition name="fade">
    <div
      v-if="toast.show"
      :class="toast.isError ? 'bg-rose-50 border-rose-300 text-rose-800' : 'bg-emerald-50 border-emerald-300 text-emerald-800'"
      class="mb-6 border px-4 py-3 rounded-lg shadow-sm flex items-center justify-between"
    >
      <div class="flex items-center space-x-2">
        <i :class="toast.isError ? 'fa-solid fa-circle-exclamation text-rose-500' : 'fa-solid fa-circle-check text-emerald-500'"></i>
        <span class="text-sm font-medium">{{ toast.message }}</span>
      </div>
      <button @click="$emit('close')" class="text-slate-400 hover:text-slate-600">
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>
  </transition>
</template>
