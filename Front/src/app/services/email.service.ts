import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map } from 'rxjs';

export interface ContactFormData {
  firstName: string;
  lastName: string;
  email: string;
  message: string;
  phoneNumber?: string;
  companyName?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private readonly apiUrl = 'http://localhost:8090/email';

  constructor(private readonly http: HttpClient) { }

  /**
   * Send email with contact form data
   * Makes an HTTP POST request to the backend API
   */
  sendContactEmail(formData: ContactFormData): Observable<boolean> {
    console.log('Sending email with form data:', formData);

    // Map description to message as expected by the backend
    const backendFormData = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      message: formData.message,
      phoneNumber: formData.phoneNumber,
      companyName: formData.companyName
    };

    return this.http.post(this.apiUrl, backendFormData).pipe(
      map(() => true),
      catchError(error => {
        console.error('Error sending email:', error);
        throw new Error('Failed to send email. Please try again later.');
      })
    );
  }
}
