package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.webrouting.domain.Bids;

/**
 * Sets the Bids Repository using the CrudRepository
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 4/10/2022
 */

public interface BidsRepository extends CrudRepository<Bids, Long> {}