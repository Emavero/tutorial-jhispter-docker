import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShoppingOrder } from 'app/shared/model/shopping-order.model';
import { getEntities as getShoppingOrders } from 'app/entities/shopping-order/shopping-order.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IShipment } from 'app/shared/model/shipment.model';
import { getEntity, updateEntity, createEntity, reset } from './shipment.reducer';

export const ShipmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const shoppingOrders = useAppSelector(state => state.shoppingOrder.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const shipmentEntity = useAppSelector(state => state.shipment.entity);
  const loading = useAppSelector(state => state.shipment.loading);
  const updating = useAppSelector(state => state.shipment.updating);
  const updateSuccess = useAppSelector(state => state.shipment.updateSuccess);

  const handleClose = () => {
    navigate('/shipment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getShoppingOrders({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...shipmentEntity,
      ...values,
      order: shoppingOrders.find(it => it.id.toString() === values.order?.toString()),
      shippedBy: users.find(it => it.id.toString() === values.shippedBy?.toString()),
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
          ...shipmentEntity,
          order: shipmentEntity?.order?.id,
          shippedBy: shipmentEntity?.shippedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mySimpleShopApp.shipment.home.createOrEditLabel" data-cy="ShipmentCreateUpdateHeading">
            <Translate contentKey="mySimpleShopApp.shipment.home.createOrEditLabel">Create or edit a Shipment</Translate>
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
                  id="shipment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mySimpleShopApp.shipment.shippedAt')}
                id="shipment-shippedAt"
                name="shippedAt"
                data-cy="shippedAt"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="shipment-order"
                name="order"
                data-cy="order"
                label={translate('mySimpleShopApp.shipment.order')}
                type="select"
                required
              >
                <option value="" key="0" />
                {shoppingOrders
                  ? shoppingOrders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="shipment-shippedBy"
                name="shippedBy"
                data-cy="shippedBy"
                label={translate('mySimpleShopApp.shipment.shippedBy')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/shipment" replace color="info">
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

export default ShipmentUpdate;
