export const Color = {
    Yellow: 'YELLOW',
    White: 'WHITE',
    Brown: 'BROWN',
    Green: 'GREEN',
    Red: 'RED',
    Black: 'BLACK',
    Orange: 'ORANGE',
} as const;

export type ColorType =
    (typeof Color)[keyof typeof Color];