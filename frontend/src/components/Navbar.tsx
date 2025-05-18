// Navbar.tsx
import {AppBar, Box, Button, Toolbar, Typography} from '@mui/material'
import {logout} from '../service/AuthService.ts'

export default function Navbar() {
    const handleLogout = async () => {
        const success = await logout()
        if (success) {
            window.location.href = '/login'
        } else {
            console.error('Logout failed')
        }
    }

    return (
        <AppBar sx={{ m: 0, p: 0, width: '100%', backgroundColor: 'green' }}>
            <Toolbar sx={{ m: 0, p: 0 }}>
                <Typography variant="h6" component="div">
                    Enefit
                </Typography>
                <Box sx={{ flexGrow: 1 }} />
                <Button color="inherit" onClick={handleLogout}>
                    Logout
                </Button>
            </Toolbar>
        </AppBar>
    )
}