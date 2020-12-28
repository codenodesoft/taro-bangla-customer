import { Moment } from 'moment';
import { IEmployee } from 'app/shared/model/employee.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IFine {
  id?: number;
  finedOn?: Moment;
  reason?: any;
  amount?: number;
  finePercentage?: number;
  paymentStatus?: PaymentStatus;
  employee?: IEmployee;
}

export class Fine implements IFine {
  constructor(
    public id?: number,
    public finedOn?: Moment,
    public reason?: any,
    public amount?: number,
    public finePercentage?: number,
    public paymentStatus?: PaymentStatus,
    public employee?: IEmployee
  ) {}
}
