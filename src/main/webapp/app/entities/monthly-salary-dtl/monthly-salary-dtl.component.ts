import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager, JhiDataUtils, JhiAlertService } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMonthlySalaryDtl } from 'app/shared/model/monthly-salary-dtl.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MonthlySalaryDtlService } from './monthly-salary-dtl.service';
import { MonthlySalaryDtlDeleteDialogComponent } from './monthly-salary-dtl-delete-dialog.component';
import { PayrollManagementService } from 'app/app-components/payroll-management/payroll-management.service';

@Component({
  selector: 'jhi-monthly-salary-dtl',
  templateUrl: './monthly-salary-dtl.component.html',
})
export class MonthlySalaryDtlComponent implements OnInit, OnDestroy {
  monthlySalaryDtls?: IMonthlySalaryDtl[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  monthlySalaryId?: number;

  constructor(
    protected monthlySalaryDtlService: MonthlySalaryDtlService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private payrollManagementService: PayrollManagementService,
    private jhiAlertService: JhiAlertService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.monthlySalaryDtlService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'monthlySalaryId.equals': this.monthlySalaryId,
      })
      .subscribe(
        (res: HttpResponse<IMonthlySalaryDtl[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInMonthlySalaryDtls();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      this.monthlySalaryId = +params.get('monthlySalaryId')!;
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      this.predicate = predicate;
      this.ascending = ascending;
      this.loadPage(pageNumber, true);
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMonthlySalaryDtl): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInMonthlySalaryDtls(): void {
    this.eventSubscriber = this.eventManager.subscribe('monthlySalaryDtlListModification', () => this.loadPage());
  }

  delete(monthlySalaryDtl: IMonthlySalaryDtl): void {
    const modalRef = this.modalService.open(MonthlySalaryDtlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.monthlySalaryDtl = monthlySalaryDtl;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMonthlySalaryDtl[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate([], {
        queryParams: {
          monthlySalaryId: this.monthlySalaryId,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
        relativeTo: this.activatedRoute,
      });
    }
    this.monthlySalaryDtls = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  public regenerateEmployeeSalaries(monthlySalaryDtl: IMonthlySalaryDtl): void {
    this.payrollManagementService.regenerateEmployeeSalary(this.monthlySalaryId, monthlySalaryDtl.id).subscribe(res => {
      monthlySalaryDtl = res.body!;
      this.jhiAlertService.success('Employee salary re-generation process completed successfully');
      this.handleNavigation();
    });
  }
}
