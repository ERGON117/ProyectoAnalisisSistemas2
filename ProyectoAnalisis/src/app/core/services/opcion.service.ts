// src/app/services/opcion.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Opcion } from '../models/opcion.model';

@Injectable({
  providedIn: 'root'
})
export class OpcionService {
  private apiUrl = 'http://localhost:8080/api/opciones'; // Ajusta según tu controlador de opciones

  constructor(private http: HttpClient) {}

  getOpciones(): Observable<Opcion[]> {
    return this.http.get<Opcion[]>(this.apiUrl);
  }
}
