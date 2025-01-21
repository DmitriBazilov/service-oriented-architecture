export const EyeColor = {
    Brown: 'BROWN',
    Blue: 'BLUE',
    Green: 'GREEN',
} as const;

export type EyeColorType =
    (typeof EyeColor)[keyof typeof EyeColor];

export const EyeRusColorMap = new Map([
    ['Карий', EyeColor.Brown],
    ['Голубой', EyeColor.Blue],
    ['Зеленый', EyeColor.Green],
]);

export const EyeEngColorMap = new Map([
    [EyeColor.Brown, 'Карий'],
    [EyeColor.Blue, 'Голубой'],
    [EyeColor.Green, 'Зеленый'],
]);
