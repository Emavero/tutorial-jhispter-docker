package com.betterprojectsfaster.tutorial.jhipsterdocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShoppingOrder.
 */
@Entity
@Table(name = "shopping_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 90)
    @Column(name = "name", length = 90, nullable = false, unique = true)
    private String name;

    @DecimalMin(value = "0")
    @Column(name = "total_amount")
    private Float totalAmount;

    @Column(name = "ordered")
    private LocalDate ordered;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "overallOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "buyer", "overallOrder" }, allowSetters = true)
    private Set<ProductOrder> orders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User buyer;

    @JsonIgnoreProperties(value = { "order", "shippedBy" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "order")
    private Shipment shipment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ShoppingOrder name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getTotalAmount() {
        return this.totalAmount;
    }

    public ShoppingOrder totalAmount(Float totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getOrdered() {
        return this.ordered;
    }

    public ShoppingOrder ordered(LocalDate ordered) {
        this.setOrdered(ordered);
        return this;
    }

    public void setOrdered(LocalDate ordered) {
        this.ordered = ordered;
    }

    public Set<ProductOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<ProductOrder> productOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setOverallOrder(null));
        }
        if (productOrders != null) {
            productOrders.forEach(i -> i.setOverallOrder(this));
        }
        this.orders = productOrders;
    }

    public ShoppingOrder orders(Set<ProductOrder> productOrders) {
        this.setOrders(productOrders);
        return this;
    }

    public ShoppingOrder addOrders(ProductOrder productOrder) {
        this.orders.add(productOrder);
        productOrder.setOverallOrder(this);
        return this;
    }

    public ShoppingOrder removeOrders(ProductOrder productOrder) {
        this.orders.remove(productOrder);
        productOrder.setOverallOrder(null);
        return this;
    }

    public User getBuyer() {
        return this.buyer;
    }

    public void setBuyer(User user) {
        this.buyer = user;
    }

    public ShoppingOrder buyer(User user) {
        this.setBuyer(user);
        return this;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        if (this.shipment != null) {
            this.shipment.setOrder(null);
        }
        if (shipment != null) {
            shipment.setOrder(this);
        }
        this.shipment = shipment;
    }

    public ShoppingOrder shipment(Shipment shipment) {
        this.setShipment(shipment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((ShoppingOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingOrder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", ordered='" + getOrdered() + "'" +
            "}";
    }
}
