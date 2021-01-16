import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOverTime } from 'app/shared/model/over-time.model';
import { OverTimeService } from './over-time.service';
import { OverTimeDeleteDialogComponent } from './over-time-delete-dialog.component';
import { IDesignation } from 'app/shared/model/designation.model';
import { MonthType } from 'app/shared/model/enumerations/month-type.model';
import * as moment from 'moment';
import { Moment } from 'moment';

@Component({
  selector: 'jhi-over-time',
  templateUrl: './over-time.component.html',
})
export class OverTimeComponent implements OnInit, OnDestroy {
  overTimes?: IOverTime[];
  eventSubscriber?: Subscription;
  years: number[] = [];
  selectedYear?: number;
  designations: IDesignation[] = [];
  selectedDesignation?: IDesignation;
  selectedDesignationId?: number;
  selectedMonth?: MonthType;
  fromDate?: Moment;
  toDate?: Moment;

  constructor(
    protected overTimeService: OverTimeService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.overTimeService.query().subscribe((res: HttpResponse<IOverTime[]>) => (this.overTimes = res.body || []));
  }

  ngOnInit(): void {
    this.configureYears();
    this.loadAll();
    this.registerChangeInOverTimes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOverTime): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInOverTimes(): void {
    this.eventSubscriber = this.eventManager.subscribe('overTimeListModification', () => this.loadAll());
  }

  delete(overTime: IOverTime): void {
    const modalRef = this.modalService.open(OverTimeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.overTime = overTime;
  }

  configureYears(): void {
    let year = new Date().getFullYear();
    this.selectedYear = new Date().getFullYear();
    this.years.push(year);
    for (let i = 0; i < 3; i++) {
      year -= 1;
      this.years.push(year);
    }
  }
}
