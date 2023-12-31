package com.amand.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class ProductEntity extends BaseEntity {

    @Column
    private String name;

    @Column(name = "old_price")
    private Double oldPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column
    private int amount;

    @Column
    private String season;

    @ManyToMany
    @JoinTable(name = "product_size",
               joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<SizeEntity> sizes ;

    @ManyToMany
    @JoinTable(name = "product_color",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<ColorEntity> colors;

    @ManyToMany
    @JoinTable(name = "product_bag",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "bag_id"))
    private List<BagEntity> bags;

    @ManyToMany
    @JoinTable(name = "product_order",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<OrderEntity> orders;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column
    private String image1;

    @Column
    private String image2;

    @Column
    private String image3;

    @Column
    private String image4;

}
