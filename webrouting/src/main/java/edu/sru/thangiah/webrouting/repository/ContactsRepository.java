package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.webrouting.domain.Contacts;

/**
 * Sets the Contacts Repository using the CrudRepository
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

public interface ContactsRepository extends CrudRepository<Contacts, Long> {}
