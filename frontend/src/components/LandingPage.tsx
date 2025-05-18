import {useEffect, useState} from 'react'
import {
    Box,
    CircularProgress,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from '@mui/material'
import {BarChart} from '@mui/x-charts/BarChart'
import {getConsumptionsWithPrices} from '../service/ConsumptionService.ts'
import {getMeteringPoints} from '../service/MeteringPointService.ts'
import type {MeteringPointResponse} from '../models/MeteringPointResponse.ts'
import type {YearlyConsumptionWithPricesDto} from '../models/YearlyConsumptionWithPricesDto.ts'
import Navbar from "./Navbar.tsx";

export default function LandingPage() {
    const [meteringPoints, setMeteringPoints] = useState<MeteringPointResponse[]>([])
    const [consumptions, setConsumptions] = useState<YearlyConsumptionWithPricesDto[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const meteringPoints = await getMeteringPoints()
                setMeteringPoints(meteringPoints)

                const consumptions = await getConsumptionsWithPrices()
                setConsumptions(consumptions)
            } catch (err) {
                console.error(err)
            } finally {
                setLoading(false)
            }
        }
        fetchData().catch(console.error)
    }, [])

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
                <CircularProgress color={"success"}/>
            </Box>
        )
    }

    const chartData = consumptions.map(yearObj => ({
        year: yearObj.year,
        months: yearObj.values.map(v => v.month),
        consumptionsKw: yearObj.values.map(v => v.consumptionInKw),
        totalPriceWithVat: yearObj.values.map(v => Number(v.totalPriceWithVat)),
        totalPriceWithoutVat: yearObj.values.map(v => Number(v.totalPriceWithoutVat)),
        baselinePricePerMwh: yearObj.values.map(v => Number(v.baselinePricePerMwh)),
        baselinePriceMwhWithVat: yearObj.values.map(v => Number(v.baselinePriceMwhWithVat)),
    }));

    return (

        <Box>
            <Navbar/>
            <Box display="flex" flexDirection="column" gap={4} p={4}>
                {chartData.map(data => (
                    <Paper key={data.year} sx={{p: 2, mb: 2}}>
                        <Typography variant="h6" mb={2}>
                            Monthly Consumption & Prices ({data.year})
                        </Typography>
                        <BarChart
                            series={[
                                {data: data.consumptionsKw, label: 'Consumption (kWh)'},
                                {
                                    data: data.totalPriceWithVat,
                                    label: 'Total Price (With VAT)',
                                    valueFormatter: (v: number | null) => v !== null ? `${v} €` : ''
                                },
                                {
                                    data: data.totalPriceWithoutVat,
                                    label: 'Total Price (Without VAT)',
                                    valueFormatter: (v: number | null) => v !== null ? `${v} €` : ''
                                },
                                {
                                    data: data.baselinePricePerMwh,
                                    label: 'Baseline Price/MWh',
                                    valueFormatter: (v: number | null) => v !== null ? `${v} €` : ''
                                },
                                {
                                    data: data.baselinePriceMwhWithVat,
                                    label: 'Baseline Price/MWh (With VAT)',
                                    valueFormatter: (v: number | null) => v !== null ? `${v} €` : ''
                                }
                            ]}
                            height={300}
                            xAxis={[{data: data.months}]}/>
                    </Paper>
                ))}
                <Paper sx={{p: 2}}>
                    <Typography variant="h6" mb={2}>Metering Points</Typography>
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Address</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {meteringPoints.map(mp => (
                                    <TableRow key={mp.id}>
                                        <TableCell>{mp.id}</TableCell>
                                        <TableCell>{mp.address}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>
            </Box>
        </Box>
    )
}