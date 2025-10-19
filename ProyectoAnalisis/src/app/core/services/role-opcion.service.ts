// src/app/services/role-opcion.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleOpcionDTO } from '../models/role-opcion.dto';
import { RoleOpcion } from '../models/role-opcion.model';
import { map } from 'rxjs/operators'; // Para mapear respuesta si es necesario

export interface ApiResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class RoleOpcionService {
  private apiUrl = 'http://localhost:8080/api/role-opcion';

  constructor(private http: HttpClient) {}

  asignarOpciones(idRole: number, opciones: RoleOpcionDTO[]): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/asignar/${idRole}`, opciones);
  }

  obtenerOpcionesPorRol(idRole: number): Observable<RoleOpcion[]> {
    return this.http.get<RoleOpcion[]>(`${this.apiUrl}/rol/${idRole}`);
  }
}
