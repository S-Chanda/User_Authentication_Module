import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from './helper';
import { publishFacade } from '@angular/compiler';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  public loginStatusSubject = new Subject<boolean>();

  constructor(private _http: HttpClient) {}

  // get Current User
  public getCurrentUser() {
    return this._http.get(`${baseUrl}/user/current-user`);
  }

  // generate token
  public generateToken(loginData: any) {
    return this._http.post(`${baseUrl}/auth/login`, loginData);
  }

  // log in User
  public loginUser(token: any) {
    localStorage.setItem('token', token);
    return true;
  }

  // is User LoggedIn ?
  public isLoggedIn() {
    let tokenStr = localStorage.getItem('token');
    if (!tokenStr) {
      return false;
    } else {
      return true;
    }
  }

  // get Token
  public getToken() {
    return localStorage.getItem('token');
  }

  // logout
  public logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    return true;
  }

  // set User Detail
  public setUserInLocalStorage(user: any) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  // get Current User
  public getUser() {
    let userStr = localStorage.getItem('user');
    if (userStr) {
      return JSON.parse(userStr);
    } else {
      this.logout();
      return null;
    }
  }

  // getUserRole
  public getUserRole() {
    let user = this.getUser();
    return user.authorities[0].authority;
  }

  // get UserId
  public getUserId() {
    let user = this.getUser();
    return user.userID;
  }
}
