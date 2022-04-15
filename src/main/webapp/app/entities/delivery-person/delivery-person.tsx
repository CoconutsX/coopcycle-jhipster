import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDeliveryPerson } from 'app/shared/model/delivery-person.model';
import { getEntities } from './delivery-person.reducer';

export const DeliveryPerson = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const deliveryPersonList = useAppSelector(state => state.deliveryPerson.entities);
  const loading = useAppSelector(state => state.deliveryPerson.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="delivery-person-heading" data-cy="DeliveryPersonHeading">
        <Translate contentKey="coopcycleApp.deliveryPerson.home.title">Delivery People</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.deliveryPerson.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/delivery-person/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.deliveryPerson.home.createLabel">Create new Delivery Person</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {deliveryPersonList && deliveryPersonList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.iD">I D</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.surname">Surname</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.vehiculeType">Vehicule Type</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.latitude">Latitude</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.deliveryPerson.longitude">Longitude</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {deliveryPersonList.map((deliveryPerson, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/delivery-person/${deliveryPerson.id}`} color="link" size="sm">
                      {deliveryPerson.id}
                    </Button>
                  </td>
                  <td>{deliveryPerson.iD}</td>
                  <td>{deliveryPerson.name}</td>
                  <td>{deliveryPerson.surname}</td>
                  <td>{deliveryPerson.phone}</td>
                  <td>{deliveryPerson.vehiculeType}</td>
                  <td>{deliveryPerson.latitude}</td>
                  <td>{deliveryPerson.longitude}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/delivery-person/${deliveryPerson.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/delivery-person/${deliveryPerson.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/delivery-person/${deliveryPerson.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopcycleApp.deliveryPerson.home.notFound">No Delivery People found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DeliveryPerson;
