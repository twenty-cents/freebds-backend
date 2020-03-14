package com.freebds.backend.common.web;

/**
 * Parent interface of all mappers.
 *
 * @param <E> Entity object
 * @param <D> DTO object
 */
public interface EntityDTOMapper <E, D> {

    /**
     * Convert the <code>D</code> DTO object to its corresponding entity <code>E</code>.
     *
     * @param dto of <code>D</code> The DTO to convert
     * @return <code>E</code> resulting entity
     */
    public E toEntity(D dto);

    /**
     * Convert the <code>E</code> entity object to its corresponding DTO <code>D</code>.
     *
     * @param entity of <code>E</code> entity object to convert
     * @return <code>D</code> resulting DTO
     */
    public D toDTO(E entity);


}
