import { IProduct } from 'app/shared/model/product.model';
import { IUser } from 'app/shared/model/user.model';
import { IShoppingOrder } from 'app/shared/model/shopping-order.model';

export interface IProductOrder {
  id?: number;
  amount?: number;
  product?: IProduct;
  buyer?: IUser;
  overallOrder?: IShoppingOrder;
}

export const defaultValue: Readonly<IProductOrder> = {};
