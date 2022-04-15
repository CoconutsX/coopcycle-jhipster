import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './delivery-person.reducer';

export const DeliveryPersonDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const deliveryPersonEntity = useAppSelector(state => state.deliveryPerson.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deliveryPersonDetailsHeading">
          <Translate contentKey="coopcycleApp.deliveryPerson.detail.title">DeliveryPerson</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.id}</dd>
          <dt>
            <span id="iD">
              <Translate contentKey="coopcycleApp.deliveryPerson.iD">I D</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.iD}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopcycleApp.deliveryPerson.name">Name</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="coopcycleApp.deliveryPerson.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.surname}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="coopcycleApp.deliveryPerson.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.phone}</dd>
          <dt>
            <span id="vehiculeType">
              <Translate contentKey="coopcycleApp.deliveryPerson.vehiculeType">Vehicule Type</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.vehiculeType}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="coopcycleApp.deliveryPerson.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="coopcycleApp.deliveryPerson.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{deliveryPersonEntity.longitude}</dd>
        </dl>
        <Button tag={Link} to="/delivery-person" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/delivery-person/${deliveryPersonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeliveryPersonDetail;
