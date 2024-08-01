import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Route, Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css',
})
export class ResetPasswordComponent {
  myForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.myForm = this.fb.group({
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }

  resetPassword() {
    let token: any;

    // getting token from URL
    this.route.queryParams.subscribe((params) => {
      token = params['token'];
      console.log(token);
    });

    // resetting password
    this.userService.resetPassword(token, this.myForm.value).subscribe(
      (res: any) => {
        alert(res.message);
        console.log(res);
        this.router.navigate(['login']);
      },
      (err) => {
        alert('Error');
        console.log(err);
      }
    );
  }
}
