import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DeliveryPerson from './delivery-person';
import DeliveryPersonDetail from './delivery-person-detail';
import DeliveryPersonUpdate from './delivery-person-update';
import DeliveryPersonDeleteDialog from './delivery-person-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeliveryPersonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeliveryPersonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeliveryPersonDetail} />
      <ErrorBoundaryRoute path={match.url} component={DeliveryPerson} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeliveryPersonDeleteDialog} />
  </>
);

export default Routes;
