import { IOrder } from 'app/shared/model/order.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';

export interface IPayment {
  id?: number;
  method?: PaymentMethod;
  ammount?: number;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IPayment> = {};
