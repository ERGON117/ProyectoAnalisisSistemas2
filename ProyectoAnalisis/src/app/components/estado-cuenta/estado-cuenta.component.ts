import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';

@Component({
  selector: 'app-estado-cuenta',
  templateUrl: './estado-cuenta.component.html',
  styleUrls: ['./estado-cuenta.component.css']
})
export class EstadoCuentaComponent implements OnInit {

  opcion: OpcionItem | null = null;
  errorMessage: string = '';
  estadoCuenta: any = null;

  tipoBusqueda: string = 'idPersona';

  filtros = {
    idPersona: null,
    idSaldoCuenta: null,
    nombre: '',
    apellido: '',
    inicio: '',
    fin: ''
  };

  constructor(private http: HttpClient, private menuService: MenuService) {}

  ngOnInit(): void {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'estado-cuenta');
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

  private buildParams(): HttpParams | null {
    let params = new HttpParams()
      .set('inicio', this.filtros.inicio)
      .set('fin', this.filtros.fin);

    if (this.tipoBusqueda === 'idPersona' && this.filtros.idPersona) {
      params = params.set('idPersona', this.filtros.idPersona);
    } else if (this.tipoBusqueda === 'idSaldoCuenta' && this.filtros.idSaldoCuenta) {
      params = params.set('idSaldoCuenta', this.filtros.idSaldoCuenta);
    } else if (this.tipoBusqueda === 'nombreApellido' && this.filtros.nombre && this.filtros.apellido) {
      params = params.set('nombre', this.filtros.nombre).set('apellido', this.filtros.apellido);
    } else {
      this.errorMessage = 'Por favor completa los campos requeridos según el tipo de búsqueda seleccionado.';
      return null;
    }

    return params;
  }

  consultarEstadoCuenta() {
    const url = `${environment.apiUrl}/api/estado-cuenta/consulta`;
    const params = this.buildParams();
    if (!params) return;

    this.http.get<any>(url, { params, headers: this.getAuthHeaders() }).subscribe({
      next: data => {
        this.estadoCuenta = data;
        this.errorMessage = '';
      },
      error: err => {
        this.errorMessage = 'Error al consultar estado de cuenta: ' + (err.error?.message || err.message);
        console.error('Error:', err);
      }
    });
  }

  exportarPdf() {
    const params = this.buildParams();
    if (!params) return;

    const url = `${environment.apiUrl}/api/estado-cuenta/export/pdf`;
    this.http.get(url, { params, headers: this.getAuthHeaders(), responseType: 'blob' })
      .subscribe(blob => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = 'estado_cuenta.pdf';
        link.click();
      }, err => {
        console.error('Error al exportar PDF:', err);
      });
  }

  exportarExcel() {
    const params = this.buildParams();
    if (!params) return;

    const url = `${environment.apiUrl}/api/estado-cuenta/export/excel`;
    this.http.get(url, { params, headers: this.getAuthHeaders(), responseType: 'blob' })
      .subscribe(blob => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = 'estado_cuenta.xlsx';
        link.click();
      }, err => {
        console.error('Error al exportar Excel:', err);
      });
  }

  limpiar() {
    this.filtros = {
      idPersona: null,
      idSaldoCuenta: null,
      nombre: '',
      apellido: '',
      inicio: '',
      fin: ''
    };
    this.estadoCuenta = null;
    this.errorMessage = '';
  }

  print() {
    window.print();
  }
}
