import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { ChangePasswordRequest } from '../../core/models/change-password-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  changePasswordRequest: ChangePasswordRequest = { correoElectronico: '', currentPassword: '', newPassword: '' };
  errorMessage: string = '';
  successMessage: string = '';
  missingFields: string[] = [];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    // Obtener el correo electrónico almacenado desde el login
    const email = localStorage.getItem('changePasswordEmail');
    if (email) {
      this.changePasswordRequest.correoElectronico = email;
    }
  }

  onSubmit() {
    this.missingFields = [];
    this.errorMessage = '';
    this.successMessage = '';

    // Validar campos vacíos
    if (!this.changePasswordRequest.correoElectronico) {
      this.missingFields.push('Correo Electrónico');
    }
    if (!this.changePasswordRequest.currentPassword) {
      this.missingFields.push('Contraseña Actual');
    }
    if (!this.changePasswordRequest.newPassword) {
      this.missingFields.push('Nueva Contraseña');
    }

    if (this.missingFields.length > 0) {
      this.errorMessage = `Por favor, complete los siguientes campos: ${this.missingFields.join(', ')}.`;
      return;
    }

    this.authService.changePassword(this.changePasswordRequest).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.mensaje;
          this.errorMessage = '';
          // Limpiar el correo almacenado
          localStorage.removeItem('changePasswordEmail');
          setTimeout(() => this.router.navigate(['/login']), 2000);
        } else {
          this.errorMessage = response.mensaje;
          this.successMessage = '';
        }
      },
      error: () => {
        this.errorMessage = 'Error en el servidor';
        this.successMessage = '';
      }
    });
  }
}
