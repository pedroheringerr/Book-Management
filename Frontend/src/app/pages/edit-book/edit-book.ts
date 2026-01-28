import { Component, inject, OnInit, signal } from '@angular/core';
import { BookModel } from '../../models/book.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../../services/book';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Toast } from '../../shared/toast';

@Component({
  selector: 'app-edit-book',
  imports: [ReactiveFormsModule],
  templateUrl: './edit-book.html',
  styleUrl: './edit-book.scss',
})
export class EditBook implements OnInit {
  private route = inject(ActivatedRoute);
  private bookService = inject(Book);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  private toast = inject(Toast);
  private currentYear: number = new Date().getFullYear();

  book = signal<BookModel | null>(null);
  isEdit = signal(false);

  bookForm: FormGroup = this.formBuilder.group({
    title: ['', [Validators.required]],
    author: ['', [Validators.required]],
    genre: ['', [Validators.required]],
    yearPublished: ['', [Validators.required, Validators.max(this.currentYear)]],
    isbn: ['', [Validators.required, Validators.pattern('\\d{13}$')]],
  });

  ngOnInit(): void {
    const isbn = this.route.snapshot.paramMap.get('isbn');

    if (isbn) {
      this.isEdit.set(true);
      this.loadBook(isbn);
    }
  }

  loadBook(isbn: string) {
    this.bookService.getBookByIsbn(isbn).subscribe((book) => {
      this.book.set(book);

      this.bookForm.patchValue({
        title: book.title,
        author: book.author,
        genre: book.genre,
        yearPublished: book.yearPublished,
        isbn: book.isbn,
      });
    });
  }

  submit(): void {
    if (this.bookForm.valid) {
      const book = this.bookForm.value;
      if (this.isEdit()) {
        // PUT update book
        const isbn = this.bookForm.get('isbn')?.value;
        this.bookService.editBook(isbn, book).subscribe({
          next: (res: any) => {
            console.log(res);
            this.toast.show('Book updated successfully', 'success');
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.toast.show(`Failed to update Book with ISBN: ${isbn}`, 'danger');
            console.error('Update Failed: ', err);
          },
        });
      } else {
        // POST create book
        this.bookService.addBook(book).subscribe({
          next: (res: any) => {
            console.log(res);
            this.toast.show('Book added successfully', 'success');
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.toast.show('Failed to add book', 'danger');
            console.error('Failed to add Book: ', err);
          },
        });
      }
    } else {
      this.toast.show('Form is invalid. Please check fields.', 'warning');
    }
  }
}
