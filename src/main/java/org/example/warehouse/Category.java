package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private static final Map<String, Category> CATEGORIES = new HashMap<>();

    private final String name;

    private Category(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        this.name = name;
    }

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        return CATEGORIES.computeIfAbsent(capitalizeFirstLetter(name), Category::new);
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
