import { IOrder } from 'app/shared/model/order.model';
import { IDeliveryPerson } from 'app/shared/model/delivery-person.model';

export interface ICourse {
  id?: number;
  iD?: number;
  iDDeliveryPerson?: number;
  order?: IOrder | null;
  deliveryPerson?: IDeliveryPerson | null;
}

export const defaultValue: Readonly<ICourse> = {};
