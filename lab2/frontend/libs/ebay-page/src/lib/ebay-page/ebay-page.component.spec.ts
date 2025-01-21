import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EbayPageComponent } from './ebay-page.component';

describe('EbayPageComponent', () => {
  let component: EbayPageComponent;
  let fixture: ComponentFixture<EbayPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EbayPageComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EbayPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
