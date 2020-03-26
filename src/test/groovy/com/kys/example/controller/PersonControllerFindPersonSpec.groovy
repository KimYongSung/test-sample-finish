package com.kys.example.controller

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.DataResponse
import com.kys.example.dto.PersonDTO
import com.kys.example.exception.detail.PersonNotFoundException
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Narrative

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@Narrative( value = """
    Person 사용자 조회 테스트 진행
"""
)
class PersonControllerFindPersonSpec extends PersonControllerConfig{

    def "name 으로 person 정보 조회"(){

        given: "성공 응답 생성"
        personService.findPersonByName(_) >> DataResponse.ok(personDTO)

        when: "조회 호출"
        def resultAction = mockMvc.perform(MockMvcRequestBuilders.get("/person/{name}", personDTO.getName()))
                                  .andDo(print())
        then: "응답 검증"
        resultAction.andExpect(MockMvcResultMatchers.status().isOk())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.code').value(ErrorCode.CD_0000.getCode()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data').exists())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data.id').value(_id))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data.name').value(_name))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data.address').value(_address))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data.age').value(_age))

        where:
        personDTO                                             || _id | _name       | _address           | _age
        new PersonDTO(1l, "kody.kim", "서울시 강북구 수유동", 32)   || 1l | "kody.kim" | "서울시 강북구 수유동" | 32
    }

    def "미등록된 사용자로 조회 요청"(){

        given: "성공 응답 생성"
        personService.findPersonByName(_) >> { throw new PersonNotFoundException() }

        when: "조회 호출"
        def resultAction = mockMvc.perform(MockMvcRequestBuilders.get("/person/{name}", "kody.kim" ))
                .andDo(print())
        then: "응답 검증"
        resultAction.andExpect(MockMvcResultMatchers.status().isNotFound())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.code').value(ErrorCode.CD_1001.getCode()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(ErrorCode.CD_1001.getMessage()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.data').doesNotExist())

    }
}