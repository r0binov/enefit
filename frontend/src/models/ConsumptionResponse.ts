export interface ConsumptionWithPricesDto {
    month: string
    totalPriceWithVat: string
    totalPriceWithoutVat: string
    consumptionInKw: string // Use string if backend sends as string, or number if BigDecimal is serialized as number
    baselinePricePerMwh: string
    baselinePriceMwhWithVat: string
}