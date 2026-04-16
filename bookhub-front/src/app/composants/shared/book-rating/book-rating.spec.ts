import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BookRatingComponent } from './book-rating';
import { ComponentRef } from '@angular/core';

describe('BookRatingComponent', () => {
  let component: BookRatingComponent;
  let fixture: ComponentFixture<BookRatingComponent>;
  let componentRef: ComponentRef<BookRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookRatingComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(BookRatingComponent);
    component = fixture.componentInstance;

    // Accès à la référence du composant pour manipuler les inputs (signals)
    componentRef = fixture.componentRef;
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait afficher 5 étoiles pleines pour une note de 5', () => {
    // Définition de l'input signal
    fixture.componentRef.setInput('rating', 5);
    fixture.detectChanges();

    const stars = fixture.nativeElement.querySelectorAll('i.fa.fa-star');
    expect(stars.length).toBe(5);
  });

  it('devrait afficher des étoiles vides pour une note de 0', () => {
    fixture.componentRef.setInput('rating', 0);
    fixture.detectChanges();

    const emptyStars = fixture.nativeElement.querySelectorAll('i.fa-regular.fa-star');
    expect(emptyStars.length).toBe(5);
  });

  it('devrait afficher une demi-étoile pour une note décimale (ex: 3.5)', () => {
    fixture.componentRef.setInput('rating', 3.5);
    fixture.detectChanges();

    const fullStars = fixture.nativeElement.querySelectorAll('i.fa.fa-star');
    const halfStar = fixture.nativeElement.querySelectorAll('i.fa-solid.fa-star-half-stroke');
    const emptyStars = fixture.nativeElement.querySelectorAll('i.fa-regular.fa-star');

    expect(fullStars.length).toBe(3); // 1, 2, 3
    expect(halfStar.length).toBe(1);  // La 4ème
    expect(emptyStars.length).toBe(1); // La 5ème
  });

  describe('Logique getStarClass', () => {
    it('devrait retourner "fa fa-star" si l\'index est inférieur ou égal à la note', () => {
      expect(component.getStarClass(1, 3)).toBe('fa fa-star');
      expect(component.getStarClass(3, 3)).toBe('fa fa-star');
    });

    it('devrait retourner "fa-solid fa-star-half-stroke" pour la partie décimale', () => {
      // Si i=4 et note=3.5, i est > 3.5 ET (4-1) <= 3.5
      expect(component.getStarClass(4, 3.5)).toBe('fa-solid fa-star-half-stroke');
    });

    it('devrait retourner "fa-regular fa-star" si l\'index est bien supérieur à la note', () => {
      expect(component.getStarClass(5, 3.5)).toBe('fa-regular fa-star');
      expect(component.getStarClass(2, 1)).toBe('fa-regular fa-star');
    });
  });

  it('devrait mettre à jour l\'affichage lorsque la note change', () => {
    // Note initiale
    fixture.componentRef.setInput('rating', 1);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('i.fa.fa-star').length).toBe(1);

    // Nouvelle note
    fixture.componentRef.setInput('rating', 4);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('i.fa.fa-star').length).toBe(4);
  });
});
