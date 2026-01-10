import { AfterViewInit, Component, inject } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Book } from '../../services/book';

Chart.register(...registerables);

@Component({
  selector: 'app-charts',
  imports: [],
  templateUrl: './charts.html',
  styleUrl: './charts.scss',
})
export class Charts implements AfterViewInit {

  private bookService: Book = inject(Book);

  ngAfterViewInit(): void {
      this.loadCharts();
  }

  loadCharts(): void {
    this.bookService.getAllBooks().subscribe(books => {
      this.createGenreChart(books);
      this.createYearChart(books);
    });
  }

  private createGenreChart(books: any[]): void {
    const genreCount: Record<string, number> = {};

    books.forEach(book => {
      const genre = book.genre || 'Unknown';
      genreCount[genre] = (genreCount[genre] || 0) + 1;
    });

    new Chart('genreChart', {
      type: 'pie',
      data: {
        labels: Object.keys(genreCount),
        datasets: [{
          data: Object.values(genreCount)
        }]
      }
    });
  }

  private createYearChart(books: any[]): void {
    const yearCount: Record<number, number> = {};

    books.forEach(book => {
      const year = book.yearPublished;
      yearCount[year] = (yearCount[year] || 0) + 1;
    });

    new Chart('yearChart', {
      type: 'bar',
      data: {
        labels: Object.keys(yearCount),
        datasets: [{
          label: 'Books',
          data: Object.values(yearCount)
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}
