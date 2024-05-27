import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './shopping-order.reducer';

export const ShoppingOrder = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const shoppingOrderList = useAppSelector(state => state.shoppingOrder.entities);
  const loading = useAppSelector(state => state.shoppingOrder.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="shopping-order-heading" data-cy="ShoppingOrderHeading">
        <Translate contentKey="mySimpleShopApp.shoppingOrder.home.title">Shopping Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mySimpleShopApp.shoppingOrder.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/shopping-order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mySimpleShopApp.shoppingOrder.home.createLabel">Create new Shopping Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {shoppingOrderList && shoppingOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mySimpleShopApp.shoppingOrder.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="mySimpleShopApp.shoppingOrder.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('totalAmount')}>
                  <Translate contentKey="mySimpleShopApp.shoppingOrder.totalAmount">Total Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalAmount')} />
                </th>
                <th className="hand" onClick={sort('ordered')}>
                  <Translate contentKey="mySimpleShopApp.shoppingOrder.ordered">Ordered</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ordered')} />
                </th>
                <th>
                  <Translate contentKey="mySimpleShopApp.shoppingOrder.buyer">Buyer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shoppingOrderList.map((shoppingOrder, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/shopping-order/${shoppingOrder.id}`} color="link" size="sm">
                      {shoppingOrder.id}
                    </Button>
                  </td>
                  <td>{shoppingOrder.name}</td>
                  <td>{shoppingOrder.totalAmount}</td>
                  <td>
                    {shoppingOrder.ordered ? <TextFormat type="date" value={shoppingOrder.ordered} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{shoppingOrder.buyer ? shoppingOrder.buyer.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/shopping-order/${shoppingOrder.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/shopping-order/${shoppingOrder.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/shopping-order/${shoppingOrder.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="mySimpleShopApp.shoppingOrder.home.notFound">No Shopping Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ShoppingOrder;
