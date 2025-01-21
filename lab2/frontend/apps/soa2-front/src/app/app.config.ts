import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { NG_EVENT_PLUGINS } from '@taiga-ui/event-plugins';
import { API_EBAY_PREFIX, API_PERSON_PREFIX, API_PRODUCTS_PREFIX, BASE_URL_EBAY, BASE_URL_PRODUCTS, } from "@soa2-front/shared";
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(appRoutes),
    provideHttpClient(withInterceptorsFromDi()),
    provideAnimations(),
    NG_EVENT_PLUGINS,
    {
      provide: API_PRODUCTS_PREFIX,
      useValue: 'products'
    },
    {
      provide: API_PERSON_PREFIX,
      useValue: 'persons'
    },
    {
      provide: API_EBAY_PREFIX,
      useValue: 'ebay'
    },
    {
      provide: BASE_URL_PRODUCTS,
      useValue: 'https://89.169.155.80:9912',
    },
    {
      provide: BASE_URL_EBAY,
      useValue: 'https://89.169.155.80:9912',
    }
  ],
};
