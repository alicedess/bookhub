import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookEvaluations } from './book-evaluations';

describe('BookEvaluations', () => {
  let component: BookEvaluations;
  let fixture: ComponentFixture<BookEvaluations>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookEvaluations],
    }).compileComponents();

    fixture = TestBed.createComponent(BookEvaluations);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
