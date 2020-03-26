package com.kys.example.model.mapper


import com.kys.example.config.ApplicationConfig
import com.kys.example.config.DataSourceConfig
import com.kys.example.config.MyBatisConfig
import com.kys.example.config.WebMvcConfig
import com.kys.example.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [ MyBatisConfig.class, DataSourceConfig.class, ApplicationConfig.class, WebMvcConfig.class])
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PersonMapperSpec extends Specification{

    @Autowired
    private PersonMapper mapper

    def "정상적으로 person 정보가 저장되어야 한다."(){

        given: "고객 정보 설정"
        def person = new Person(null, "kody.kim", "서울시 강북구 수유동", 32)

        when: "저장 호출"
        mapper.insert(person)

        then: "id 값이 정상적으로 설정되었는지 확인"
        Objects.nonNull(person.getId())
        person.getId() > 0l

        cleanup: "고객정보 삭제"
        mapper.delete(person)

    }

    def "이름으로 person 정보가 정상적으로 조회되야 한다."(){

        given: "테스트를 위해 _age 정보 저장"
        mapper.insert(person)

        when: "name으로 _age 정보 조회"
        Optional<Person> optional = mapper.findByName(_name)

        then: "fildPerson 정보 검증"
        optional.isPresent()
        Person findPerson = optional.get();
        findPerson.getName() == _name
        findPerson.getAddress() == _address
        findPerson.getAge() == _age

        cleanup: "고객정보 삭제"
        mapper.delete(person)

        where:
        person                                            || _name       | _address          | _age
        new Person(null,"kody.kim", "서울시 강북구 수유동", 32) || "kody.kim"  | "서울시 강북구 수유동" | 32
    }

    def "id로 person 정보가 조회되야 한다."(){

        given: "테스트를 위한 고객 정보 저장"
        mapper.insert(person)

        when: "id로 고객정보 조회"
        Optional<Person> optional = mapper.findById(person.getId())

        then: "저장한 고객정보와 조회된 고객정보 검증"
        optional.isPresent()
        Person findPerson = optional.get();
        findPerson.getName() == _name
        findPerson.getAddress() == _address
        findPerson.getAge() == _age

        cleanup:
        mapper.delete(person)

        where:
        person                                            || _name       | _address          | _age
        new Person(null,"kody.kim", "서울시 강북구 수유동", 32) || "kody.kim"  | "서울시 강북구 수유동" | 32
    }

    def "id로 person 정보가 삭제되어야 한다."(){

        given: "테스트를 위한 고객 정보 저장"
        Person person = new Person(null,"kody.kim", "서울시 강북구 수유동", 32)
        mapper.insert(person)

        when: "고객정보 삭제"
        mapper.delete(person)

        then: "삭제 여부 확인"
        mapper.findById(person.getId())
              .isPresent() == false

    }

    def "id로 person 정보가 수정되어야 한다."(){

        given: "테스트를 위한 고객 정보 저장"
        mapper.insert(person)

        updatePerson.setId(person.getId())

        when: "고객정보 수정"
        mapper.update(updatePerson)

        then: "수정 여부 확인"
        Person findPerson = mapper.findById(person.getId()).get()
        findPerson.getId() == updatePerson.getId()
        findPerson.getAddress() == updatePerson.getAddress()
        findPerson.getAge() == updatePerson.getAge()

        where:
        person                                             ||  updatePerson
        new Person(null,"kody.kim1", "서울시 강북구 수유동", 32) ||  new Person(null,null, "서울시 강북구 수유동", 30)
        new Person(null,"kody.kim2", "서울시 강북구 수유동", 32) ||  new Person(null,null, "서울시 강북구 인수동", 32)
    }

    def "id로 count가 조회되야 한다."(){

        given: "테스트를 위한 고객 정보 저장"
        mapper.insert(person)

        when: "고객정보 수정"
        int count = mapper.countById(person.getId())

        then: "수정 여부 확인"
        count == _count

        where:
        person                                             ||  _count
        new Person(null,"kody.kim1", "서울시 강북구 수유동", 32) ||  1
    }
}
