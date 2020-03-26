package com.kys.example.controller

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.PageResponse
import com.kys.example.common.response.Response
import com.kys.example.exception.detail.PersonNotFoundException
import com.kys.example.model.Person
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Narrative

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@Narrative( value = """
    사용자 정보 수정
"""
)
class PersonControllerModifyPersonSpec extends PersonControllerConfig{

    def "필수 값 테스트"(){

        given: "성공 응답 생성"
        def param = MockMvcRequestBuilders.post("/modify/person")
                .param("id", id)
                .param("address", address)
                .param("age", age)

        when: "정보 수정 호출"
        def resultAction = mockMvc.perform(param)
                                  .andDo(print())

        then: "응답 검증"
        resultAction.andExpect(MockMvcResultMatchers.status().isBadRequest())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.code').value(ErrorCode.CD_0001.getCode()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(message))

        where:
        id  |  address           | age  || message
        "1" |  "서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동 서울시 강북구 수유동"                | "32" || "address가 255 자릿수를 초과하였습니다."
        "1" |  "서울시 강북구 수유동"  | "0"  || "age 는 0보다 커야 합니다."
        ""  |  "서울시 강북구 수유동"  | "32"  || "id 는 필수 입니다."
        "0" |  "서울시 강북구 수유동"  | "32"  || "id는 0보다 커야 합니다."
    }

    def "미등록 사용자 정보 수정"(){

        given: "성공 응답 생성"
        personService.modifyPerson(_) >> { throw new PersonNotFoundException()};

        def param = MockMvcRequestBuilders.post("/modify/person")
                .param("id", id)
                .param("address", address)
                .param("age", age)

        when: "정보 수정 호출"
        def resultAction = mockMvc.perform(param)
                .andDo(print())

        then: "응답 검증"
        resultAction.andExpect(MockMvcResultMatchers.status().isNotFound())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.code').value(ErrorCode.CD_1001.getCode()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(ErrorCode.CD_1001.getMessage()))

        where:
        id  | address           | age
        "1"  | "서울시 강북구 수유동"  | "32"
    }

    def "사용자 정보 수정"(){

        given: "성공 응답 생성"
        personService.modifyPerson(_) >> Response.ok();

        def param = MockMvcRequestBuilders.post("/modify/person")
                .param("id", id)
                .param("address", address)
                .param("age", age)

        when: "정보 수정 호출"
        def resultAction = mockMvc.perform(param)
                .andDo(print())

        then: "응답 검증"
        resultAction.andExpect(MockMvcResultMatchers.status().isOk())
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.code').value(ErrorCode.CD_0000.getCode()))
        resultAction.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))

        where:
        id  | address           | age
        "1"  | "서울시 강북구 수유동"  | "32"
    }
}