import type {ConsumptionWithPricesDto} from './ConsumptionWithPricesDto'

export interface YearlyConsumptionWithPricesDto {
  year: number
  values: ConsumptionWithPricesDto[]
}