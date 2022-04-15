import { IProduct } from 'app/shared/model/product.model';
import { IOrder } from 'app/shared/model/order.model';

export interface ICooperative {
  id?: number;
  iD?: number;
  name?: string | null;
  phone?: string | null;
  address?: string;
  webURL?: string | null;
  products?: IProduct[] | null;
  orders?: IOrder[] | null;
}

export const defaultValue: Readonly<ICooperative> = {};
