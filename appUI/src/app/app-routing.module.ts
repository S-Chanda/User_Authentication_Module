import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ProfileComponent } from './user-settings/profile/profile.component';
import { ChangePasswordComponent } from './user-settings/change-password/change-password.component';
import { UpdateProfileComponent } from './user-settings/update-profile/update-profile.component';
import { HomeComponent } from './pages/home/home.component';
import { ClientHomeComponent } from './client/client-home/client-home.component';
import { clientGuard } from './guards/client.guard';
import { ForgotPasswordComponent } from './user-settings/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './user-settings/reset-password/reset-password.component';
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';

const routes: Routes = [
  {
    path:'',
    component:HomeComponent,
    pathMatch:'full'

  },
  {
    path:'login',
    component:LoginComponent
  },
  {
    path:'signup',
    component: SignupComponent
  },

  {
    path:'client',
    component:ClientDashboardComponent,
    canActivate:[clientGuard],
    children:[
      {
        path:'client-home',
        component:ClientHomeComponent
      },
      {
        path:'profile/:id',
        component:ProfileComponent
      },
      {
        path:'change-password/:id',
        component:ChangePasswordComponent
      },
      {
        path:'update-profile/:id',
        component:UpdateProfileComponent
      },

    ]
  },
  
  {
    path:'forgot-password',
    component:ForgotPasswordComponent
  },
  {
    path:'reset-password', // no need to mention query parameter here
    component:ResetPasswordComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
