import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { CommonModule } from '@angular/common';

interface SaldoConsultaDTO {
  idSaldoCuenta: number;
  idPersona: number;
  nombrePersona: string;
  apellidoPersona: string;
  idTipoSaldoCuenta: number;
  nombreTipo: string;
  idStatusCuenta: number;
  nombreStatus: string;
  saldoInicial: number;
  cargos: number;
  abonos: number;
  saldoFinal: number;
}

@Component({
  selector: 'app-consulta-saldos',
  templateUrl: './consulta-saldos.component.html',
  styleUrls: ['./consulta-saldos.component.css'],

})
export class ConsultaSaldosComponent implements OnInit {
  opcion: OpcionItem | null = null;
  saldos: SaldoConsultaDTO[] = [];
  errorMessage: string = '';
  loading: boolean = false;

  // Tipo de consulta seleccionado
  tipoConsulta: 'idPersona' | 'idSaldoCuenta' | 'nombreApellido' = 'idPersona';

  // Formulario dinámico según tipo de consulta
  searchForm = {
    idPersona: '',
    idSaldoCuenta: '',
    nombre: '',
    apellido: ''
  };

  // Opciones del combobox
  opcionesConsulta = [
    { value: 'idPersona', label: 'Por ID de Persona' },
    { value: 'idSaldoCuenta', label: 'Por ID de Saldo Cuenta' },
    { value: 'nombreApellido', label: 'Por Nombre y Apellido' }
  ];

  // Propiedad computada para el label del tipo de consulta
  get labelTipoConsulta(): string {
    const opcion = this.opcionesConsulta.find(op => op.value === this.tipoConsulta);
    return opcion ? opcion.label : '';
  }

  constructor(private menuService: MenuService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'consulta-saldos');
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

  onTipoConsultaChange() {
    this.resetForm();
  }

  private resetForm() {
    this.searchForm = {
      idPersona: '',
      idSaldoCuenta: '',
      nombre: '',
      apellido: ''
    };
    this.saldos = [];
    this.errorMessage = '';
  }

  buscarSaldos() {
    this.loading = true;
    this.errorMessage = '';
    this.saldos = [];

    // Validar según tipo de consulta
    let params: any = {};
    let isValid = true;

    switch (this.tipoConsulta) {
      case 'idPersona':
        if (!this.searchForm.idPersona || isNaN(parseInt(this.searchForm.idPersona))) {
          this.errorMessage = 'Debe ingresar un ID de Persona válido';
          isValid = false;
        } else {
          params.idPersona = parseInt(this.searchForm.idPersona);
        }
        break;

      case 'idSaldoCuenta':
        if (!this.searchForm.idSaldoCuenta || isNaN(parseInt(this.searchForm.idSaldoCuenta))) {
          this.errorMessage = 'Debe ingresar un ID de Saldo Cuenta válido';
          isValid = false;
        } else {
          params.idSaldoCuenta = parseInt(this.searchForm.idSaldoCuenta);
        }
        break;

      case 'nombreApellido':
        if (!this.searchForm.nombre?.trim() || !this.searchForm.apellido?.trim()) {
          this.errorMessage = 'Debe ingresar tanto nombre como apellido';
          isValid = false;
        } else {
          params.nombre = this.searchForm.nombre.trim();
          params.apellido = this.searchForm.apellido.trim();
        }
        break;
    }

    if (!isValid) {
      this.loading = false;
      return;
    }

    const url = `${environment.apiUrl}/api/saldos/consulta`;

    this.http.get<SaldoConsultaDTO[]>(url, {
      params,
      headers: this.getAuthHeaders(),
      observe: 'response'
    }).subscribe({
      next: (response) => {
        this.saldos = response.body || [];
        if (this.saldos.length === 0) {
          this.errorMessage = 'No se encontraron resultados con los criterios especificados';
        }
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error al consultar saldos: ' + (error.error?.message || error.message);
        console.error('Error:', error);
        this.loading = false;
      }
    });
  }

  limpiarBusqueda() {
    this.resetForm();
    this.tipoConsulta = 'idPersona'; // Reset al primer tipo
  }

  print() {
    window.print();
  }

  exportData() {
    if (this.saldos.length === 0) {
      this.errorMessage = 'No hay datos para exportar';
      return;
    }

    const tipoConsultaText = this.labelTipoConsulta;
    const headers = 'Tipo de Consulta,ID Saldo Cuenta,ID Persona,Nombre,Apellido,Tipo Saldo,Status,Saldo Inicial,Cargos,Abonos,Saldo Final\n';
    const csvRows = this.saldos.map(saldo =>
      `"${tipoConsultaText}",${saldo.idSaldoCuenta},${saldo.idPersona},"${saldo.nombrePersona}","${saldo.apellidoPersona}","${saldo.nombreTipo}","${saldo.nombreStatus}",${saldo.saldoInicial},${saldo.cargos},${saldo.abonos},${saldo.saldoFinal}`
    ).join('\n');

    const csv = headers + csvRows;
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', `saldos_${this.tipoConsulta}_${new Date().toISOString().split('T')[0]}.csv`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 2
    }).format(value || 0);
  }

  getTotalSaldoFinal(): number {
    return this.saldos.reduce((total, saldo) => total + (saldo.saldoFinal || 0), 0);
  }

  getVisibleFields(): string[] {
    switch (this.tipoConsulta) {
      case 'idPersona': return ['idPersona'];
      case 'idSaldoCuenta': return ['idSaldoCuenta'];
      case 'nombreApellido': return ['nombre', 'apellido'];
      default: return [];
    }
  }

  // Método helper para obtener el label en el template
  getTipoConsultaLabel(): string {
    return this.labelTipoConsulta;
  }
}
