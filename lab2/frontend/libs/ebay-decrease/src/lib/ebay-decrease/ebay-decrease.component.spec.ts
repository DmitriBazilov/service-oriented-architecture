import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EbayDecreaseComponent } from './ebay-decrease.component';

describe('EbayDecreaseComponent', () => {
  let component: EbayDecreaseComponent;
  let fixture: ComponentFixture<EbayDecreaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EbayDecreaseComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EbayDecreaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
