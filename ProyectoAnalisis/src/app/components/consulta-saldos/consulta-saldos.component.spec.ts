import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultaSaldosComponent } from './consulta-saldos.component';

describe('ConsultaSaldosComponent', () => {
  let component: ConsultaSaldosComponent;
  let fixture: ComponentFixture<ConsultaSaldosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConsultaSaldosComponent]
    });
    fixture = TestBed.createComponent(ConsultaSaldosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
