// src/app/components/option/option.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-option',
  templateUrl: './option.component.html',
  styleUrls: ['./option.component.css']
})
export class OptionComponent implements OnInit {
  option: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.option = this.route.snapshot.paramMap.get('option') || '';
  }
}
