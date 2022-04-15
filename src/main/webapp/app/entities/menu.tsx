import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cooperative">
        <Translate contentKey="global.menu.entities.cooperative" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/delivery-person">
        <Translate contentKey="global.menu.entities.deliveryPerson" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
