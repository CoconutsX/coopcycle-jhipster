import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { IDeliveryPerson } from 'app/shared/model/delivery-person.model';
import { getEntities as getDeliveryPeople } from 'app/entities/delivery-person/delivery-person.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntity, updateEntity, createEntity, reset } from './course.reducer';

export const CourseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const orders = useAppSelector(state => state.order.entities);
  const deliveryPeople = useAppSelector(state => state.deliveryPerson.entities);
  const courseEntity = useAppSelector(state => state.course.entity);
  const loading = useAppSelector(state => state.course.loading);
  const updating = useAppSelector(state => state.course.updating);
  const updateSuccess = useAppSelector(state => state.course.updateSuccess);
  const handleClose = () => {
    props.history.push('/course');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getOrders({}));
    dispatch(getDeliveryPeople({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courseEntity,
      ...values,
      order: orders.find(it => it.id.toString() === values.order.toString()),
      deliveryPerson: deliveryPeople.find(it => it.id.toString() === values.deliveryPerson.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...courseEntity,
          order: courseEntity?.order?.id,
          deliveryPerson: courseEntity?.deliveryPerson?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.course.home.createOrEditLabel" data-cy="CourseCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.course.home.createOrEditLabel">Create or edit a Course</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="course-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.course.iD')}
                id="course-iD"
                name="iD"
                data-cy="iD"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.course.iDDeliveryPerson')}
                id="course-iDDeliveryPerson"
                name="iDDeliveryPerson"
                data-cy="iDDeliveryPerson"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField id="course-order" name="order" data-cy="order" label={translate('coopcycleApp.course.order')} type="select">
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="course-deliveryPerson"
                name="deliveryPerson"
                data-cy="deliveryPerson"
                label={translate('coopcycleApp.course.deliveryPerson')}
                type="select"
              >
                <option value="" key="0" />
                {deliveryPeople
                  ? deliveryPeople.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/course" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CourseUpdate;
