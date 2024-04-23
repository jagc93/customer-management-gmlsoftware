import { Injectable } from '@angular/core';
import * as Papa from 'papaparse';

@Injectable({
  providedIn: 'root'
})
export class ExportService {

  constructor() { }

  public exportToCsv(data: any[], filename: string): void {
    const csv = Papa.unparse(data);
    const blob = new Blob([csv], { type: 'text/csv' });

    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.setAttribute('download', filename + '.csv');
    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    window.URL.revokeObjectURL(link.href);
  }
}
