import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CierreMesComponent } from './cierre-mes.component';

describe('CierreMesComponent', () => {
  let component: CierreMesComponent;
  let fixture: ComponentFixture<CierreMesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CierreMesComponent]
    });
    fixture = TestBed.createComponent(CierreMesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
