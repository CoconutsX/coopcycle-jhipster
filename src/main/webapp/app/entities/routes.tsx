import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cooperative from './cooperative';
import Customer from './customer';
import DeliveryPerson from './delivery-person';
import Course from './course';
import Order from './order';
import Product from './product';
import Payment from './payment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}cooperative`} component={Cooperative} />
        <ErrorBoundaryRoute path={`${match.url}customer`} component={Customer} />
        <ErrorBoundaryRoute path={`${match.url}delivery-person`} component={DeliveryPerson} />
        <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
        <ErrorBoundaryRoute path={`${match.url}order`} component={Order} />
        <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
        <ErrorBoundaryRoute path={`${match.url}payment`} component={Payment} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
