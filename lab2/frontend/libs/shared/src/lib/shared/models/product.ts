import { Coordinates } from "./coordinates";
import { Owner } from "./owner";
import { UnitOfMeasureType } from "./unit-of-measure.enum";

export interface Product {
    price: number;
    name: string;
    coordinates: Coordinates;
    manufacturerCost: number;
    partNumber: string;
    id: number;
    owner: Owner;
    createdDate: string;
    unitOfMeasure: UnitOfMeasureType;
}