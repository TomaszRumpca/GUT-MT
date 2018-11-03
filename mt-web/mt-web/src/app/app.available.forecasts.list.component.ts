import {Component} from '@angular/core';

@Component({
  selector: 'app-available-forecasts-list',
  template: `
    <ul>
      <li *ngFor="let windForecast of windForecasts">
        <a ng-click="selectedWindForecast(windForecast)">{{windForecast.day}} {{windForecast.month}} {{windForecast.year}}</a>
      </li>
    </ul>
  `,
  styles: []
})
export class AppAvailableForecastsListComponent {

  windForecasts = [
    {
      year: 2018,
      month: 9,
      day: 28
    }, {
      year: 2018,
      month: 9,
      day: 29
    }
  ];

}
