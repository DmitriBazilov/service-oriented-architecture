import { ColorType } from "./color.enum";

export interface Person {
    name: string;
    passportID: string;
    eyeColor: ColorType;
    hairColor: ColorType;
}