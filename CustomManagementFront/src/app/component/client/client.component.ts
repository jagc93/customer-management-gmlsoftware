import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { debounceTime, firstValueFrom, fromEvent, Subject, Subscription, takeUntil } from 'rxjs';
import { ClientDto } from '../../model/client/client-dto';
import { ClientService } from '../../service/client/client.service';
import { ClientRequest } from '../../model/client/client-request';
import { CharactersAllowed, CheckEmail } from '../../functions/app.function';
import { ProgressBarService } from '../../service/progress-bar/progress-bar.service';
import { AlertService } from '../../service/aler/alert.service';
import { OffcanvasService } from '../../service/offcanvas/offcanvas.service';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent implements OnInit, AfterViewInit, OnDestroy {

  public showForm = false;
  public showFormFilter = false;
  public clients: ClientDto[] = [];
  public form!: FormGroup;
  public formFilter!: FormGroup;
  public itemsPerPage = 5;
  public page = 1;
  public pageTotalItems = 0;
  public buttons = {
    isRegister: true,
    isEdit: false,
    registrationButtonIsEnabled: false,
    updateButtonIsEnabled: false
  };

  private isBtnAdvancedSearch = false;
  private subscription!: Subscription;
  private clientEdition!: ClientDto;
  private pageSubject = new Subject<void>();
  private pageObservable$ = this.pageSubject.asObservable();
  private pageSubscription!: Subscription;
  private searchSubscription!: Subscription;
  private subscriptionOffcanvas!: Subscription;
  private destroy$ = new Subject<void>();

  constructor(
    private service: ClientService,
    private progressBarService: ProgressBarService,
    private alertService: AlertService,
    private offcanvasService: OffcanvasService
  ) { }

  private async create(request: ClientRequest): Promise<void> {
    try {
      await firstValueFrom(this.service.create(request));

      setTimeout(() => {
        this.progressBarService.hide();
        this.resetFormFields();
        this.filterFields()
        this.showForm = false;
        this.alertService.showSuccess('Client successfully registered!');
      }, 300);
    } catch (e: any) {
      console.error(e);
      this.alertService.showError(e);
      this.progressBarService.hide();
    }
  }

  private async update(clientID: number, request: ClientRequest) {
    try {
      await firstValueFrom(this.service.update(clientID, request));

      setTimeout(() => {
        this.progressBarService.hide();
        this.resetFormFields();
        this.filterFields()
        this.showForm = false;
        this.alertService.showSuccess('Client successfully updated!');
      }, 300);
    } catch (e: any) {
      console.error(e);
      this.alertService.showError(e);
      this.progressBarService.hide();
    }
  }

  private filterFields() {
    if (!this.isBtnAdvancedSearch) {
      this.progressBarService.show();
    }
    const keysFormFilter = Object.keys(this.formFilter.controls);
    const fullName: any = this.processFullName(this.formFilter?.get('nameFilter')?.value);

    Object.keys(fullName).map(key => {
      const k = `${key}Filter`;
      if (Object.prototype.hasOwnProperty.call(this.formFilter.controls, k)) {
        this.formFilter.get(k)?.setValue(fullName[key]);
      }
    });

    const search = keysFormFilter
      .filter(key => key !== 'nameFilter')
      .map((key, i) => {
        const k = key.replace('Filter', '');
        const v = this.formFilter.get(key)?.value?.trim();
        const op = ['clientID', 'startDate', 'endDate'].includes(k) ? '==v' : '==*v*';
        return v ? `${k}${op.replace('v', v)};` : '';
      }).join('');
    if (!this.isBtnAdvancedSearch || this.isBtnAdvancedSearch && search.trim()) {
      if (this.isBtnAdvancedSearch) {
        this.progressBarService.show();
      }
      this.findAll(search.substring(0, search.lastIndexOf(';')), false);
    }
  }

  private async findAll(search: string = "", option: boolean = true): Promise<void> {
    try {
      const page = await this.service.index(search, { pageNumber: (this.page - 1), pageSize: 5, sort: '' }).toPromise();
      this.clients = page!.content;
      this.pageTotalItems = page!.totalElements;
      if (option) {
        this.filterForm();
      }
      setTimeout(() => {
        this.progressBarService.hide();
      }, 2000);
    } catch (e: any) {
      console.error(e);
      this.progressBarService.hide();
      this.alertService.showError(e);
    }
  }

  private changePage() {
    this.pageSubscription = this.pageObservable$
      .pipe(debounceTime(0))
      .subscribe(() => this.filterFields());
  }

  private async enableUpdateButton(): Promise<void> {
    if (!this.buttons.isEdit || !(await this.validateFields(false, false))) {
      this.buttons.updateButtonIsEnabled = false;
      return;
    }

    const keysForm = Object.keys(this.form.controls);
    for (const i in keysForm) {
      const key = keysForm[i];
      if (Object.prototype.hasOwnProperty.call(this.form.controls, key)) {
        const oldValue = (key !== 'name'
          ? (this.clientEdition as any)[key]
          : this.getFullName(this.clientEdition)) || '';
        const newValue = this.form.get(key)?.value || '';
        this.buttons.updateButtonIsEnabled = oldValue !== newValue;
        if (this.buttons.updateButtonIsEnabled) {
          break;
        }
      }
    }
  }

  private async enableRegisterButton(): Promise<void> {
    if (this.buttons.isRegister) {
      this.buttons.registrationButtonIsEnabled = await this.validateFields(false, false);
    }
  }

  private mainForm(client: ClientDto | null): void {
    this.form = new FormGroup({
      name: new FormControl(client ? this.getFullName(client) : '', Validators.required),
      phoneNumber: new FormControl(client?.phoneNumber || ''),
      emailAddress: new FormControl(client?.emailAddress || '', [Validators.required]),
      startDate: new FormControl(client?.startDate || ''),
      endDate: new FormControl(client?.endDate || '')
    });

    const fieldsToWatch = [
      'startDate',
      'endDate'
    ];

    fieldsToWatch.forEach(field => {
      this.form.get(field)?.valueChanges.subscribe(() => {
        this.enableUpdateButton();
        this.enableRegisterButton();
      });
    });

    this.form.get('name')?.valueChanges.pipe(
      debounceTime(300)
    ).subscribe(name => {
      if (name?.trim()) {
        const newName = CharactersAllowed(name, 2);
        if (newName !== name) {
          this.form.get('name')?.setValue(newName);
          return;
        }
        if (name.trim().length > 20) {
          this.form.get('name')?.setValue(name.substring(0, 202));
          return;
        }
      }
      this.enableUpdateButton();
      this.enableRegisterButton();
    })

    this.form.get('phoneNumber')?.valueChanges.pipe(
      debounceTime(300)
    ).subscribe(phoneNumber => {
      if (phoneNumber?.trim()) {
        const newPhoneNumber = CharactersAllowed(phoneNumber, 3);
        if (newPhoneNumber !== phoneNumber) {
          this.form.get('phoneNumber')?.setValue(newPhoneNumber);
          return;
        }
        if (phoneNumber.trim().length > 20) {
          this.form.get('phoneNumber')?.setValue(phoneNumber.substring(0, 20));
          return;
        }
      }
      this.enableUpdateButton();
      this.enableRegisterButton();
    });

    this.form.get('emailAddress')?.valueChanges.pipe(
      debounceTime(300)
    ).subscribe(emailAddress => {
      if (emailAddress?.trim()) {
        const newEmailAddress = CharactersAllowed(emailAddress, 1);
        if (newEmailAddress !== emailAddress) {
          this.form.get('emailAddress')?.setValue(newEmailAddress);
          return;
        }
        if (emailAddress.trim().length > 100) {
          this.form.get('emailAddress')?.setValue(emailAddress.substring(0, 100));
          return;
        }
        CheckEmail(emailAddress.trim()).then(result => {
          if (!result) {
            this.form.get('emailAddress')?.markAsTouched();
            this.form.get('emailAddress')?.setErrors({ isValidEmail: result });
          } else {
            this.form.get('emailAddress')?.markAsTouched();
            this.form.get('emailAddress')?.setErrors(null);
          }
        });
      }
      this.enableUpdateButton();
      this.enableRegisterButton();
    });
  }

  private filterForm(): void {
    this.formFilter = new FormGroup({
      sharedKeyFilter: new FormControl(''),
      nameFilter: new FormControl(''),
      firstNameFilter: new FormControl(''),
      middleNameFilter: new FormControl(''),
      lastNameFilter: new FormControl(''),
      secondLastNameFilter: new FormControl(''),
      phoneNumberFilter: new FormControl(''),
      emailAddressFilter: new FormControl(''),
      startDateFilter: new FormControl(''),
      endDateFilter: new FormControl('')
    });

    this.formFilter.get('sharedKeyFilter')?.valueChanges
      .pipe(debounceTime(300))
      .subscribe(sharedKey => {
        if (sharedKey?.trim()) {
          const newName = CharactersAllowed(sharedKey, 4);
          if (newName !== sharedKey) {
            this.formFilter.get('sharedKeyFilter')?.setValue(newName);
            return;
          }
          if (sharedKey.trim().length > 20) {
            this.formFilter.get('sharedKeyFilter')?.setValue(sharedKey.substring(0, 51));
            return;
          }
        }
      });

    this.formFilter.get('phoneNumberFilter')?.valueChanges
      .pipe(debounceTime(300))
      .subscribe(phoneNumber => {
        if (phoneNumber?.trim()) {
          const newPhoneNumber = CharactersAllowed(phoneNumber, 3);
          if (newPhoneNumber !== phoneNumber) {
            this.formFilter.get('phoneNumberFilter')?.setValue(newPhoneNumber);
            return;
          }
          if (phoneNumber.trim().length > 20) {
            this.formFilter.get('phoneNumberFilter')?.setValue(phoneNumber.substring(0, 20));
            return;
          }
        }
      });

    this.formFilter.get('emailAddressFilter')?.valueChanges
      .pipe(debounceTime(300))
      .subscribe(emailAddress => {
        if (emailAddress?.trim()) {
          const newEmailAddress = CharactersAllowed(emailAddress, 1);
          if (newEmailAddress !== emailAddress) {
            this.formFilter.get('emailAddressFilter')?.setValue(newEmailAddress);
            return;
          }
          if (emailAddress.trim().length > 100) {
            this.formFilter.get('emailAddressFilter')?.setValue(emailAddress.substring(0, 100));
            return;
          }
        }
      });
  }

  private search(): void {
    if (this.searchSubscription) {
      return;
    }

    const btnSearch = document.getElementById('search');

    if (btnSearch) {
      this.searchSubscription = fromEvent<MouseEvent>(btnSearch, 'click').pipe(
        debounceTime(300),
        takeUntil(this.destroy$)
      ).subscribe(() => {
        const v = this.formFilter.get('sharedKeyFilter')?.value?.trim();
        const search = v ? `sharedKey==*${v}*;` : '';

        if (search.trim()) {
          this.progressBarService.show();
          this.page = 1;
          this.findAll(search.substring(0, search.lastIndexOf(';')), false);
        }
      });
    }
  }

  private resize(): void {
    const renderizado$ = fromEvent(window, 'resize');

    renderizado$.subscribe(() => {
      if (this.showForm && this.form) {
        const divClientContainer = document.getElementById('client-container') as HTMLDivElement;
        const divCreateFormContainer = divClientContainer?.querySelector('#create-form-container');

        if (divCreateFormContainer) {
          (divCreateFormContainer as HTMLDivElement).style.width = `${divClientContainer.clientWidth}px`;
          (divCreateFormContainer as HTMLDivElement).style.minHeight = `${divClientContainer.clientHeight}px`;
        }
      }
    });
  }

  private validateFields(option: boolean = false, markAsTouched: boolean = true): Promise<boolean> {
    const promise = new Promise<boolean>(resolve => {

      const keysForm = Object.keys(this.form.controls);

      for (let i = 0; i < keysForm.length; i++) {
        const key = keysForm[i];
        if (Object.prototype.hasOwnProperty.call(this.form.controls, key)) {
          if (option) {
            if (this.form.get(key)?.value) {
              if (typeof this.form.get(key)!.value === 'string') {
                resolve(this.form.get(key)!.value?.trim());
              } else {
                resolve(true);
              }
            }
          } else {
            if (this.form.get(key)!.errors) {
              if (markAsTouched) {
                this.form.get(key)!.markAsTouched();
              }
              resolve(false);
            }
          }
          if (i === keysForm.length - 1) {
            resolve(!option);
          }
        } else if (i === keysForm.length - 1) {
          resolve(false);
        }
      }
    });

    return promise;
  }

  private processFullName(fullName: string) {
    if (!fullName) {
      return {
        firstName: null,
        middleName: null,
        lastName: null,
        secondLastName: null
      };
    }

    const parts = fullName.trim().split(' ');

    let firstName = '';
    let middleName = '';
    let lastName = '';
    let secondLastName = '';

    if (parts.length >= 1) {
      firstName = parts[0];
    }
    if (parts.length >= 2) {
      lastName = parts[parts.length == 2 ? 1 : 2];
    }
    if (parts.length === 3) {
      middleName = parts[1];
    }
    if (parts.length === 4) {
      middleName = parts[1];
      secondLastName = parts[3];
    }

    return {
      firstName,
      middleName,
      lastName,
      secondLastName
    };
  }

  public advancedSearch(): void {
    this.showFormFilter = true;
    setTimeout(() => {
      const btnAdvancedSearch = document.getElementById('advanced-search');

      if (btnAdvancedSearch) {
        const click$ = fromEvent<MouseEvent>(btnAdvancedSearch, 'click').pipe(
          debounceTime(300),
          takeUntil(this.destroy$)
        );

        click$.subscribe(() => {
          this.page = 1;
          this.isBtnAdvancedSearch = true;
          this.filterFields();
          this.showFormFilter = false;
          setTimeout(() => {
            this.isBtnAdvancedSearch = false;
          }, 300);
          click$.subscribe().unsubscribe();
        });
      }
    }, 300);
  }

  public getFullName(client: ClientDto): string {
    const fullNameParts = [
      client.firstName?.trim(),
      client.middleName?.trim(),
      client.lastName?.trim(),
      client.secondLastName?.trim()
    ].filter(part => !!part);

    return fullNameParts.join(' ').trim();
  }

  public select(selectedClient: ClientDto) {
    this.clientEdition = selectedClient;
    this.showMainForm(selectedClient);
  }

  public async verifyForm(): Promise<void> {
    if (this.subscription && !this.subscription.closed ||
      !this.buttons.registrationButtonIsEnabled && !this.buttons.updateButtonIsEnabled) {
      return;
    }

    if (!(await this.validateFields())) {
      this.buttons.isRegister = !this.buttons.isRegister;
      this.buttons.isEdit = !this.buttons.isEdit;
      setTimeout(() => {
        this.buttons.isRegister = !this.buttons.isRegister;
        this.buttons.isEdit = !this.buttons.isEdit;
      }, 0);
      return;
    }

    this.progressBarService.show();
    const keysForm = Object.keys(this.form.controls);

    if (!keysForm || keysForm.length === 0) {
      this.progressBarService.hide();
      return;
    }

    let client: any = {};
    for (let i = 0; i < keysForm.length; i++) {
      const key = keysForm[i];
      if (Object.prototype.hasOwnProperty.call(this.form.controls, key)) {
        if (key === 'name') {
          client = Object.assign(client, this.processFullName(this.form.get(key)!.value));
        } else {
          client[key] = this.form.get(key)!.value;
        }
      }
      if (i === keysForm.length - 1) {
        if (this.buttons.isRegister) {
          this.create(client as ClientRequest);
        } else {
          this.update(this.clientEdition.clientID, client as ClientRequest);
        }
      }
    }
  }

  public showMainForm(client: ClientDto | null = null): void {
    this.buttons.isRegister = client == null;
    this.buttons.isEdit = !this.buttons.isRegister;
    this.buttons.updateButtonIsEnabled = false;
    this.buttons.registrationButtonIsEnabled = false;
    this.mainForm(client);
    this.showForm = true;
    this.resize();

    setTimeout(() => {
      const divClientContainer = document.getElementById('client-container') as HTMLDivElement;
      const divCreateFormContainer = divClientContainer?.querySelector('#create-form-container');

      if (divCreateFormContainer) {
        (divCreateFormContainer as HTMLDivElement).style.width = `${divClientContainer.clientWidth}px`;
        (divCreateFormContainer as HTMLDivElement).style.minHeight = `${divClientContainer.clientHeight}px`;
        (divCreateFormContainer as HTMLDivElement).style.display = 'block';
      }
    }, 200);
  }

  public runChangePage(e: number) {
    this.page = e;
    this.pageSubject.next();
  }

  public resetFormFields(): void {
    this.form.reset({
      name: '',
      phoneNumber: '',
      emailAddress: '',
      startDate: '',
      endDate: ''
    });

    this.buttons.isRegister = true;
    this.buttons.isEdit = false;
    this.enableUpdateButton();
    this.enableRegisterButton();
  }

  public ngOnInit(): void {
    this.progressBarService.show();
    this.changePage();
    this.filterForm();
    this.findAll();
    this.subscriptionOffcanvas = this.offcanvasService.offcanvas$.subscribe(isOffcanvasOpen => {
      if (isOffcanvasOpen && this.showForm && this.form) {
        this.showForm = false;
        this.resetFormFields();
      }
    });
  }

  public ngAfterViewInit(): void {
    this.search();
  }

  public ngOnDestroy(): void {
    this.pageSubscription.unsubscribe();
    this.destroy$.next();
    this.destroy$.complete();
    this.subscriptionOffcanvas.unsubscribe();
  }
}
