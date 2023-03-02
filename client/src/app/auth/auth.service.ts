import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, shareReplay } from "rxjs";

interface User {
    username: string;
    password: string;
}

@Injectable({providedIn: 'root'})
export class AuthService {
     
    constructor(private http: HttpClient) {
    }
      
    login(username:string, password:string): Observable<User> {
        return this.http.post<User>('localhost:8080/api/v1/auth/login', {username, password});
        shareReplay();
    }

}