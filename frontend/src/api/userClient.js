export async function getUsers() {
  const res = await fetch('/digg/user');
  if (!res.ok) {
    const errData = await res.json().catch(() => ({}));
    throw new Error(errData.error || errData.title || `HTTP ${res.status}`);
  }
  return res.json();
}

export async function getUser(id) {
  const res = await fetch(`/digg/user/${id}`);
  if (!res.ok) {
    const errData = await res.json().catch(() => ({}));
    throw new Error(errData.error || errData.title || `HTTP ${res.status}`);
  }
  return res.json();
}

export async function createUser(payload) {
  const res = await fetch('/digg/user', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const errData = await res.json().catch(() => ({}));
    throw new Error(errData.error || errData.title || `Server returned ${res.status}`);
  }
  return res.json();
}

export async function updateUser(id, payload) {
  const res = await fetch(`/digg/user/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const errData = await res.json().catch(() => ({}));
    throw new Error(errData.error || errData.title || `Server returned ${res.status}`);
  }
  return res.json();
}

export async function deleteUser(id) {
  const res = await fetch(`/digg/user/${id}`, {
    method: 'DELETE'
  });
  if (!res.ok) {
    const errData = await res.json().catch(() => ({}));
    throw new Error(errData.error || errData.title || `HTTP ${res.status}`);
  }
  return true;
}

export async function getHealth() {
  try {
    const res = await fetch('/health');
    if (res.ok) {
      const data = await res.json();
      return data.status || 'UP';
    }
    return 'DOWN';
  } catch {
    return 'OFFLINE';
  }
}
