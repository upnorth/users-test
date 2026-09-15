<script setup>
defineProps({
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
  totalFiltered: {
    type: Number,
    required: true
  }
});

defineEmits(['update:pageSize', 'changePage', 'prev', 'next']);
</script>

<template>
  <div class="px-6 py-4 bg-slate-50 border-t border-slate-200 flex flex-col sm:flex-row items-center justify-between gap-4">
    <div class="flex flex-wrap items-center gap-4 text-xs text-slate-500">
      <div>
        Showing <span class="font-semibold text-slate-900">{{ showingStart }}</span> to
        <span class="font-semibold text-slate-900">{{ showingEnd }}</span> of
        <span class="font-semibold text-slate-900">{{ totalFiltered }}</span> users
      </div>
      <div class="flex items-center space-x-2">
        <label for="pageSizeSelect" class="text-slate-500">Per page:</label>
        <select
          id="pageSizeSelect"
          :value="pageSize"
          @change="$emit('update:pageSize', Number($event.target.value))"
          class="bg-white border border-slate-300 text-slate-700 text-xs rounded-lg px-2.5 py-1.5 focus:ring-2 focus:ring-indigo-500 focus:outline-none shadow-sm font-medium"
        >
          <option v-for="option in pageSizeOptions" :key="option" :value="option">{{ option }}</option>
        </select>
      </div>
    </div>

    <div class="flex items-center space-x-1.5">
      <button
        @click="$emit('prev')"
        :disabled="currentPage === 1"
        class="px-3 py-1.5 rounded-lg border border-slate-200 bg-white text-slate-700 text-xs font-medium hover:bg-slate-100 transition disabled:opacity-40 disabled:cursor-not-allowed shadow-sm flex items-center space-x-1"
      >
        <i class="fa-solid fa-chevron-left text-[10px]"></i>
        <span>Prev</span>
      </button>

      <button
        v-for="page in totalPages"
        :key="page"
        @click="$emit('changePage', page)"
        :class="currentPage === page ? 'bg-indigo-600 text-white font-bold border-indigo-600 shadow-sm' : 'bg-white text-slate-700 hover:bg-slate-100 border-slate-200'"
        class="w-8 h-8 rounded-lg border text-xs font-medium transition flex items-center justify-center"
      >
        {{ page }}
      </button>

      <button
        @click="$emit('next')"
        :disabled="currentPage === totalPages"
        class="px-3 py-1.5 rounded-lg border border-slate-200 bg-white text-slate-700 text-xs font-medium hover:bg-slate-100 transition disabled:opacity-40 disabled:cursor-not-allowed shadow-sm flex items-center space-x-1"
      >
        <span>Next</span>
        <i class="fa-solid fa-chevron-right text-[10px]"></i>
      </button>
    </div>
  </div>
</template>
