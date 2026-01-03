package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {
    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName productName;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;
    private Boolean available;

    @Builder(builderClassName = "ExistingShoppingCartItem", builderMethodName = "existing")
    public ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId productId, ProductName productName,
                            Money price, Quantity quantity, Boolean available, Money totalAmount) {
        this.setId(id);
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(productId);
        this.setProductName(productName);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setAvailable(available);
        this.setTotalAmount(totalAmount);
    }

    @Builder(builderClassName = "BrandNewShoppingCartItem", builderMethodName = "brandNew")
    public ShoppingCartItem(ShoppingCartId shoppingCartId,
                            ProductId productId, ProductName productName, Money price,
                            Quantity quantity, Boolean available) {
        this(new ShoppingCartItemId(), shoppingCartId, productId, productName, price, quantity, available, Money.ZERO);
        this.recalculateTotals();
    }

    public void recalculateTotals() {
        this.setTotalAmount(price().multiply(quantity));
    }

    void changeQuantity(Quantity quantity) {
        if (Quantity.ZERO.equals(quantity)) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        this.setQuantity(quantity);
        this.recalculateTotals();
    }

    void refresh(Product product) {
        Objects.requireNonNull(product);

        if (!product.id().equals(this.productId())) {
            throw new ShoppingCartItemIncompatibleProductException(this.id(), this.productId());
        }

        this.setPrice(product.price());
        this.setAvailable(product.inStock());
        this.setProductName(product.name());
        this.recalculateTotals();
    }

    public ShoppingCartItemId id() {
        return id;
    }

    public ShoppingCartId shoppingCartId() {
        return shoppingCartId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName productName() {
        return productName;
    }

    public Money price() {
        return price;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Boolean isAvailable() {
        return available;
    }

    private void setId(ShoppingCartItemId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setShoppingCartId(ShoppingCartId shoppingCartId) {
        Objects.requireNonNull(id);
        this.shoppingCartId = shoppingCartId;
    }

    private void setProductId(ProductId productId) {
        Objects.requireNonNull(productId);
        this.productId = productId;
    }

    private void setProductName(ProductName name) {
        Objects.requireNonNull(name);
        this.productName = name;
    }

    private void setPrice(Money price) {
        Objects.requireNonNull(price);
        this.price = price;
    }

    private void setQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.quantity = quantity;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setAvailable(Boolean available) {
        Objects.requireNonNull(available);
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
