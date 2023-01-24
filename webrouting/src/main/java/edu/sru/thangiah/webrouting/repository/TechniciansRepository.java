package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.webrouting.domain.Technicians;

/**
 * Sets the Technicians Repository using the CrudRepository
 * @author Ian Black	imb1007@sru.edu
 * @since 2/8/2022
 */

public interface TechniciansRepository extends CrudRepository<Technicians, Long> {}
