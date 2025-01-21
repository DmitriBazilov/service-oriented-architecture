import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class FunService {
    public readonly funToggle$ = new BehaviorSubject<boolean>(false);
}