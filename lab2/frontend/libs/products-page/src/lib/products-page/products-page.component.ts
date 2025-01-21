import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton, TuiDataList, TuiDialogService, TuiHint, TuiNumberFormat, TuiSelect } from '@taiga-ui/core';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TuiInputModule, TuiInputNumberModule, TuiSelectModule } from '@taiga-ui/legacy';
import { TuiDataListWrapper } from '@taiga-ui/kit/components/data-list-wrapper';
import { noEmptyStringValidator } from './no-empty-string.validator';
import { moreTenStringValidator } from './more-ten-string.validator';
import { FunService, Product, UnitOfMeasureRusMap } from '@soa2-front/shared';
import { ProductsService } from '@soa2-front/shared';
import { BehaviorSubject, switchMap } from 'rxjs';
import { ProductViewComponent } from '@soa2-front/product-view';
import { ProductEditComponent } from '@soa2-front/product-edit';
import { ProductsPriceSumComponent } from '@soa2-front/products-price-sum';
import { ProductMinComponent } from '@soa2-front/product-min';
import { ProductsLessThanComponent } from '@soa2-front/products-less-than';
import { ProductByIdComponent } from '@soa2-front/product-by-id';

@Component({
  selector: 'lib-products-page',
  standalone: true,
  imports: [
    CommonModule, 
    TuiButton, 
    ReactiveFormsModule,
    TuiInputModule,
    TuiInputNumberModule,
    TuiNumberFormat,
    TuiSelectModule,
    TuiDataList,
    TuiDataListWrapper,
    TuiHint,
    ProductViewComponent,
    ProductEditComponent,
    ProductsPriceSumComponent,
    ProductMinComponent,
    ProductsLessThanComponent,
    ProductByIdComponent,
    TuiSelect,
  ],
  templateUrl: './products-page.component.html',
  styleUrl: './products-page.component.less',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductsPageComponent {
  private readonly funService = inject(FunService);
  private readonly dialogService = inject(TuiDialogService);
  private readonly productService = inject(ProductsService);
  private readonly alertService = inject(TuiAlertService);

  private readonly search$ = new BehaviorSubject<number>(0);
  protected readonly funToggle$ = this.funService.funToggle$;

  protected readonly sortValues = ['ID', 'NAME', 'CREATION_DATE', 'PRICE', 'PART_NUMBER', 'MANUFACTURER_COST', 'UNIT_OF_MEASURE', 'OWNER_PASSPORT_ID']
  protected readonly filterValues1 = ['ID', 'NAME', 'CREATION_DATE', 'PRICE', 'PART_NUMBER', 'MANUFACTURER_COST', 'UNIT_OF_MEASURE', 'OWNER_PASSPORT_ID']
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected readonly sortValues1: any[] = [];
  protected readonly sortControls: FormControl[] = [];

  protected readonly sortValue = new FormControl<string>('ID')
  protected readonly filterValue = new FormControl<string>('ID')

  protected readonly products1$ = new BehaviorSubject<Product[]>([]);

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected readonly filterCriteria: any[] = [];
  protected readonly filterOperators: FormControl[] = [];
  protected readonly filterValues: FormControl[] = [];

  protected readonly page = new FormControl<number>(1);
  protected readonly size = new FormControl<number>(10);

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected readonly sortArray = new FormArray<any>([]);

  protected readonly products$ = this.search$.pipe(
    switchMap(() => this.productService.getProducts())
  );

  protected readonly selectedProduct$ = new BehaviorSubject<Product | null>(null);

  protected readonly unitsOfMeasure = [
    'Килограммы',
    'Метры',
    'Миллиграммы',
    'Миллилитры'
  ];

  protected readonly productForm = new FormGroup({
    name: new FormControl<string>('', [noEmptyStringValidator()]),
    coordinateX: new FormControl<number>(0),
    coordinateY: new FormControl<number>(0),
    price: new FormControl<number>(1),
    partNumber: new FormControl<string | null>(null, [moreTenStringValidator()]),
    manufactureCost: new FormControl<number>(1),
    unitOfMeasure: new FormControl<string>('Килограммы'),
    personPassportId: new FormControl<string | null>(null),
  });

  protected openCreateForm(template: PolymorpheusContent): void {
    this.dialogService.open(template, {
      label: 'Создание продукта'
    }).subscribe()
  }

  
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected submitForm(observer: any): void {
    const name = this.productForm.controls.name.value;
    const coordinateX = this.productForm.controls.coordinateX.value;
    const coordinateY = this.productForm.controls.coordinateY.value;
    const price = this.productForm.controls.price.value;
    const partNumber = this.productForm.controls.partNumber.value;
    const manufacturerCost = this.productForm.controls.manufactureCost.value;
    const unitOfMeasure = this.productForm.controls.unitOfMeasure.value;
    const personPassportId = this.productForm.controls.personPassportId.value;

    if (
      !name || coordinateX === null || coordinateY === null || price === null ||
      manufacturerCost === null || !unitOfMeasure
    ) {
      return;
    }

    const mappedUnitOfMeasure = UnitOfMeasureRusMap.get(unitOfMeasure);

    if (!mappedUnitOfMeasure) {
      return;
    }

    this.productService.createProduct({
      name,
      coordinates: {
        x: coordinateX,
        y: coordinateY,
      },
      price,
      partNumber,
      manufacturerCost,
      unitOfMeasure: mappedUnitOfMeasure,
      ownerPassportId: personPassportId,
    }).subscribe({
      next: () => {
        observer.complete();

        this.alertService.open("Вы успешно создали новый продукт", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      },
      error: () => {
        this.alertService.open("Не удалось создать новый продукт", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    });
  }

  protected setViewProduct(template: PolymorpheusContent, product: Product): void {
    this.selectedProduct$.next(product);
  
    this.dialogService.open(template, {
      label: 'Просмотр продукта'
    }).subscribe()
  }

  protected deleteProduct(productId: number): void {
    this.productService.deleteProduct(productId).subscribe({
      next: () => {
        this.search$.next(this.search$.value + 1);
  
        this.alertService.open("Вы успешно удалили продукт", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      }, 
      error: () => {
        this.alertService.open("Не удалось удалить продукт", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    })
  }

  protected setEditProduct(template: PolymorpheusContent, product: Product): void {
    this.selectedProduct$.next(product);
  
    this.dialogService.open(template, {
      label: 'Редактирование продукт'
    }).subscribe()
  }

  protected addSort(): void {
    const sortToAdd = this.sortValue.value;

    if (!sortToAdd) {
      return;
    }

    this.sortValues1.push(sortToAdd);
    this.sortControls.push(new FormControl<string>('ASC'))

    this.sortValue.setValue(this.filterValuesFunc(this.sortValues, this.sortValues1)[0]);
  }

  protected deleteSort(index: number): void {
    this.sortValues1.splice(index, 1);
    this.sortControls.splice(index, 1);
  }

  protected addFilter(): void {
    const filterToAdd = this.filterValue.value;

    console.log(filterToAdd)

    this.filterCriteria.push(filterToAdd);
    this.filterOperators.push(new FormControl<string>('EQUAL'));
    this.filterValues.push(new FormControl<string>(''));
    this.filterValue.setValue(this.filterValuesFunc(this.filterValues1, this.filterCriteria)[0]);
  }

  protected deleteFilter(index: number): void {
    this.filterCriteria.splice(index, 1);
    this.filterOperators.splice(index, 1);
    this.filterValues.splice(index, 1);
  }

  protected search(): void {
    this.productService.getProductsWithSettings(
      this.page.value || 1,
      this.size.value || 10,
      this.sortValues1,
      this.sortControls.map((control) => control.value),
      this.filterCriteria,
      this.filterOperators.map((control) => control.value),
      this.filterValues.map((control) => control.value)
    ).subscribe(x => this.products1$.next(x));
  }

  protected filterValuesFunc(array: string[], exclude: string[]): string[] {
    return array.filter((val) => !exclude.includes(val));
  }

  protected isAllFiltersOk(array: FormControl[]): boolean {
    const arrayMapped = array.map((control) => control.value);

    let flag = true;

    arrayMapped.forEach((value) => {
      if (!value) {
        flag = false;
      }
    })

    return flag;
  }
}
