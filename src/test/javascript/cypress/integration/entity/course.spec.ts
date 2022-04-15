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

describe('Course e2e test', () => {
  const coursePageUrl = '/course';
  const coursePageUrlPattern = new RegExp('/course(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const courseSample = { iD: 65309, iDDeliveryPerson: 64561 };

  let course: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/courses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/courses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/courses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (course) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/courses/${course.id}`,
      }).then(() => {
        course = undefined;
      });
    }
  });

  it('Courses menu should load Courses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('course');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Course').should('exist');
    cy.url().should('match', coursePageUrlPattern);
  });

  describe('Course page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(coursePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Course page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/course/new$'));
        cy.getEntityCreateUpdateHeading('Course');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', coursePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/courses',
          body: courseSample,
        }).then(({ body }) => {
          course = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/courses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [course],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(coursePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Course page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('course');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', coursePageUrlPattern);
      });

      it('edit button click should load edit Course page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Course');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', coursePageUrlPattern);
      });

      it('last delete button click should delete instance of Course', () => {
        cy.intercept('GET', '/api/courses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('course').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', coursePageUrlPattern);

        course = undefined;
      });
    });
  });

  describe('new Course page', () => {
    beforeEach(() => {
      cy.visit(`${coursePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Course');
    });

    it('should create an instance of Course', () => {
      cy.get(`[data-cy="iD"]`).type('1102').should('have.value', '1102');

      cy.get(`[data-cy="iDDeliveryPerson"]`).type('2409').should('have.value', '2409');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        course = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', coursePageUrlPattern);
    });
  });
});
