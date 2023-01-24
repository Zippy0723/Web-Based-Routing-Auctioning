package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;

/**
 * Sets the Contractors Repository using the CrudRepository
 * @author Ian Black	imb1007@sru.edu
 * @since 2/8/2022
 */

public interface MaintenanceOrdersRepository extends CrudRepository<MaintenanceOrders, Long> {}
