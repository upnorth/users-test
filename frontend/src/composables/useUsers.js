import { ref, computed } from 'vue';
import { getUsers } from '../api/userClient';

export function useUsers() {
  const users = ref([]);
  const loading = ref(false);
  const searchQuery = ref('');

  const filteredUsers = computed(() => {
    const q = searchQuery.value.trim().toLowerCase();
    if (!q) return users.value;
    return users.value.filter(u =>
      (u.name && u.name.toLowerCase().includes(q)) ||
      (u.email && u.email.toLowerCase().includes(q)) ||
      (u.telephone && u.telephone.toLowerCase().includes(q)) ||
      (u.address && u.address.toLowerCase().includes(q)) ||
      (u.id && u.id.toLowerCase().includes(q))
    );
  });

  async function loadUsers() {
    loading.value = true;
    try {
      users.value = await getUsers();
    } finally {
      loading.value = false;
    }
  }

  function getInitials(name) {
    if (!name) return 'U';
    const parts = name.trim().split(' ');
    if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }

  return {
    users,
    loading,
    searchQuery,
    filteredUsers,
    loadUsers,
    getInitials
  };
}
