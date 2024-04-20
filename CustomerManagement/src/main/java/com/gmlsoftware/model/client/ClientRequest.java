package com.gmlsoftware.model.client;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
	private String firstName;
	private String middleName;
	private String lastName;
	private String secondLastName;
	private String emailAddress;
	private String phoneNumber;
	private Date startDate;
	private Date endDate;
}
