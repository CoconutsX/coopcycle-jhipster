import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const customerEntity = useAppSelector(state => state.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="coopcycleApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="iD">
              <Translate contentKey="coopcycleApp.customer.iD">I D</Translate>
            </span>
          </dt>
          <dd>{customerEntity.iD}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopcycleApp.customer.name">Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="coopcycleApp.customer.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{customerEntity.surname}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="coopcycleApp.customer.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phone}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="coopcycleApp.customer.address">Address</Translate>
            </span>
          </dt>
          <dd>{customerEntity.address}</dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
