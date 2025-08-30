// src/app/core/services/menu.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MenuResponse } from '../models/menu-response.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMenuStructure(): Observable<MenuResponse[]> {
    return this.http.get<MenuResponse[]>(`${this.apiUrl}/auth/menu`);
  }
}
