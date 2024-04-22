import { Injectable } from '@angular/core';
import { GenericService } from '../generic.service';
import { ClientDto } from '../../model/client/client-dto';
import { ClientRequest } from '../../model/client/client-request';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../model/PageResponse';

@Injectable({
  providedIn: 'root'
})
export class ClientService extends GenericService<ClientDto, ClientRequest, number> {

  constructor(_http: HttpClient) {
    super();
    this.context = 'client';
    this.http = _http;
  }

  override index(search: string, pageable: any): Observable<PageResponse<ClientDto>> {
    pageable.sort = pageable.sort || 'clientID,DESC';
    return super.index(search, pageable);
  }
}
