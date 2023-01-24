package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.webrouting.domain.Role;

/**
 * Sets the Role Repository using the JpaRepository
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

public interface RoleRepository extends JpaRepository<Role, Long>{}
