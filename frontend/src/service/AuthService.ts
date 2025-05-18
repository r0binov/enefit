import {apiFetch} from "../util/ApiService";

const API_ROOT = "http://localhost:8080";

export async function login(username: string, password: string): Promise<boolean> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);

    const res = await apiFetch(`${API_ROOT}/api/auth/login`, {
        method: 'POST',
        body: formData,
        credentials: 'include'
    });
    return res ? res.ok : false;
}

export async function logout(): Promise<boolean> {
    const res = await fetch(`${API_ROOT}/api/auth/logout`, {
        method: 'POST',
        credentials: 'include'
    })
    return res.ok
}
