import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IPayment } from 'app/shared/model/payment.model';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { ICooperative } from 'app/shared/model/cooperative.model';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { State } from 'app/shared/model/enumerations/state.model';
import { getEntity, updateEntity, createEntity, reset } from './order.reducer';

export const OrderUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const courses = useAppSelector(state => state.course.entities);
  const payments = useAppSelector(state => state.payment.entities);
  const customers = useAppSelector(state => state.customer.entities);
  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);
  const stateValues = Object.keys(State);
  const handleClose = () => {
    props.history.push('/order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCourses({}));
    dispatch(getPayments({}));
    dispatch(getCustomers({}));
    dispatch(getCooperatives({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...orderEntity,
      ...values,
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          orderState: 'Ordered',
          ...orderEntity,
          date: convertDateTimeFromServer(orderEntity.date),
          customer: orderEntity?.customer?.id,
          cooperative: orderEntity?.cooperative?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.order.home.createOrEditLabel">Create or edit a Order</Translate>
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
                  id="order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.order.iDOrder')}
                id="order-iDOrder"
                name="iDOrder"
                data-cy="iDOrder"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.order.iDCoop')}
                id="order-iDCoop"
                name="iDCoop"
                data-cy="iDCoop"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.order.iDCustomer')}
                id="order-iDCustomer"
                name="iDCustomer"
                data-cy="iDCustomer"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.order.iDCourse')}
                id="order-iDCourse"
                name="iDCourse"
                data-cy="iDCourse"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.order.price')}
                id="order-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  min: { value: 3, message: translate('entity.validation.min', { min: 3 }) },
                  max: { value: 300, message: translate('entity.validation.max', { max: 300 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.order.date')}
                id="order-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopcycleApp.order.orderState')}
                id="order-orderState"
                name="orderState"
                data-cy="orderState"
                type="select"
              >
                {stateValues.map(state => (
                  <option value={state} key={state}>
                    {translate('coopcycleApp.State.' + state)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="order-customer"
                name="customer"
                data-cy="customer"
                label={translate('coopcycleApp.order.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopcycleApp.order.cooperative')}
                type="select"
              >
                <option value="" key="0" />
                {cooperatives
                  ? cooperatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
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

export default OrderUpdate;
