export const HairColor = {
    Black: 'BLACK',
    DarkBlonde: 'DARK_BLONDE',
    Blonde: 'BLONDE',
    Red: 'RED',
    Gray: 'GRAY',
} as const;

export type HairColorType =
    (typeof HairColor)[keyof typeof HairColor];

export const HairRusColorMap = new Map([
    ['Черный', HairColor.Black],
    ['Русый', HairColor.DarkBlonde],
    ['Блондинистый', HairColor.Blonde],
    ['Рыжий', HairColor.Red],
    ['Седой', HairColor.Gray],
]);

export const HairEngColorMap = new Map([
    [HairColor.Black, 'Черный'],
    [HairColor.DarkBlonde, 'Русый'],
    [HairColor.Blonde, 'Блондинистый'],
    [HairColor.Red, 'Рыжий'],
    [HairColor.Gray, 'Седой'],
]);
