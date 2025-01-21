import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { API_EBAY_PREFIX, BASE_URL_EBAY } from "../tokens";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class EbayService {
    private readonly baseUrl = inject(BASE_URL_EBAY);
    private readonly apiEbayPrefix = inject(API_EBAY_PREFIX);
    private readonly http = inject(HttpClient);

    public increase(percent: number): Observable<void> {
        return this.http.post<void>(
            `${this.baseUrl}/price/increase/${percent}`,
            null,
        )
    }

    public decrease(percent: number): Observable<void> {
        return this.http.post<void>(
            `${this.baseUrl}/price/decrease/${percent}`,
            null,
        )
    }
}