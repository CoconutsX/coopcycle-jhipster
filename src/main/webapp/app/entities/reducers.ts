import cooperative from 'app/entities/cooperative/cooperative.reducer';
import customer from 'app/entities/customer/customer.reducer';
import deliveryPerson from 'app/entities/delivery-person/delivery-person.reducer';
import course from 'app/entities/course/course.reducer';
import order from 'app/entities/order/order.reducer';
import product from 'app/entities/product/product.reducer';
import payment from 'app/entities/payment/payment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cooperative,
  customer,
  deliveryPerson,
  course,
  order,
  product,
  payment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
