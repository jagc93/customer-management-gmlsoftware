package com.gmlsoftware.model.client;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
	private Long clientID;
	private String firstName;
	private String middleName;
	private String lastName;
	private String secondLastName;
	private String sharedKey;
	private String emailAddress;
	private String phoneNumber;
	private Date startDate;
	private Date endDate;
	private Date creationDate;
	private Date modificationDate;
}
