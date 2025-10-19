import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';

@Component({
  selector: 'app-cierre-mes',
  templateUrl: './cierre-mes.component.html',
  styleUrls: ['./cierre-mes.component.css']
})
export class CierreMesComponent implements OnInit {

  opcion: OpcionItem | null = null;
  formItem = { anio: new Date().getFullYear(), mes: new Date().getMonth() + 1 };
  mensaje: string = '';
  errorMessage: string = '';
  isProcessing: boolean = false;

  constructor(private menuService: MenuService, private http: HttpClient) {}

  ngOnInit(): void {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'cierre-mes');
    });
  }

  private findOpcion(menus: MenuResponse[], pagina: string): OpcionItem | null {
    for (const menu of menus) {
      for (const menuItem of menu.menuItems) {
        for (const opcion of menuItem.opciones) {
          if (opcion.pagina === pagina) return opcion;
        }
      }
    }
    return null;
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token || ''}`,
      'Content-Type': 'application/json'
    });
  }

  cerrarMes(): void {
    this.isProcessing = true;
    this.errorMessage = '';
    this.mensaje = '';

    const url = `${environment.apiUrl}/api/cierre-mes/cerrar`;
    const body = { anio: this.formItem.anio, mes: this.formItem.mes };

    this.http.post<any>(url, body, { headers: this.getAuthHeaders() }).subscribe({
      next: (response) => {
        this.mensaje = response.message || 'Cierre de mes completado exitosamente.';
        this.isProcessing = false;
      },
      error: (error) => {
        this.errorMessage = 'Error al realizar el cierre: ' + (error.error?.error || error.message);
        this.isProcessing = false;
      }
    });
  }

  resetForm(): void {
    this.formItem = {
      anio: new Date().getFullYear(),
      mes: new Date().getMonth() + 1
    };
    this.mensaje = '';
    this.errorMessage = '';
  }
}
