// src/app/components/menu/menu.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse } from '../../core/models/menu-response.model';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  menus: MenuResponse[] = [];

  constructor(
    private menuService: MenuService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe({
      next: (menus) => {
        this.menus = menus;
        console.log('Menus loaded:', menus);
      },
      error: (error) => {
        console.error('Error loading menu:', error);
      }
    });
  }

  navigateToProfile() {
    const url = window.location.origin + '/profile';
    window.open(url, '_blank');
  }

  navigateTo(pagina: string) {
    this.router.navigate([pagina]);
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      }
    });
  }
}
