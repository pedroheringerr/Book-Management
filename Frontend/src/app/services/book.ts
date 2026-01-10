import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BookModel } from '../models/book.interface';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Book {

  http = inject(HttpClient);
  url = `${environment.apiUrl}/api/books`;

  getAllBooks() {
    return this.http.get<BookModel[]>(`${this.url}/stats/all`);
  }

  getBooksFromApi(page: number, size: number, genre: string, author: string, year: string, search: string | null) {
    let params: any = { page, size };

    if (genre && genre !== 'All') params.genre = genre;
    if (author && author !== 'All') params.author = author;
    if (year && year !== 'All') {
      const parsedYear = parseInt(year);
      if (!isNaN(parsedYear)) {
        params.yearPublished = parsedYear;
      }
    }
    if (search && search.trim().length > 0) params.search = search.trim();

    return this.http.get<BookModel[]>(this.url, { params });
  }

  getBookByIsbn(isbn: string) {
    return this.http.get<BookModel>(`${this.url}/${isbn}`);
  }

  deleteBook(isbn: string): Observable<any> {
    return this.http.delete(`${this.url}/${isbn}`);
  }

  editBook(isbn: any, book: BookModel): Observable<any> {
    return this.http.put(`${this.url}/${isbn}`, book);
  }

  addBook(book: BookModel): Observable<any> {
    return this.http.post(`${this.url}`, book);
  }
}
