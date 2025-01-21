import { Route } from '@angular/router';

export const appRoutes: Route[] = [
    {
        path: 'products',
        loadComponent: async () => 
            import('@soa2-front/products-page').then((m) => m.ProductsPageComponent)
    },
    {
        path: 'persons',
        loadComponent: async () =>
            import('@soa2-front/persons-page').then((m) => m.PersonsPageComponent),
    },
    {
        path: 'ebay',
        loadComponent: async () => 
            import('@soa2-front/ebay-page').then((m) => m.EbayPageComponent),
    },
];
