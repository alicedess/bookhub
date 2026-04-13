import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Header } from '../../layout/header/header';

@Component({
  selector: 'app-livre-details',
  imports: [
    Header
  ],
  templateUrl: './livre-details.html',
  styleUrl: './livre-details.css',
})
export class LivreDetails implements OnInit {
  private route = inject(ActivatedRoute);

  id = 0;

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
    });
  }
}
