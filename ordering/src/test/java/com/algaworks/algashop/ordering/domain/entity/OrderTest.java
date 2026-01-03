package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(customerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> Assertions.assertThat(o.isDraft()).isTrue(),
                o -> Assertions.assertThat(o.items()).isEmpty(),

                o -> Assertions.assertThat(o.placedAt()).isNull(),
                o -> Assertions.assertThat(o.paidAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNull(),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.billing()).isNull(),
                o -> Assertions.assertThat(o.shipping()).isNull(),
                o -> Assertions.assertThat(o.paymentMethod()).isNull()

        );
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();

        order.addItem(product, new Quantity(1));

        Assertions.assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(1));

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    public void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2)
        );

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1)
        );

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenMarkAsPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        order.markAsPaid();
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenPaidOrder_whenMarkAsReady_shouldChangeToReadyAndSetReadyAt() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.markAsReady();
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                o -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }

    @Test
    public void givenNonPaidOrder_whenTryToMarkAsReady_shouldThrowAndNotChangeStatusOrReadyAt() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.PLACED),
                o -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBilling_shouldAllowChange() {
        Billing billing = OrderTestDataBuilder.aBilling();
        Order order = Order.draft(new CustomerId());
        order.changeBilling(billing);

        Assertions.assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    public void givenDraftOrder_whenChangeShipping_shouldAllowChange() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Order order = Order.draft(new CustomerId());

        order.changeShipping(shipping);

        Assertions.assertWith(order, o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping));
    }

    @Test
    public void givenDraftOrderAndDeliveryDateInThePast_whenChangeShipping_shouldNotAllowChange() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(2);

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shipping));
    }

    @Test
    public void givenNonDraftOrder_whenTryToAddItem_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(product, new Quantity(1)));
    }

    @Test
    public void givenNonDraftOrder_whenTryToChangePaymentMethod_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));
    }

    @Test
    public void givenNonDraftOrder_whenTryToChangeBilling_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBilling(OrderTestDataBuilder.aBilling()));
    }

    @Test
    public void givenNonDraftOrder_whenTryToChangeShipping_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(OrderTestDataBuilder.aShipping()));
    }

    @Test
    public void givenNonDraftOrder_whenTryToChangeItemQuantity_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeItemQuantity(orderItem.id(), new Quantity(10)));
    }

    @Test
    public void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(3)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("500")),
                (o) -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5))
        );
    }

    @Test
    public void givenOutOfStockProduct_whenTryToAddToAnOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(
                ProductTestDataBuilder.aProductUnavailable().build(),
                new Quantity(1)
        );

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(addItemTask);
    }

    @Test
    public void givenDraftOrder_whenRemoveItem_shouldRecalculateTotalsAndItems() {
        Order order = OrderTestDataBuilder.anOrder().build();

        OrderItem ramItem = order.items().stream()
                .filter(i -> i.productName().equals(new ProductName("4GB RAM")))
                .findFirst()
                .orElseThrow();

        order.removeItem(ramItem.id());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.items()).hasSize(1),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(2)),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("6010"))
        );
    }

    @Test
    public void givenDraftOrder_whenRemoveNonExistingItem_shouldThrow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        Assertions.assertThatExceptionOfType(com.algaworks.algashop.ordering.domain.exception.OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(new com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId()));
    }

    @Test
    public void givenNonDraftOrder_whenTryToRemoveItem_shouldNotAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(orderItem.id()));
    }

    @Test
    public void givenVariousStatuses_whenCancel_shouldChangeToCanceledAndSetCanceledAt() {
        // DRAFT
        Order draftOrder = OrderTestDataBuilder.anOrder().build();
        draftOrder.cancel();
        Assertions.assertWith(draftOrder,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull(),
                o -> Assertions.assertThat(o.isCanceled()).isTrue()
        );

        // PLACED
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        placedOrder.cancel();
        Assertions.assertWith(placedOrder,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull(),
                o -> Assertions.assertThat(o.isCanceled()).isTrue()
        );

        // PAID
        Order paidOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        paidOrder.cancel();
        Assertions.assertWith(paidOrder,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull(),
                o -> Assertions.assertThat(o.isCanceled()).isTrue()
        );

        // READY
        Order readyOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        readyOrder.markAsReady();
        readyOrder.cancel();
        Assertions.assertWith(readyOrder,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull(),
                o -> Assertions.assertThat(o.isCanceled()).isTrue()
        );
    }

    @Test
    public void givenCanceledOrder_whenTryToCancelAgain_shouldThrowAndNotChangeCanceledAt() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        order.cancel();
        OffsetDateTime firstCanceledAt = order.canceledAt();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isEqualTo(firstCanceledAt)
        );
    }

    @Test
    public void isCanceled_shouldReturnTrueOnlyWhenStatusIsCanceled() {
        Order draftOrder = OrderTestDataBuilder.anOrder().build();
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order paidOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Order readyOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        readyOrder.markAsReady();
        Order canceledOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        canceledOrder.cancel();

        Assertions.assertThat(draftOrder.isCanceled()).isFalse();
        Assertions.assertThat(placedOrder.isCanceled()).isFalse();
        Assertions.assertThat(paidOrder.isCanceled()).isFalse();
        Assertions.assertThat(readyOrder.isCanceled()).isFalse();
        Assertions.assertThat(canceledOrder.isCanceled()).isTrue();
    }

}
