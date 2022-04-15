import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrder } from 'app/shared/model/order.model';
import { getEntities } from './order.reducer';

export const Order = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const orderList = useAppSelector(state => state.order.entities);
  const loading = useAppSelector(state => state.order.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="order-heading" data-cy="OrderHeading">
        <Translate contentKey="coopcycleApp.order.home.title">Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.order.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.order.home.createLabel">Create new Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderList && orderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.order.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.iDOrder">I D Order</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.iDCoop">I D Coop</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.iDCustomer">I D Customer</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.iDCourse">I D Course</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.price">Price</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.orderState">Order State</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.customer">Customer</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.order.cooperative">Cooperative</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderList.map((order, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order/${order.id}`} color="link" size="sm">
                      {order.id}
                    </Button>
                  </td>
                  <td>{order.iDOrder}</td>
                  <td>{order.iDCoop}</td>
                  <td>{order.iDCustomer}</td>
                  <td>{order.iDCourse}</td>
                  <td>{order.price}</td>
                  <td>{order.date ? <TextFormat type="date" value={order.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`coopcycleApp.State.${order.orderState}`} />
                  </td>
                  <td>{order.customer ? <Link to={`/customer/${order.customer.id}`}>{order.customer.id}</Link> : ''}</td>
                  <td>{order.cooperative ? <Link to={`/cooperative/${order.cooperative.id}`}>{order.cooperative.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order/${order.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/order/${order.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/order/${order.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.order.home.notFound">No Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Order;
