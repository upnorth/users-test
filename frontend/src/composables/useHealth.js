import { ref } from 'vue';
import { getHealth } from '../api/userClient';

export function useHealth() {
  const healthStatus = ref('CHECKING');

  async function checkHealth() {
    healthStatus.value = await getHealth();
  }

  return {
    healthStatus,
    checkHealth
  };
}
