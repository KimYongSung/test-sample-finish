package com.kys.example.service

import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.model.mapper.PersonMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [ MyBatisConfig.class, DataSourceConfig.class, ApplicationConfig.class, WebMvcConfig.class])
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonServiceConfig extends Specification{

    @SpringBean
    PersonMapper personMapper = Mock()

    @Autowired
    PersonService personService
}
