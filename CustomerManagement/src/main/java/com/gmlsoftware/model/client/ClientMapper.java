package com.gmlsoftware.model.client;

import org.mapstruct.Mapper;

import com.gmlsoftware.config.MyMapperConfig;
import com.gmlsoftware.entity.Client;
import com.gmlsoftware.model.GenericMapper;

@Mapper(config = MyMapperConfig.class)
public interface ClientMapper
	extends GenericMapper<ClientRequest, Client, ClientDto> {

}
