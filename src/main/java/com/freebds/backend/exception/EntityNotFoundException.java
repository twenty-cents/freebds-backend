package com.freebds.backend.exception;

/**
 * Specific API entity not found exception
 */
public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE= "The entity with ID: %s cannot be found in DB";
    private static final String MESSAGE_QUERY_NOT_FOUND = "The entity with criterion: % cannot be found in DB";
    //private static final String MESSAGE_QUERY_TOO_MANY_ROWS_FOUND = "The entity with criterion: % is not unique. Please check in DB";

    private String entityName;

    public EntityNotFoundException(long id, String entityName) {
        super(String.format(MESSAGE, id));
        this.entityName = entityName;
    }

    public EntityNotFoundException(String searchKey, String entityName) {
        super(String.format(MESSAGE, searchKey));
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }
}

