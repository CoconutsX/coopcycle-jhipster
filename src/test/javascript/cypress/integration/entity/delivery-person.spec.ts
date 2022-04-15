import { entityItemSelector } from '../../support/commands';
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

describe('DeliveryPerson e2e test', () => {
  const deliveryPersonPageUrl = '/delivery-person';
  const deliveryPersonPageUrlPattern = new RegExp('/delivery-person(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const deliveryPersonSample = {
    iD: 63285,
    name: 'transmit centrafricaine bottom-line',
    surname: 'Fresh Cambridgeshire Cambridgeshire',
    latitude: 33076,
    longitude: 72283,
  };

  let deliveryPerson: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/delivery-people+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/delivery-people').as('postEntityRequest');
    cy.intercept('DELETE', '/api/delivery-people/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deliveryPerson) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/delivery-people/${deliveryPerson.id}`,
      }).then(() => {
        deliveryPerson = undefined;
      });
    }
  });

  it('DeliveryPeople menu should load DeliveryPeople page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('delivery-person');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DeliveryPerson').should('exist');
    cy.url().should('match', deliveryPersonPageUrlPattern);
  });

  describe('DeliveryPerson page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deliveryPersonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DeliveryPerson page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/delivery-person/new$'));
        cy.getEntityCreateUpdateHeading('DeliveryPerson');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryPersonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/delivery-people',
          body: deliveryPersonSample,
        }).then(({ body }) => {
          deliveryPerson = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/delivery-people+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [deliveryPerson],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(deliveryPersonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DeliveryPerson page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deliveryPerson');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryPersonPageUrlPattern);
      });

      it('edit button click should load edit DeliveryPerson page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeliveryPerson');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryPersonPageUrlPattern);
      });

      it('last delete button click should delete instance of DeliveryPerson', () => {
        cy.intercept('GET', '/api/delivery-people/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('deliveryPerson').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryPersonPageUrlPattern);

        deliveryPerson = undefined;
      });
    });
  });

  describe('new DeliveryPerson page', () => {
    beforeEach(() => {
      cy.visit(`${deliveryPersonPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DeliveryPerson');
    });

    it('should create an instance of DeliveryPerson', () => {
      cy.get(`[data-cy="iD"]`).type('49475').should('have.value', '49475');

      cy.get(`[data-cy="name"]`).type('dynamic').should('have.value', 'dynamic');

      cy.get(`[data-cy="surname"]`).type('Stand-alone').should('have.value', 'Stand-alone');

      cy.get(`[data-cy="phone"]`).type('+33 217709').should('have.value', '+33 217709');

      cy.get(`[data-cy="vehiculeType"]`).type('UIC-Franc Home').should('have.value', 'UIC-Franc Home');

      cy.get(`[data-cy="latitude"]`).type('51155').should('have.value', '51155');

      cy.get(`[data-cy="longitude"]`).type('37662').should('have.value', '37662');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        deliveryPerson = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', deliveryPersonPageUrlPattern);
    });
  });
});
