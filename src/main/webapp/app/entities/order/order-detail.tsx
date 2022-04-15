import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="coopcycleApp.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="iDOrder">
              <Translate contentKey="coopcycleApp.order.iDOrder">I D Order</Translate>
            </span>
          </dt>
          <dd>{orderEntity.iDOrder}</dd>
          <dt>
            <span id="iDCoop">
              <Translate contentKey="coopcycleApp.order.iDCoop">I D Coop</Translate>
            </span>
          </dt>
          <dd>{orderEntity.iDCoop}</dd>
          <dt>
            <span id="iDCustomer">
              <Translate contentKey="coopcycleApp.order.iDCustomer">I D Customer</Translate>
            </span>
          </dt>
          <dd>{orderEntity.iDCustomer}</dd>
          <dt>
            <span id="iDCourse">
              <Translate contentKey="coopcycleApp.order.iDCourse">I D Course</Translate>
            </span>
          </dt>
          <dd>{orderEntity.iDCourse}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="coopcycleApp.order.price">Price</Translate>
            </span>
          </dt>
          <dd>{orderEntity.price}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="coopcycleApp.order.date">Date</Translate>
            </span>
          </dt>
          <dd>{orderEntity.date ? <TextFormat value={orderEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="orderState">
              <Translate contentKey="coopcycleApp.order.orderState">Order State</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderState}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.order.customer">Customer</Translate>
          </dt>
          <dd>{orderEntity.customer ? orderEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.order.cooperative">Cooperative</Translate>
          </dt>
          <dd>{orderEntity.cooperative ? orderEntity.cooperative.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
