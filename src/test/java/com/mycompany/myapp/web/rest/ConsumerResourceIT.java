package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Consumer;
import com.mycompany.myapp.repository.ConsumerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConsumerResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ConsumerResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PROCEDURA = "AAAAAAAAAA";
    private static final String UPDATED_PROCEDURA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumerMockMvc;

    private Consumer consumer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .creationDate(DEFAULT_CREATION_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .procedura(DEFAULT_PROCEDURA)
            .enabled(DEFAULT_ENABLED);
        return consumer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createUpdatedEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .procedura(UPDATED_PROCEDURA)
            .enabled(UPDATED_ENABLED);
        return consumer;
    }

    @BeforeEach
    public void initTest() {
        consumer = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsumer() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();
        // Create the Consumer
        restConsumerMockMvc.perform(post("/api/consumers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consumer)))
            .andExpect(status().isCreated());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate + 1);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testConsumer.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testConsumer.getProcedura()).isEqualTo(DEFAULT_PROCEDURA);
        assertThat(testConsumer.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createConsumerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();

        // Create the Consumer with an existing ID
        consumer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerMockMvc.perform(post("/api/consumers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consumer)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConsumers() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get all the consumerList
        restConsumerMockMvc.perform(get("/api/consumers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].procedura").value(hasItem(DEFAULT_PROCEDURA)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get the consumer
        restConsumerMockMvc.perform(get("/api/consumers/{id}", consumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumer.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)))
            .andExpect(jsonPath("$.procedura").value(DEFAULT_PROCEDURA))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingConsumer() throws Exception {
        // Get the consumer
        restConsumerMockMvc.perform(get("/api/consumers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer
        Consumer updatedConsumer = consumerRepository.findById(consumer.getId()).get();
        // Disconnect from session so that the updates on updatedConsumer are not directly saved in db
        em.detach(updatedConsumer);
        updatedConsumer
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .procedura(UPDATED_PROCEDURA)
            .enabled(UPDATED_ENABLED);

        restConsumerMockMvc.perform(put("/api/consumers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConsumer)))
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testConsumer.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testConsumer.getProcedura()).isEqualTo(UPDATED_PROCEDURA);
        assertThat(testConsumer.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc.perform(put("/api/consumers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consumer)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeDelete = consumerRepository.findAll().size();

        // Delete the consumer
        restConsumerMockMvc.perform(delete("/api/consumers/{id}", consumer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
