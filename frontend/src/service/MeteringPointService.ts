import type {MeteringPointResponse} from "../models/MeteringPointResponse";
import {apiFetch} from "../util/ApiService";

const API_ROOT = import.meta.env.VITE_API_ROOT

export async function getMeteringPoints(): Promise<MeteringPointResponse[]> {
    const res = await apiFetch(`${API_ROOT}/api/metering-points`, {
        credentials: 'include'
    });
    if (!res) throw new Error('Failed to fetch metering points');
    return await res.json();
}