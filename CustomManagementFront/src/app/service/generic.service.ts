import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PageResponse } from '../model/PageResponse';
/**
 * @param <D>  Dto
 * @param <R>  Request
 * @param <ID> Id type in entity
 */
export class GenericService<D, R, ID> {

    private uri = `${location.protocol}//${location.hostname}${location.port ? `:${location.port}` : ''}`;
    // private uri = 'http://localhost:8081';
    protected context!: string;
    protected http!: HttpClient;

    constructor() { }

    index(search: string, pageable: any): Observable<PageResponse<D>> {
        const params = new HttpParams()
        .set('search', search)
        .set('page', pageable.pageNumber.toString())
        .set('size', pageable.pageSize.toString())
        .set('sort', pageable.sort ? pageable.sort : '');
  
      return this.http.get<PageResponse<D>>(`${this.uri}/${this.context}`, { params });
    }

    show(id: ID): Observable<D> {
        return this.http.get<D>(`${this.uri}/${this.context}/${id}`);
    }

    create(request: R): Observable<D> {
        return this.http.post<D>(`${this.uri}/${this.context}`, request);
    }

    update(codigo: ID, request: R): Observable<D> {
        return this.http.patch<D>(`${this.uri}/${this.context}/${codigo}`, request);
    }

    delete(id: ID): Observable<void> {
        return this.http.delete<void>(`${this.uri}/${this.context}/${id}`);
    }
}
