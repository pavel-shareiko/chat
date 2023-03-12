import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "./layouts/login/login.component";
import { RegistrationComponent } from "./layouts/registration/registration.component";
import { NotFoundComponent } from "./not-found/not-found.component";

const routes: Routes = [
  { path: "login", component: LoginComponent },
  { path: "register", component: RegistrationComponent },
  { path: "404", component: NotFoundComponent },
  { path: "**", redirectTo: "404" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
