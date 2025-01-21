import { Coordinates } from "./coordinates";
import { UnitOfMeasureType } from "./unit-of-measure.enum";

export interface CreateProductPayload {
    name: string;
    coordinates: Coordinates;
    price: number;
    partNumber: string | null;
    manufacturerCost: number;
    unitOfMeasure: UnitOfMeasureType;
    ownerPassportId: string | null;
}