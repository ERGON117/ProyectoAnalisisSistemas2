import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleOpcionComponent } from './role-opcion.component';

describe('RoleOpcionComponent', () => {
  let component: RoleOpcionComponent;
  let fixture: ComponentFixture<RoleOpcionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoleOpcionComponent]
    });
    fixture = TestBed.createComponent(RoleOpcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
