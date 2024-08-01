import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  user: any = null;

  constructor(public loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    // asObservable converts the subject to an observable, which allows other parts of the code to subscribe to it and react to changes
    // .subscribe sets subscription to the observable 
    // when subjectBehaviour emits a new value, callback function is executed
    this.loginService.loginStatusSubject.asObservable().subscribe((data) => {
      this.isLoggedIn = this.loginService.isLoggedIn();
      this.user = this.loginService.getUser();
    });
  }

  

  logout() {
    this.loginService.logout();
    this.router.navigate(['login']);
    this.loginService.loginStatusSubject.next(false);
  }
}
