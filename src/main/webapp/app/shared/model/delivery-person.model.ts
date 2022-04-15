import { ICourse } from 'app/shared/model/course.model';

export interface IDeliveryPerson {
  id?: number;
  iD?: number;
  name?: string;
  surname?: string;
  phone?: string | null;
  vehiculeType?: string | null;
  latitude?: number;
  longitude?: number;
  courses?: ICourse[] | null;
}

export const defaultValue: Readonly<IDeliveryPerson> = {};
