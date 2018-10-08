package com.acme.airports;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.Stores;

@Component
public class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {    
	//@Autowired
	//private IStoreRepository storeRepository;
	
	@Autowired
	private IStoreService storeService;
    
	public static int counter;
 
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Increment counter");
        counter++;
        
        ObjectMapper mapper = new ObjectMapper();		
		TypeReference<Stores> typeReference = new TypeReference<Stores>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/stores.json");
		try {
			Stores storeList = mapper.readValue(inputStream,typeReference);
			storeService.saveAll(storeList.getStores());
		} catch (IOException e){
			System.out.println("Unable to save users: " + e.getMessage());
		}
    }
}
