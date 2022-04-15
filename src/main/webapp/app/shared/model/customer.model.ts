import { IOrder } from 'app/shared/model/order.model';

export interface ICustomer {
  id?: number;
  iD?: number;
  name?: string | null;
  surname?: string | null;
  phone?: string | null;
  address?: string;
  orders?: IOrder[] | null;
}

export const defaultValue: Readonly<ICustomer> = {};
