import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private _http: HttpClient) {}

  // add User
  registerUser(user: any) {
    return this._http.post(`${baseUrl}/user/addUser`, user);
  }

  // change password
  changePassword(id: any, passwordData: any) {
    return this._http.post(
      `${baseUrl}/user/change-password/${id}`,
      passwordData
    );
  }

  // update profile
  updateProfile(id: any, userDto: any) {
    return this._http.put(`${baseUrl}/user/update-user/${id}`, userDto);
  }

  // forgotPassword
  public forgotPassword(email: any) {
    return this._http.post(`${baseUrl}/user/forgot-password`, email);
  }

  // reset password
  public resetPassword(token: string, passwordData: any) {
    let queryParam = new HttpParams().set('token', token);
    return this._http.post(`${baseUrl}/user/reset-password`, passwordData, {
      params: queryParam,
    });
  }

  // verifyOtp
  verifyOtp(verificationData: any) {
    return this._http.post(`${baseUrl}/auth/verifyOtp`, verificationData);
  }

  // get Qr for Otp Login
  getQrForOtp(id: any) {
    return this._http.get(`${baseUrl}/auth/generateQr/${id}`);
  }
}
