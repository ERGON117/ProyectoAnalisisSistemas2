// src/app/components/reset-password/reset-password.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { PasswordResetRequest } from '../../core/models/password-reset-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  resetRequest: PasswordResetRequest = {
    correoElectronico: '',
    respuesta: '',
    newPassword: ''
  };
  securityQuestion: string | null = null;
  errorMessage: string = '';
  missingFields: string[] = [];
  successMessage: string = '';
  showResetForm: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  getSecurityQuestion() {
    this.missingFields = [];
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.resetRequest.correoElectronico) {
      this.errorMessage = 'Por favor, ingrese el correo electrónico.';
      return;
    }

    this.authService.getSecurityQuestion(this.resetRequest.correoElectronico).subscribe({
      next: (response) => {
        if (response.success) {
          this.securityQuestion = response.mensaje;
          this.showResetForm = true;
        } else {
          this.errorMessage = response.mensaje || 'Error al obtener la pregunta de seguridad.';
          this.showResetForm = false;
        }
      },
      error: () => {
        this.errorMessage = 'Error en el servidor.';
        this.showResetForm = false;
      }
    });
  }

  onSubmit() {
    this.missingFields = [];
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.resetRequest.respuesta) {
      this.missingFields.push('Respuesta');
    }
    if (!this.resetRequest.newPassword) {
      this.missingFields.push('Nueva Contraseña');
    }

    if (this.missingFields.length > 0) {
      this.errorMessage = `Por favor, complete los siguientes campos: ${this.missingFields.join(', ')}.`;
      return;
    }

    this.authService.resetPassword(this.resetRequest).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.mensaje || 'Contraseña restablecida correctamente.';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        } else {
          this.errorMessage = response.mensaje || 'Error al restablecer la contraseña.';
        }
      },
      error: () => {
        this.errorMessage = 'Error en el servidor.';
      }
    });
  }
}
