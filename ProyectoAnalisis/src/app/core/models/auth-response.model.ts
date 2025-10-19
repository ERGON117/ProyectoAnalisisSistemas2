// src/app/core/models/auth-response.model.ts
export interface AuthResponse {
  success: boolean;
  mensaje: string;
  token?: string;
  requiereCambioPassword?: boolean;

}
