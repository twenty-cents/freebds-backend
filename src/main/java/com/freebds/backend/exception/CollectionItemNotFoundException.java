package com.freebds.backend.exception;

/**
 * Specific API entity not found exception
 */
public class CollectionItemNotFoundException extends RuntimeException {
    private static final String MESSAGE= "The item %s cannot be found in the collection %s";

    private String item;
    private String collection;

    public CollectionItemNotFoundException(String item, String collection) {
        super(String.format(MESSAGE, item, collection));
        this.item = item;
        this.collection = collection;
    }

    public String getItem() {
        return item;
    }

    public String getCollection() {
        return collection;
    }
}

