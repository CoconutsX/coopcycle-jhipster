import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { ICourse } from 'app/shared/model/course.model';
import { IPayment } from 'app/shared/model/payment.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ICooperative } from 'app/shared/model/cooperative.model';
import { State } from 'app/shared/model/enumerations/state.model';

export interface IOrder {
  id?: number;
  iDOrder?: number;
  iDCoop?: number;
  iDCustomer?: number;
  iDCourse?: number;
  price?: number | null;
  date?: string | null;
  orderState?: State | null;
  products?: IProduct[] | null;
  course?: ICourse | null;
  payment?: IPayment | null;
  customer?: ICustomer | null;
  cooperative?: ICooperative | null;
}

export const defaultValue: Readonly<IOrder> = {};
