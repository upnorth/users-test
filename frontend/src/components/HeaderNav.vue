<script setup>
defineProps({
  healthStatus: {
    type: String,
    default: 'CHECKING'
  }
});
</script>

<template>
  <header class="bg-indigo-700 text-white shadow-lg sticky top-0 z-30">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
      <div class="flex items-center space-x-3">
        <div class="w-10 h-10 rounded-lg bg-indigo-500/40 flex items-center justify-center font-bold text-xl border border-indigo-400">
          <i class="fa-solid fa-users"></i>
        </div>
        <div>
          <h1 class="text-xl font-bold tracking-tight leading-none">UserHub</h1>
          <span class="text-xs text-indigo-200">Quarkus + Vue 3 REST API</span>
        </div>
      </div>

      <div class="flex items-center space-x-4">
        <!-- Health Badge -->
        <div class="flex items-center space-x-2 bg-indigo-800/80 px-3 py-1.5 rounded-full text-xs font-medium border border-indigo-600">
          <span
            class="w-2.5 h-2.5 rounded-full"
            :class="healthStatus === 'UP' ? 'bg-emerald-400 animate-pulse' : 'bg-amber-400'"
          ></span>
          <span>API Health: {{ healthStatus }}</span>
        </div>

        <!-- Links -->
        <a
          href="/q/swagger-ui"
          target="_blank"
          class="hidden sm:inline-flex items-center space-x-1.5 text-xs bg-indigo-600 hover:bg-indigo-500 px-3 py-1.5 rounded-md transition shadow-sm font-medium"
        >
          <i class="fa-solid fa-book"></i>
          <span>Swagger UI</span>
        </a>
        <a
          href="/q/openapi"
          target="_blank"
          class="hidden sm:inline-flex items-center space-x-1.5 text-xs bg-indigo-600 hover:bg-indigo-500 px-3 py-1.5 rounded-md transition shadow-sm font-medium"
        >
          <i class="fa-solid fa-code"></i>
          <span>OpenAPI Spec</span>
        </a>
      </div>
    </div>
  </header>
</template>
