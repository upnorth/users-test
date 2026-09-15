<script setup>
defineProps({
  show: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  form: {
    type: Object,
    required: true
  },
  formErrors: {
    type: Object,
    required: true
  },
  submitting: {
    type: Boolean,
    default: false
  }
});

defineEmits(['close', 'save']);
</script>

<template>
  <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
    <div class="bg-white rounded-2xl shadow-xl border border-slate-200 max-w-lg w-full overflow-hidden transition-all">
      <!-- Modal Header -->
      <div class="px-6 py-4 border-b border-slate-100 flex items-center justify-between bg-slate-50">
        <div class="flex items-center space-x-2">
          <i :class="isEdit ? 'fa-solid fa-user-pen text-indigo-600' : 'fa-solid fa-user-plus text-indigo-600'"></i>
          <h3 class="text-lg font-bold text-slate-900">{{ isEdit ? 'Edit User' : 'Create New User' }}</h3>
        </div>
        <button @click="$emit('close')" class="text-slate-400 hover:text-slate-600">
          <i class="fa-solid fa-xmark text-lg"></i>
        </button>
      </div>

      <!-- Modal Form -->
      <form @submit.prevent="$emit('save')" class="p-6 space-y-4">
        <!-- Form Errors Banner -->
        <div v-if="formErrors.general" class="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-lg">
          {{ formErrors.general }}
        </div>

        <!-- Name -->
        <div>
          <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">Full Name *</label>
          <input
            type="text"
            v-model="form.name"
            placeholder="e.g. Jane Doe"
            class="w-full px-3 py-2 text-sm rounded-lg border focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            :class="formErrors.name ? 'border-rose-400 bg-rose-50' : 'border-slate-300'"
          />
          <p v-if="formErrors.name" class="text-xs text-rose-500 mt-1">{{ formErrors.name }}</p>
        </div>

        <!-- Email -->
        <div>
          <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">Email Address *</label>
          <input
            type="email"
            v-model="form.email"
            placeholder="e.g. jane.doe@example.com"
            class="w-full px-3 py-2 text-sm rounded-lg border focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            :class="formErrors.email ? 'border-rose-400 bg-rose-50' : 'border-slate-300'"
          />
          <p v-if="formErrors.email" class="text-xs text-rose-500 mt-1">{{ formErrors.email }}</p>
        </div>

        <!-- Telephone -->
        <div>
          <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">Telephone *</label>
          <input
            type="text"
            v-model="form.telephone"
            placeholder="e.g. +1 (555) 234-5678"
            class="w-full px-3 py-2 text-sm rounded-lg border focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            :class="formErrors.telephone ? 'border-rose-400 bg-rose-50' : 'border-slate-300'"
          />
          <p v-if="formErrors.telephone" class="text-xs text-rose-500 mt-1">{{ formErrors.telephone }}</p>
        </div>

        <!-- Address -->
        <div>
          <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">Address *</label>
          <textarea
            v-model="form.address"
            rows="2"
            placeholder="e.g. 123 Main Street, Suite 400, Springfield"
            class="w-full px-3 py-2 text-sm rounded-lg border focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            :class="formErrors.address ? 'border-rose-400 bg-rose-50' : 'border-slate-300'"
          ></textarea>
          <p v-if="formErrors.address" class="text-xs text-rose-500 mt-1">{{ formErrors.address }}</p>
        </div>

        <!-- Modal Actions -->
        <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
          <button
            type="button"
            @click="$emit('close')"
            class="px-4 py-2 rounded-lg border border-slate-300 text-slate-700 hover:bg-slate-100 text-sm font-medium transition"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="submitting"
            class="px-5 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-semibold transition flex items-center space-x-2 shadow-sm"
          >
            <i v-if="submitting" class="fa-solid fa-spinner animate-spin"></i>
            <span>{{ isEdit ? 'Save Changes' : 'Create User' }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
