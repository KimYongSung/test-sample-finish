package com.kys.example.controller

import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.controller.handler.GlobalRestExceptionHandler
import com.kys.example.service.PersonService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.filter.CharacterEncodingFilter
import spock.lang.Specification

@ContextConfiguration(classes = [ MyBatisConfig.class, DataSourceConfig.class, ApplicationConfig.class, WebMvcConfig.class])
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonControllerConfig extends Specification {

    @SpringBean
    PersonService personService = Mock()

    @Autowired
    PersonController personController

    MockMvc mockMvc

    def setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build()
    }
}
