package com.kys.example.service

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.Response
import com.kys.example.dto.PersonModifyDTO
import com.kys.example.exception.detail.PersonNotFoundException
import spock.lang.Narrative

@Narrative( value = """
    사용자 수정 테스트 
"""
)
class PersonServiceModifyPersonSpec extends PersonServiceConfig{

    def "미등록 사용자 정보 수정 요청"(){

        given: "요청한 id로 등록된 유저가 없다."
        personMapper.countById(1l) >> 0

        when: "고객정보 수정"
        Response response = personService.modifyPerson(new PersonModifyDTO(1l, "서울시 강북구 우이동", 30) );

        then: "예외 처리 확인"
        def ex = thrown(PersonNotFoundException.class)
        ex.message == ErrorCode.CD_1001.getMessage()

    }

    def "사용자 정보 수정 요청"(){

        given: "요청한 id로 등록된 유저가 있다."
        personMapper.countById(1l) >> 1

        when: "고객정보 수정"
        Response response = personService.modifyPerson(new PersonModifyDTO(1l, "서울시 강북구 우이동", 30) );

        then: "성공 처리 및 update 호출 검증"
        response.isSuccess()
        1 * personMapper.update(_)
    }
}
