import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfApkComponent } from './list-of-apk.component';

describe('ListOfApkComponent', () => {
  let component: ListOfApkComponent;
  let fixture: ComponentFixture<ListOfApkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListOfApkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListOfApkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
