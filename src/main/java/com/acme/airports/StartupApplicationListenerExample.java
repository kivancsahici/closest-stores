package com.acme.airports;


import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acme.airports.model.Stores;
import com.acme.airports.repository.StoreRepository;

@Component
public class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {    
	@Autowired
	private StoreRepository storeRepository;
    
	public static int counter;
 
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Increment counter");
        counter++;
        
        ObjectMapper mapper = new ObjectMapper();		
		TypeReference<Stores> typeReference = new TypeReference<Stores>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/stores.json");
		try {
			Stores users = mapper.readValue(inputStream,typeReference);
			storeRepository.saveAll(users.getStores());
			System.out.println("Users Saved!");
		} catch (IOException e){
			System.out.println("Unable to save users: " + e.getMessage());
		}
    }
}
