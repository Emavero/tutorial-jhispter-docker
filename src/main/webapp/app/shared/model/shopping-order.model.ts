import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IShoppingOrder {
  id?: number;
  name?: string;
  totalAmount?: number | null;
  ordered?: dayjs.Dayjs | null;
  buyer?: IUser;
}

export const defaultValue: Readonly<IShoppingOrder> = {};
