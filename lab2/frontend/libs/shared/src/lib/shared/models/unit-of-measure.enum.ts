export const UnitOfMeasure = {
    Kilograms: 'KILOGRAMS',
    Meters: 'METERS',
    Milliliters: 'MILLILITERS',
    Milligrams: 'MILLIGRAMS',
} as const;

export type UnitOfMeasureType =
    (typeof UnitOfMeasure)[keyof typeof UnitOfMeasure];

export const UnitOfMeasureRusMap = new Map([
    ['Килограммы', UnitOfMeasure.Kilograms],
    ['Метры', UnitOfMeasure.Meters],
    ['Миллилитры', UnitOfMeasure.Milliliters],
    ['Миллиграммы', UnitOfMeasure.Milligrams]
]);

export const UnitOfMeasureEngMap = new Map([
    [UnitOfMeasure.Kilograms, 'Килограммы'],
    [UnitOfMeasure.Meters, 'Метры'],
    [UnitOfMeasure.Milliliters, 'Миллилитры'],
    [UnitOfMeasure.Milligrams, 'Миллиграммы']
]);