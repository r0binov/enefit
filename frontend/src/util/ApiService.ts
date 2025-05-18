export async function apiFetch(input: RequestInfo, init?: RequestInit) {
    const response = await fetch(input, { credentials: "include", ...init });
    if (response.status === 401 || response.status === 403) {
        window.location.href = "/login";
        return;
    }
    return response;
}