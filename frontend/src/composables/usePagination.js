import { ref, computed, watch } from 'vue';

export function usePagination(itemsRef, defaultPageSize = 5) {
  const currentPage = ref(1);
  const pageSize = ref(defaultPageSize);
  const pageSizeOptions = [5, 10, 20];

  const totalPages = computed(() => {
    const total = itemsRef.value ? itemsRef.value.length : 0;
    return Math.max(1, Math.ceil(total / pageSize.value));
  });

  const paginatedUsers = computed(() => {
    if (!itemsRef.value) return [];
    const start = (currentPage.value - 1) * pageSize.value;
    return itemsRef.value.slice(start, start + pageSize.value);
  });

  const showingStart = computed(() => {
    const total = itemsRef.value ? itemsRef.value.length : 0;
    if (total === 0) return 0;
    return (currentPage.value - 1) * pageSize.value + 1;
  });

  const showingEnd = computed(() => {
    const total = itemsRef.value ? itemsRef.value.length : 0;
    return Math.min(currentPage.value * pageSize.value, total);
  });

  function goToPage(page) {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page;
    }
  }

  function prevPage() {
    if (currentPage.value > 1) {
      currentPage.value--;
    }
  }

  function nextPage() {
    if (currentPage.value < totalPages.value) {
      currentPage.value++;
    }
  }

  function resetPage() {
    currentPage.value = 1;
  }

  watch(pageSize, () => {
    currentPage.value = 1;
  });

  watch(totalPages, (newTotal) => {
    if (currentPage.value > newTotal) {
      currentPage.value = newTotal;
    }
  });

  return {
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
  };
}
