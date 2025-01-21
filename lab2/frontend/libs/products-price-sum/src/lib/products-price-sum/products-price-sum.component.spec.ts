import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductsPriceSumComponent } from './products-price-sum.component';

describe('ProductsPriceSumComponent', () => {
  let component: ProductsPriceSumComponent;
  let fixture: ComponentFixture<ProductsPriceSumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsPriceSumComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductsPriceSumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
