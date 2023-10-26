package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final String name;
    private final Map<UUID, ProductRecord> products = new HashMap<>();
    private final Set<UUID> changedProducts = new HashSet<>();
    private final List<ProductRecord> orderedProducts = new ArrayList<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        return new Warehouse("Default");
    }

    public static Warehouse getInstance(String name) {
        return new Warehouse(name);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(orderedProducts);
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }

        if (price == null) {
            price = BigDecimal.ZERO;
        }

        if (products.containsKey(uuid)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        ProductRecord productRecord = new ProductRecord(uuid, name, category, price);
        products.put(uuid, productRecord);
        orderedProducts.add(productRecord);
        return productRecord;
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return Optional.ofNullable(products.get(uuid));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return orderedProducts.stream()
                .filter(productRecord -> productRecord.category().equals(category))
                .collect(Collectors.toList());
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        ProductRecord productRecord = products.get(uuid);

        if (productRecord == null) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }

        productRecord.setPrice(price);
        changedProducts.add(uuid);
    }

    public Set<ProductRecord> getChangedProducts() {
        return orderedProducts.stream()
                .filter(productRecord -> changedProducts.contains(productRecord.uuid()))
                .collect(Collectors.toSet());
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return orderedProducts.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }
}
