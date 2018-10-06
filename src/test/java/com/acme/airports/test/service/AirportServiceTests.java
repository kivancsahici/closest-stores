package com.acme.airports.test.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AirportServiceTests {
	@Autowired
    private MockMvc mockMvc;
	
	@Test
    public void getReport() throws Exception {

       this.mockMvc.perform(get("/report")).andDo(print()).andExpect(status().isOk())
       //test response structure
       .andExpect(jsonPath("$.max").isArray()).andExpect(jsonPath("$.min").isArray())
       //test response content
       .andExpect(jsonPath("$.max[0].id").value("21501")).andExpect(jsonPath("$.max[0].name").value("United States"))
       .andExpect(jsonPath("$.max[0].runwayIdentifications").value("H1, 18, 9, 17, 16, 13, 8, 14, 15, 1"))
       .andExpect(jsonPath("$.max[1].id").value("3839")).andExpect(jsonPath("$.max[1].name").value("Brazil"))
       .andExpect(jsonPath("$.max[1].runwayIdentifications").value("9, 18, 3, 12, 2, 6, 1, 7, 11, 10"))
       .andExpect(jsonPath("$.max[0].runwayTypes").value("GRVL, ASPH-E, ASPH-F, ASPH-G, TURF-DIRT-P, ROOFTOP, ASPH-GRVL-P, ASPH-L, METAL, GRVL-TURF, GRVL-DIRT-F, GRVL-DIRT-G, TURF-F, WATER-E, TURF-G, WATER-G, GRVL-DIRT, DIRT-TRTD, DIRT-TURF-G, GRVL-DIRT-P, WATER, PEM, TURF-P, DIRT-TURF-F, DIRT-TURF, ASPH-P, PER, CLA, GRAVEL-E, GRAVEL-F, GRAVEL-G, TURF-E, ALUM, ASPH-DIRT-G, NEOPRENE, ALUMINUM, GRAVEL, GRASS / SOD, GRAVEL-P, DIRT-SAND, CONC-GRVL, MATS-G, PFC, NSTD, STEEL-CONC, MATS, GRVL-TRTD-F, TURF-TRTD-G, CORAL, GRVL-DIRT-E, TREATED, CONC-TURF-F, CONC-TURF-G, ASPH-DIRT-P, ASPH-CONC-F, ASPH-CONC-G, GRVL-TRTD-P, BRICK, CONC, ASPH-GRVL, ASPH-CONC-P, UNKNOWN, ASPH-TRTD-G, ASPH-TRTD-F, BIT, ASP/CONC, ALUM-DECK, ASPH-TRTD-P, NATURAL SOIL, NONE, TURF-GRVL, GVL, SAND-F, PIERCED STEEL PLANKING / LANDING MATS / MEMBRANES, C, CALICHE, GRAVEL, GRASS / SOD, U, GRVL-E, GRVL-TURF-F, GRVL-F, GRVL-G, GRAVEL / CINDERS / CRUSHED ROCK / CORAL/SHELLS / SLAG, ASP, ASPH/GRVL-F, DIRT-GRVL-F, COM, CON, ASPH-CONC, GRVL-P, COR, COP, SOD, GRASS / SOD, GRAVEL, DIRT-GRVL, DIRT-GRVL-P, ICE, TRTD, ASPH-TURF-E, ASPH-TURF-F, ASPH-TURF-G, GRVL-TURF-P, TURF, OILED DIRT, GRVL-TURF-G, ASPH-TURF-P, DIRT-GRVL-G, LAT, DIRT-F, DIRT-G, DIRT-E, NATURAL SOIL, GRASS / SOD, SAND, CONCRETE, TRTD-DIRT, DIRT, CONC-GRVL-G, DIRT-P, TREATED-E, TREATED-F, TREATED-G, TURF-SAND-F, GRASS, CONC-E, CONC-F, CONC-G, ASPH/ CONC, CONC-TRTD, ASPHALT, CONC-TURF, CONC-P, ASPH/CONC, DECK, ASPH, STEEL, GRAVEL, TRTD, PSP, WOOD, ASPH-TRTD, TURF-GRVL-F, TURF-GRVL-G, ASPH-TURF, GRE, ROOF-TOP, TURF-GRVL-P, TRTD-DIRT-F, OIL&CHIP-T-G, GRS, TRTD-DIRT-P, ASPH-DIRT, GRASS-F, UNK, CONC-TRTD-G, GRASS / SOD, NATURAL SOIL, TURF-DIRT-F, TURF-DIRT-G, GRVL-TRTD, ASPH-GRVL-F, ASPH-GRVL-G, TURF-DIRT"))
       .andExpect(jsonPath("$.max[6].runwayTypes").value("CON, UNK, STONE, CONCRETE, GRE, MET, ASP"))
       .andExpect(jsonPath("$.max[5].runwayTypes").value("CON, GRASS - HERBE  (AVION), PAVED, CONCRETE, GRS, UNKNOWN, UNPAVED, GRASS - HERBE -> AVION - ULM, MAC, UNK, TURF, GRASS, PEM, GRASS - HERBE, GRASS - HERBE  (PLANEUR), ASP, ASPHALT"));
	}
	
	@Test
    public void findAll() throws Exception {

       this.mockMvc.perform(get("/countries")).andDo(print()).andExpect(status().isOk())
       //test response structure
       .andExpect(jsonPath("$").isArray())
       //test response size
       .andExpect(jsonPath("$", hasSize(247)))
       //test response content
       .andExpect(jsonPath("$.[0].code").value("AD"))
       .andExpect(jsonPath("$.[0].name").value("Andorra"))
       .andExpect(jsonPath("$.[246].code").value("ZZ"))
       .andExpect(jsonPath("$.[246].name").value("Unknown or unassigned country"));
    }
	

	@Test
    public void findByName() throws Exception {
		this.mockMvc.perform(get("/countries/Netherlands")).andDo(print()).andExpect(status().isOk())
		//test response content
		.andExpect(jsonPath("$.code").value("NL"))
		.andExpect(jsonPath("$.name").value("Netherlands"))
		.andExpect(jsonPath("$.airports").isArray())
		.andExpect(jsonPath("$..airports[?(@.id==2527)].name").value("Valkenburg Naval Air Base"))
		.andExpect(jsonPath("$..airports[?(@.id==2527)].runways").isArray())
		.andExpect(jsonPath("$..airports[?(@.id==2527)].runways[0].surface").value("ASP"))
		.andExpect(jsonPath("$..airports[?(@.id==2527)].runways[1].airport_ident").value("EHVB"))
		;       
    }

}
