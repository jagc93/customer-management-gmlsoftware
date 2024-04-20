package com.gmlsoftware.entity;

import static com.gmlsoftware.constant.ClientConstant.MAX_EMAIL_ADDRESS_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_FIRST_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_LAST_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_MIDDLE_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_PHONE_NUMBER_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_SECOND_LAST_NAME_LENGTH;
import static com.gmlsoftware.constant.ClientConstant.MAX_SHARED_KEY_LENGTH;

import java.util.Date;

import com.gmlsoftware.entity.listener.ClientListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CLIENTES")
@SequenceGenerator(sequenceName = "sec_clientes", name = "clientID", allocationSize = 1)
@EntityListeners({ ClientListener.class })
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "clientID")
	@Column(name = "CLIENTE_ID", unique = true, nullable = false)
	private Long clientID;

	@Column(name = "PRIMER_NOMBRE", nullable = false, length = MAX_FIRST_NAME_LENGTH)
	private String firstName;
	
	@Column(name = "SEGUNDO_NOMBRE", length = MAX_MIDDLE_NAME_LENGTH)
    private String middleName;

	@Column(name = "PRIMER_APELLIDO", nullable = false, length = MAX_LAST_NAME_LENGTH)
	private String lastName;
	
	@Column(name = "SEGUNDO_APELLIDO", length = MAX_SECOND_LAST_NAME_LENGTH)
    private String secondLastName;

	@Column(name = "CLAVE_COMPARTIDA", nullable = false, length = MAX_SHARED_KEY_LENGTH)
	private String sharedKey;

	@Column(name = "CORREO_ELECTRONICO", unique = true, nullable = false, length = MAX_EMAIL_ADDRESS_LENGTH)
    private String emailAddress;

	@Column(name = "TELEFONO", length = MAX_PHONE_NUMBER_LENGTH)
    private String phoneNumber;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_INICIO")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_FIN")
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION", nullable = false)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date modificationDate;
}
