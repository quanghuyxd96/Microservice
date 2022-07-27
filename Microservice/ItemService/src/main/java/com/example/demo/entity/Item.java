package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Getter
@Setter
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private double price;
    private long quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && Objects.equals(supplier, item.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, supplier);
    }
}
