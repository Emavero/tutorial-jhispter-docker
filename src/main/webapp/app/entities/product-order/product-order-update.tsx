import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IShoppingOrder } from 'app/shared/model/shopping-order.model';
import { getEntities as getShoppingOrders } from 'app/entities/shopping-order/shopping-order.reducer';
import { IProductOrder } from 'app/shared/model/product-order.model';
import { getEntity, updateEntity, createEntity, reset } from './product-order.reducer';

export const ProductOrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const shoppingOrders = useAppSelector(state => state.shoppingOrder.entities);
  const productOrderEntity = useAppSelector(state => state.productOrder.entity);
  const loading = useAppSelector(state => state.productOrder.loading);
  const updating = useAppSelector(state => state.productOrder.updating);
  const updateSuccess = useAppSelector(state => state.productOrder.updateSuccess);

  const handleClose = () => {
    navigate('/product-order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducts({}));
    dispatch(getUsers({}));
    dispatch(getShoppingOrders({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }

    const entity = {
      ...productOrderEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product?.toString()),
      buyer: users.find(it => it.id.toString() === values.buyer?.toString()),
      overallOrder: shoppingOrders.find(it => it.id.toString() === values.overallOrder?.toString()),
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
          ...productOrderEntity,
          product: productOrderEntity?.product?.id,
          buyer: productOrderEntity?.buyer?.id,
          overallOrder: productOrderEntity?.overallOrder?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mySimpleShopApp.productOrder.home.createOrEditLabel" data-cy="ProductOrderCreateUpdateHeading">
            <Translate contentKey="mySimpleShopApp.productOrder.home.createOrEditLabel">Create or edit a ProductOrder</Translate>
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
                  id="product-order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mySimpleShopApp.productOrder.amount')}
                id="product-order-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="product-order-product"
                name="product"
                data-cy="product"
                label={translate('mySimpleShopApp.productOrder.product')}
                type="select"
                required
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
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
                id="product-order-buyer"
                name="buyer"
                data-cy="buyer"
                label={translate('mySimpleShopApp.productOrder.buyer')}
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
              <ValidatedField
                id="product-order-overallOrder"
                name="overallOrder"
                data-cy="overallOrder"
                label={translate('mySimpleShopApp.productOrder.overallOrder')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-order" replace color="info">
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

export default ProductOrderUpdate;
