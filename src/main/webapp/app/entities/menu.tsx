import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/address">
        <Translate contentKey="global.menu.entities.address" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/shopping-order">
        <Translate contentKey="global.menu.entities.shoppingOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product-order">
        <Translate contentKey="global.menu.entities.productOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/shipment">
        <Translate contentKey="global.menu.entities.shipment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
