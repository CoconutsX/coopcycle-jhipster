import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="coopcycleApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="iDProduct">
              <Translate contentKey="coopcycleApp.product.iDProduct">I D Product</Translate>
            </span>
          </dt>
          <dd>{productEntity.iDProduct}</dd>
          <dt>
            <span id="iDMenu">
              <Translate contentKey="coopcycleApp.product.iDMenu">I D Menu</Translate>
            </span>
          </dt>
          <dd>{productEntity.iDMenu}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopcycleApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="coopcycleApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="stock">
              <Translate contentKey="coopcycleApp.product.stock">Stock</Translate>
            </span>
          </dt>
          <dd>{productEntity.stock}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.product.cooperative">Cooperative</Translate>
          </dt>
          <dd>{productEntity.cooperative ? productEntity.cooperative.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.product.order">Order</Translate>
          </dt>
          <dd>{productEntity.order ? productEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
