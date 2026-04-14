import {Component, input, InputSignal} from "@angular/core";
import {NgClass} from '@angular/common';

@Component({
  selector: "app-book-rating",
  imports: [
    NgClass
  ],
  template: `
    @for (i of [1, 2, 3, 4, 5]; track i) {
      <i [ngClass]="getStarClass(i, rating())" aria-hidden="true"></i>
    }
  `
})
export class BookRatingComponent {
  rating: InputSignal<number> = input.required<number>();

  getStarClass(i: number, rating: number): string {
    const decimalPart = rating - Math.floor(rating);

    if (i > rating) {
      return (i - 1) <= rating && decimalPart ? "fa-solid fa-star-half-stroke" : "fa-regular fa-star";
    }

    return "fa fa-star";
  }
}
