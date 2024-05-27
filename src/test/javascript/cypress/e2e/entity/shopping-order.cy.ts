import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ShoppingOrder e2e test', () => {
  const shoppingOrderPageUrl = '/shopping-order';
  const shoppingOrderPageUrlPattern = new RegExp('/shopping-order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const shoppingOrderSample = {"name":"mechanically well-documented"};

  let shoppingOrder;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"wnlh=@D\\57YyS","firstName":"Suzanne","lastName":"Wolff","email":"Reymundo50@hotmail.com","imageUrl":"fantastic good","langKey":"motive out"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/shopping-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shopping-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shopping-orders/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/product-orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (shoppingOrder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shopping-orders/${shoppingOrder.id}`,
      }).then(() => {
        shoppingOrder = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('ShoppingOrders menu should load ShoppingOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shopping-order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShoppingOrder').should('exist');
    cy.url().should('match', shoppingOrderPageUrlPattern);
  });

  describe('ShoppingOrder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shoppingOrderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShoppingOrder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/shopping-order/new$'));
        cy.getEntityCreateUpdateHeading('ShoppingOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shoppingOrderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shopping-orders',
          body: {
            ...shoppingOrderSample,
            buyer: user,
          },
        }).then(({ body }) => {
          shoppingOrder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shopping-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shoppingOrder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(shoppingOrderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(shoppingOrderPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ShoppingOrder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shoppingOrder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shoppingOrderPageUrlPattern);
      });

      it('edit button click should load edit ShoppingOrder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shoppingOrderPageUrlPattern);
      });

      it('edit button click should load edit ShoppingOrder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShoppingOrder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shoppingOrderPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ShoppingOrder', () => {
        cy.intercept('GET', '/api/shopping-orders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('shoppingOrder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shoppingOrderPageUrlPattern);

        shoppingOrder = undefined;
      });
    });
  });

  describe('new ShoppingOrder page', () => {
    beforeEach(() => {
      cy.visit(`${shoppingOrderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShoppingOrder');
    });

    it.skip('should create an instance of ShoppingOrder', () => {
      cy.get(`[data-cy="name"]`).type('pro');
      cy.get(`[data-cy="name"]`).should('have.value', 'pro');

      cy.get(`[data-cy="totalAmount"]`).type('1220.6');
      cy.get(`[data-cy="totalAmount"]`).should('have.value', '1220.6');

      cy.get(`[data-cy="ordered"]`).type('2024-05-27');
      cy.get(`[data-cy="ordered"]`).blur();
      cy.get(`[data-cy="ordered"]`).should('have.value', '2024-05-27');

      cy.get(`[data-cy="buyer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        shoppingOrder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', shoppingOrderPageUrlPattern);
    });
  });
});
