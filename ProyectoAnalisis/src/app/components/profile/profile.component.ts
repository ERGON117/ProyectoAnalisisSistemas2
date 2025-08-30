// src/app/components/profile/profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';
import { ChangePasswordRequest } from '../../core/models/change-password-request.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  changePasswordRequest: ChangePasswordRequest = { currentPassword: '', newPassword: '' };
  errorMessage: string = '';
  successMessage: string = '';
  showPasswordForm: boolean = false;

  constructor(private authService: AuthService) {}

 // src/app/components/profile/profile.component.ts
ngOnInit() {
  this.authService.user$.subscribe(user => {
    this.user = user;
    console.log('Datos del usuario en ProfileComponent:', user); // Verifica aquí
  });
  this.authService.getProfile().subscribe(); // Fuerza la carga inicial
}

  togglePasswordForm() {
    this.showPasswordForm = !this.showPasswordForm;
    this.errorMessage = '';
    this.successMessage = '';
    this.changePasswordRequest = { currentPassword: '', newPassword: '' };
  }

  onSubmitPassword() {
    this.authService.changePassword(this.changePasswordRequest).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = response.mensaje;
          this.errorMessage = '';
          this.changePasswordRequest = { currentPassword: '', newPassword: '' };
          setTimeout(() => {
            this.showPasswordForm = false;
            this.successMessage = '';
          }, 2000);
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
