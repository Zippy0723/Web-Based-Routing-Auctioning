package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.webrouting.domain.Carriers;

/**
 * Sets the Carriers Repository using the CrudRepository
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 3/21/2022
 */

public interface CarriersRepository extends CrudRepository<Carriers, Long>{}
