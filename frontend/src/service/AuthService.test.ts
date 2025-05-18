import {login, logout} from './AuthService';
import {apiFetch} from '../util/ApiService';

jest.mock('../util/ApiService', () => ({
    apiFetch: jest.fn(),
}));

const OLD_ENV = process.env;

beforeEach(() => {
    jest.resetModules();
    process.env = { ...OLD_ENV };
    global.fetch = jest.fn();
});

afterAll(() => {
    process.env = OLD_ENV;
});

describe('AuthService', () => {
    describe('login', () => {
        it('returns true when apiFetch returns ok', async () => {
            (apiFetch as jest.Mock).mockResolvedValue({ ok: true });
            const result = await login('user', 'pass');
            expect(result).toBe(true);
            expect(apiFetch).toHaveBeenCalled();
        });

        it('returns false when apiFetch returns not ok', async () => {
            (apiFetch as jest.Mock).mockResolvedValue({ ok: false });
            const result = await login('user', 'pass');
            expect(result).toBe(false);
        });

        it('returns false when apiFetch returns null/undefined', async () => {
            (apiFetch as jest.Mock).mockResolvedValue(null);
            const result = await login('user', 'pass');
            expect(result).toBe(false);
        });
    });

    describe('logout', () => {
        it('returns true when fetch returns ok', async () => {
            (global.fetch as jest.Mock).mockResolvedValue({ ok: true });
            const result = await logout();
            expect(result).toBe(true);
            expect(global.fetch).toHaveBeenCalled();
        });

        it('returns false when fetch returns not ok', async () => {
            (global.fetch as jest.Mock).mockResolvedValue({ ok: false });
            const result = await logout();
            expect(result).toBe(false);
        });
    });
});