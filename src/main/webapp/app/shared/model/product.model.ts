import { ICooperative } from 'app/shared/model/cooperative.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IProduct {
  id?: number;
  iDProduct?: number;
  iDMenu?: number;
  name?: string | null;
  price?: number | null;
  stock?: number | null;
  cooperative?: ICooperative | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IProduct> = {};
