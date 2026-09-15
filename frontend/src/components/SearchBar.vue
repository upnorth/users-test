<script setup>
defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  filteredCount: {
    type: Number,
    required: true
  },
  totalCount: {
    type: Number,
    required: true
  }
});

defineEmits(['update:modelValue']);
</script>

<template>
  <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 mb-6 flex flex-col sm:flex-row gap-4 items-center justify-between">
    <div class="relative w-full sm:w-96">
      <span class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400">
        <i class="fa-solid fa-magnifying-glass"></i>
      </span>
      <input
        type="text"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        placeholder="Search by name, email, phone, or address..."
        class="w-full pl-9 pr-4 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
      />
    </div>
    <div class="text-xs text-slate-500 font-medium">
      Showing <span class="font-bold text-slate-700">{{ filteredCount }}</span> of <span class="font-bold text-slate-700">{{ totalCount }}</span> users
    </div>
  </div>
</template>
