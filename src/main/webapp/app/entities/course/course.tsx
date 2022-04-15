import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities } from './course.reducer';

export const Course = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const courseList = useAppSelector(state => state.course.entities);
  const loading = useAppSelector(state => state.course.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="course-heading" data-cy="CourseHeading">
        <Translate contentKey="coopcycleApp.course.home.title">Courses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.course.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/course/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.course.home.createLabel">Create new Course</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {courseList && courseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.course.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.course.iD">I D</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.course.iDDeliveryPerson">I D Delivery Person</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.course.order">Order</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.course.deliveryPerson">Delivery Person</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courseList.map((course, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/course/${course.id}`} color="link" size="sm">
                      {course.id}
                    </Button>
                  </td>
                  <td>{course.iD}</td>
                  <td>{course.iDDeliveryPerson}</td>
                  <td>{course.order ? <Link to={`/order/${course.order.id}`}>{course.order.id}</Link> : ''}</td>
                  <td>
                    {course.deliveryPerson ? (
                      <Link to={`/delivery-person/${course.deliveryPerson.id}`}>{course.deliveryPerson.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/course/${course.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/course/${course.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/course/${course.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.course.home.notFound">No Courses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Course;
