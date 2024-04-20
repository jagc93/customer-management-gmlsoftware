package com.gmlsoftware.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gmlsoftware.entity.Client;

@Repository
public interface ClientRepository
	extends JpaRepository<Client, Long>,
			JpaSpecificationExecutor<Client> {

	boolean existsByEmailAddressIgnoreCase(String emailAddress);

	boolean existsByEmailAddressIgnoreCaseAndClientIDNot(String emailAddress, Long clientID);
}
