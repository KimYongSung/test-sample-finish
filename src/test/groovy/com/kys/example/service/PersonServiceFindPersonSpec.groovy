package com.kys.example.service

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.DataResponse
import com.kys.example.dto.PersonDTO
import com.kys.example.exception.detail.PersonNotFoundException
import com.kys.example.model.Person
import spock.lang.Narrative

@Narrative( value = """
    사용자 조회 테스트 
"""
)
class PersonServiceFindPersonSpec extends PersonServiceConfig{

    def "person 이름으로 조회"(){

        given: "특정 name으로 조회 요청시 동일한 name 의 사용자 정보를 리턴하겠다."
        personMapper.findByName(name) >> Optional.of(new Person(null, name, address, age))

        when: "특정 name으로 고객정보 조회"
        DataResponse<PersonDTO> response = personService.findPersonByName(name);

        then: "조회된 고객정보 검증"
        response.isSuccess()
        response.getData().getName() == _name
        response.getData().getAddress() == _address
        response.getData().getAge() == _age

        where:
        name         | address           | age || _name       | _address           | _age
        "kody.kim"   | "서울시 강북구 수유동" | 32  || "kody.kim"  | "서울시 강북구 수유동"  | 32
        "kody.kim1"  | "서울시 강북구 수유동" | 32  || "kody.kim1" | "서울시 강북구 수유동"  | 32
        "kody.kim2"  | "서울시 강북구 수유동" | 32  || "kody.kim2" | "서울시 강북구 수유동"  | 32
    }

    def "등록된 사용자가 아닌 경우"(){

        given: "특정 name으로 조회 요청시 null을 리턴하겠다."
        def name = "kody.kim";
        personMapper.findByName(name) >> Optional.empty()

        when: "특정 name으로 고객정보 조회"
        personService.findPersonByName(name);

        then: "조회된 고객정보 검증"
        def ex = thrown(PersonNotFoundException.class)
        ex.message == ErrorCode.CD_1001.getMessage()
    }
}
