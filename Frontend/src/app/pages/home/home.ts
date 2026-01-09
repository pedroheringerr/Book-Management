import { Component, computed, effect, inject, OnInit, signal, ViewChild } from '@angular/core';
import { Book } from '../../services/book'; import { BookModel } from '../../models/book.interface';
import { Auth } from '../../services/auth';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Toast } from '../../shared/toast';
import { ConfirmModal } from '../../shared/confirm-modal/confirm-modal';
import { Confirm } from '../../shared/confirm';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {

  constructor() {
    effect(() => {
      const { genre, author, yearPublished } = this.filters();
      const search = this.search();
      const page = this.currentPage;

      this.loadBooks(page, genre, author, yearPublished, search);
    });
  }

  confirm = inject(Confirm);
  bookService = inject(Book);
  auth = inject(Auth);
  router = inject(Router);
  route = inject(ActivatedRoute);
  toast = inject(Toast);

  books = signal<BookModel[]>([]);
  currentPage: number = 0;
  booksPerPage: number = 9;
  totalPages: number = 1;
  totalBooks: number = 0;

  filters = signal<BookFilters>({
    genre: 'All',
    author: 'All',
    yearPublished: 'All'
  })

  search = signal<string>('');

  onSelectGenre(event: Event) {
    this.updateFilters(({ genre: (event.target as HTMLSelectElement).value}))
  }

  onSelectAuthor(event: Event) {
    this.updateFilters(({ author: (event.target as HTMLSelectElement).value}))
  }

  onSelectYear(event: Event) {
    this.updateFilters(({ yearPublished: (event.target as HTMLSelectElement).value}))
  }

  allGenres = computed(() => {
    const genres = new Set<string>();
    genres.add('All');
    for (const book of this.books()) {
      if (book.genre) {
        genres.add(book.genre);
      }
    }

    return Array.from(genres);
  });

  allAuthors = computed(() => {
    const authors = new Set<string>();
    authors.add('All');
    for (const book of this.books()) {
      if (book.author) {
        authors.add(book.author); }
    }
    return Array.from(authors);
  });

  allYears = computed(() => {
    const years = new Set<string>();
    years.add('All');
    for (const book of this.books()) {
      if (book.yearPublished) {
        years.add(book.yearPublished.toString());
      }
    }
    return Array.from(years);
  });

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.filters.set({
        genre: params['genre'] ?? 'All',
        author: params['author'] ?? 'All',
        yearPublished: params['yearPublished'] ?? 'All',
      });

      this.search.set(params['search'] ?? null);

      this.currentPage = Number(params['page'] ?? 0);
    });
  }

  updateFilters(partial: Partial<BookFilters>, search?: string) {
    const qp: any = {
      ...this.filters(),
      ...partial,
      page: 0
    };

    if (search && search.trim().length > 0) {
      qp.search = search;
    } else {
      qp.search = null;
    }

    this.router.navigate([], {
      queryParams: qp,
      queryParamsHandling: 'merge',
    });
  }

  loadBooks(page: number, genre: string, author: string, yearPublished: string, search: any) {
    this.bookService
    .getBooksFromApi(
      page,
      this.booksPerPage,
      genre,
      author,
      yearPublished,
      search
    )
    .subscribe((response: any) => {
      this.books.set(response.content);
      this.totalPages = response.totalPages;
      this.currentPage = response.number;
      this.totalBooks = response.totalElements;
    })
  }


  async delete(isbn: string, title: string) {
    const ok = await this.confirm.open(
      'Delete book',
      `Are you sure you want to delete "${title}"?`
    );

    if (!ok) return;

    this.bookService.deleteBook(isbn).subscribe({
      next: () => {
        this.toast.show(`Book "${title}" deleted`, 'success');
        this.books.set(this.books().filter(b => b.isbn !== isbn));
      },
      error: (err) => {
        this.toast.show(`Failed to delete "${title}"`, 'danger');
        console.error(err);
      }
    });
  }

  edit(book: BookModel) {
    this.router.navigate(['/books/edit', book.isbn]);
  }

  goToPage(page: number): void {
    if (page >= 0 && page <= this.totalPages) {
      this.router.navigate([], {
        queryParams: { page },
        queryParamsHandling: 'merge',
      });
    } else {
      console.warn('Invalid Page number');
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.goToPage(this.currentPage + 1);
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }

  searchResults(search: string) {
    this.search.set(search);
  }

}

type BookFilters = {
  genre: string;
  author: string;
  yearPublished: string;
}
