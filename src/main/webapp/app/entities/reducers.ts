import product from 'app/entities/product/product.reducer';
import address from 'app/entities/address/address.reducer';
import shoppingOrder from 'app/entities/shopping-order/shopping-order.reducer';
import productOrder from 'app/entities/product-order/product-order.reducer';
import shipment from 'app/entities/shipment/shipment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  address,
  shoppingOrder,
  productOrder,
  shipment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
