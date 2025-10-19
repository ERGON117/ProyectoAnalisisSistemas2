import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusCuentaComponent } from './status-cuenta.component';

describe('StatusCuentaComponent', () => {
  let component: StatusCuentaComponent;
  let fixture: ComponentFixture<StatusCuentaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatusCuentaComponent]
    });
    fixture = TestBed.createComponent(StatusCuentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
