package com.kys.example.service

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.Response
import com.kys.example.dto.PersonRegDTO
import com.kys.example.exception.detail.AlreadyPersonException
import com.kys.example.model.Person
import spock.lang.Narrative

@Narrative( value = """
    사용자 추가 테스트 
"""
)
class PersonServiceAddPersonSpec extends PersonServiceConfig{

    def "이미 등록된 사용자 테스트"(){

        given: "특정 name으로 조회 요청시 동일한 name 의 사용자 정보를 리턴하겠다."
        personMapper.findByName(name) >> Optional.of(new Person(1l, name, address, age))

        when: "사용자 등록 요청"
        personService.addPerson(new PersonRegDTO(name, address, age))

        then: "예외 발생 검증"
        def ex = thrown(exception)
        ex.message == _message

        where:
        name         | address           | age || exception                    | _message
        "kody.kim"   | "서울시 강북구 수유동" | 32   || AlreadyPersonException.class | ErrorCode.CD_1002.getMessage()
    }

    def "사용자 등록 테스트"(){

        given: "특정 name 으로 조회 요청시 null을 리턴하겠다."
        personMapper.findByName(name) >> Optional.empty()

        when: "사용자 등록 요청"
        Response response = personService.addPerson(new PersonRegDTO(name, address, age))

        then: "처리 결과 검증"
        response.isSuccess()
        1 * personMapper.insert(_)

        where:
        name         | address           | age
        "kody.kim"   | "서울시 강북구 수유동" | 32
    }
}
