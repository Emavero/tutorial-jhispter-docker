import { ProductCategory } from 'app/shared/model/enumerations/product-category.model';

export interface IProduct {
  id?: number;
  name?: string;
  price?: number;
  description?: string;
  pictureContentType?: string;
  picture?: string;
  specificationContentType?: string | null;
  specification?: string | null;
  category?: keyof typeof ProductCategory | null;
  inventory?: number;
}

export const defaultValue: Readonly<IProduct> = {};
