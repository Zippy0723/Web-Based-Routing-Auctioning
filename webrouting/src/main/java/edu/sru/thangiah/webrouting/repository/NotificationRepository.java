package edu.sru.thangiah.webrouting.repository;

import edu.sru.thangiah.webrouting.domain.Notification;

import org.springframework.data.repository.CrudRepository;

/**
 * Sets the Notification Repository using the CrudRepository
 */

public interface NotificationRepository extends CrudRepository<Notification, Long> {}
