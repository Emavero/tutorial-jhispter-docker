import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShoppingOrder from './shopping-order';
import ShoppingOrderDetail from './shopping-order-detail';
import ShoppingOrderUpdate from './shopping-order-update';
import ShoppingOrderDeleteDialog from './shopping-order-delete-dialog';

const ShoppingOrderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShoppingOrder />} />
    <Route path="new" element={<ShoppingOrderUpdate />} />
    <Route path=":id">
      <Route index element={<ShoppingOrderDetail />} />
      <Route path="edit" element={<ShoppingOrderUpdate />} />
      <Route path="delete" element={<ShoppingOrderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShoppingOrderRoutes;
