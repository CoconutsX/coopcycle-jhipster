import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './course.reducer';

export const CourseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const courseEntity = useAppSelector(state => state.course.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courseDetailsHeading">
          <Translate contentKey="coopcycleApp.course.detail.title">Course</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courseEntity.id}</dd>
          <dt>
            <span id="iD">
              <Translate contentKey="coopcycleApp.course.iD">I D</Translate>
            </span>
          </dt>
          <dd>{courseEntity.iD}</dd>
          <dt>
            <span id="iDDeliveryPerson">
              <Translate contentKey="coopcycleApp.course.iDDeliveryPerson">I D Delivery Person</Translate>
            </span>
          </dt>
          <dd>{courseEntity.iDDeliveryPerson}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.course.order">Order</Translate>
          </dt>
          <dd>{courseEntity.order ? courseEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.course.deliveryPerson">Delivery Person</Translate>
          </dt>
          <dd>{courseEntity.deliveryPerson ? courseEntity.deliveryPerson.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/course" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/course/${courseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourseDetail;
