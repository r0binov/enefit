export interface ConsumptionWithPricesDto {
    month: string
    totalPriceWithVat: number
    totalPriceWithoutVat: number
    consumptionInKw: number
    baselinePricePerMwh: number
    baselinePriceMwhWithVat: number
}