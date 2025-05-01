import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { EmailService, ContactFormData } from '../services/email.service';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent {
  contactForm: FormGroup;
  submitted = false;
  success = false;
  error = false;
  errorMessage = '';

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly emailService: EmailService
  ) {
    this.contactForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: [''],
      companyName: [''],
      message: ['', Validators.required]
    });
  }

  get f() { return this.contactForm.controls; }

  onSubmit() {
    this.submitted = true;
    this.success = false;
    this.error = false;

    // Stop if the form is invalid
    if (this.contactForm.invalid) {
      return;
    }

    const formData: ContactFormData = this.contactForm.value;

    this.emailService.sendContactEmail(formData).subscribe({
      next: () => {
        this.success = true;
        this.submitted = false;
        this.contactForm.reset();
      },
      error: (error) => {
        this.error = true;
        this.errorMessage = error.message ?? 'An error occurred while submitting the form. Please try again.';
      }
    });
  }
}
