import { Component,inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton, TuiDataList, TuiDialogService } from '@taiga-ui/core';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';
import { TuiInputModule, TuiSelectModule } from '@taiga-ui/legacy';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TuiDataListWrapper } from '@taiga-ui/kit/components/data-list-wrapper';
import { noEmptyStringValidator } from './no-empty-string.validator';
import { Color, ColorType, FunService, PersonService } from '@soa2-front/shared';
import { EyeColorType, EyeEngColorMap, EyeRusColorMap, HairColorType, HairEngColorMap, HairRusColorMap } from '@soa2-front/shared';
import { BehaviorSubject, switchMap } from 'rxjs';
import { TuiMapperPipe } from '@taiga-ui/cdk/pipes/mapper';
import { WA_WINDOW } from '@ng-web-apis/common';

const eyeEngColorMap = EyeEngColorMap;
const hairEngColorMap = HairEngColorMap;

@Component({
  selector: 'lib-persons-page',
  standalone: true,
  imports: [
    CommonModule, 
    TuiButton,
    TuiInputModule,
    ReactiveFormsModule,
    TuiSelectModule,
    TuiDataList,
    TuiDataListWrapper,
    TuiMapperPipe,
  ],
  templateUrl: './persons-page.component.html',
  styleUrl: './persons-page.component.less',
})
export class PersonsPageComponent {
  private readonly funService = inject(FunService);
  private readonly dialogService = inject(TuiDialogService);
  private readonly personService = inject(PersonService);
  private readonly alertService = inject(TuiAlertService);
  private readonly window = inject(WA_WINDOW);

  private readonly searchTrigger$ = new BehaviorSubject<number>(0);
  protected readonly funToggle$ = this.funService.funToggle$;

  protected readonly person$ = this.searchTrigger$.pipe(
    switchMap(() => this.personService.getPerson())
  );

  protected readonly eyeColors = [
    "Карий",
    "Голубой",
    "Зеленый",
  ];

  protected readonly hairColors = [
    "Черный",
    "Русый",
    "Блондинистый",
    "Рыжий",
    "Седой"
  ];

  protected readonly colors = [
    Color.Black,
    Color.Brown,
    Color.Green,
    Color.Orange,
    Color.Red,
    Color.White,
    Color.Yellow,
  ];

  protected readonly hairRusColorMap = HairRusColorMap;
  protected readonly eyeRusColorMap = EyeRusColorMap;

  protected readonly personForm = new FormGroup({
    name: new FormControl<string>('', [noEmptyStringValidator()]),
    passportId: new FormControl<string>('', [noEmptyStringValidator()]),
    eyeColor: new FormControl<ColorType>(Color.Black),
    hairColor: new FormControl<ColorType>(Color.Black),
  })

  protected openCreateForm(template: PolymorpheusContent): void {
    this.dialogService.open(template, {
      label: 'Регистрация человека'
    }).subscribe()
  }

  protected getRusEyeColor(color: EyeColorType): string {
    return eyeEngColorMap.get(color) || '-';
  }

  protected getRusHairColor(color: HairColorType): string {
    return hairEngColorMap.get(color) || '-';
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected submitForm(observer: any): void {
    const name = this.personForm.controls.name.value;
    const passportID = this.personForm.controls.passportId.value;
    const eyeColor = this.personForm.controls.eyeColor.value;
    const hairColor = this.personForm.controls.hairColor.value;
  
    if (!name || !passportID || !eyeColor || !hairColor) {
      return;
    }

    this.personService.createPerson({
      name,
      passportID,
      hairColor: hairColor,
      eyeColor: eyeColor,
    }).subscribe({
        next: () => {
          this.searchTrigger$.next(
            this.searchTrigger$.value + 1
          );

          observer.complete();

          this.alertService.open("Вы успешно зарегистрировали нового человека", {
            appearance: "success",
            label: "Успешно!"
          }).subscribe();
        },
        error: () => {
          this.alertService.open("Не удалось зарегистрировать нового пользователя", {
            appearance: "error",
            label: "Ошибка!"
          }).subscribe();
        }
      }
    )
  }

  protected copyPassportId(passportId: string): void {
    this.window.navigator.clipboard.writeText(passportId);

    this.alertService.open("Вы скопировали номер паспорта в буфер обмена", {
      appearance: "info",
      label: "Успешно!",
    }).subscribe();
  }
}
