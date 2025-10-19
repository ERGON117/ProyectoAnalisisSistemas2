// src/app/core/models/change-password-request.model.ts
export interface ChangePasswordRequest {
  correoElectronico: string;
  currentPassword: string;
  newPassword: string;
}
