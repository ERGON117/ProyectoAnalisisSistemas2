// src/app/components/change-password/change-password.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { ChangePasswordRequest } from '../../core/models/change-password-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  changePasswordRequest: ChangePasswordRequest = { currentPassword: '', newPassword: '' };
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.changePassword(this.changePasswordRequest).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.mensaje;
          this.errorMessage = '';
          setTimeout(() => this.router.navigate(['/profile']), 2000);
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
