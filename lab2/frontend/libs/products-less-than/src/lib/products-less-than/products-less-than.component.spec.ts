import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductsLessThanComponent } from './products-less-than.component';

describe('ProductsLessThanComponent', () => {
  let component: ProductsLessThanComponent;
  let fixture: ComponentFixture<ProductsLessThanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsLessThanComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductsLessThanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
