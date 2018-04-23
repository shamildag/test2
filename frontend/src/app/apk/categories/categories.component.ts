import {Component, OnInit} from '@angular/core';
import {CATEGORIES} from '../categories.properties';
import {MenuItem} from 'primeng/primeng';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  private categories: MenuItem[];

  constructor() {
  }

  ngOnInit() {
    this.categories = CATEGORIES;
  }

}
