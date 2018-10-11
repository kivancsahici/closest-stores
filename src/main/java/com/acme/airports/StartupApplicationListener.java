package com.acme.airports;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.InputData;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {    
	Logger logger = LoggerFactory.getLogger(StartupApplicationListener.class);

	@Autowired
	private IStoreService storeService;
 
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {                
        ObjectMapper mapper = new ObjectMapper();		
		TypeReference<InputData> typeReference = new TypeReference<InputData>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/stores.json");
		try {
			InputData storeList = mapper.readValue(inputStream,typeReference);
			storeService.saveAll(storeList.getStores());
			logger.info("Initial data was successfully persisted");
		} catch (IOException e){			
			logger.error(String.format("Unable to save users: %s", e.getMessage()));
		}
    }
}
