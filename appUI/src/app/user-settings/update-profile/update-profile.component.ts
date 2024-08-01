import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrl: './update-profile.component.css',
})
export class UpdateProfileComponent implements OnInit {
  myForm!: FormGroup;
  id: any;
  user: any;

  constructor(
    private route: ActivatedRoute,
    private loginService: LoginService,
    private fb: FormBuilder,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.user = this.loginService.getUser();

    // validate form
    this.myForm = this.fb.group({
      username: [this.user.username, Validators.required],
      email: [this.user.email, Validators.required],
    });
  }

  updateProfile(form: FormGroup) {
    this.userService.updateProfile(this.id, form.value).subscribe(
      (res: any) => {
        alert(res.message);
        console.log(res);
        this.loginService.setUserInLocalStorage(res.data);
      },
      (err) => {
        alert('Error');
        console.log(err);
      }
    );
  }
}
