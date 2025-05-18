import type {YearlyConsumptionWithPricesDto} from "../models/YearlyConsumptionWithPricesDto";
import {apiFetch} from "../util/ApiService";

const API_ROOT = import.meta.env.VITE_API_ROOT

export async function getConsumptionsWithPrices(): Promise<YearlyConsumptionWithPricesDto[]> {
    const res = await apiFetch(`${API_ROOT}/api/consumption/with-prices`, {
        credentials: 'include'
    });
    if (!res || !res.ok) throw new Error('Failed to fetch consumption data');
    return await res.json();
}