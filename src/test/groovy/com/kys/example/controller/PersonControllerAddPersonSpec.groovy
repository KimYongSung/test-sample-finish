package com.kys.example.controller

import com.kys.example.common.constants.ErrorCode
import com.kys.example.common.response.DataResponse
import com.kys.example.exception.detail.AlreadyPersonException
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Narrative

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Narrative( value = """
    Person 사용자 등록 테스트 진행
"""
)
class PersonControllerAddPersonSpec extends PersonControllerConfig{

    def "사용자 등록 요청"(){

        given:
        personService.addPerson(_) >> DataResponse.ok(1l)

        def param = MockMvcRequestBuilders.post("/person")
                .param("name", "kody.kim")
                .param("address", "서울시 강북구 수유동")
                .param("age", "32")

        when:
        def resultAction = mockMvc.perform(param)
                .andDo(print())
        then:
        resultAction.andExpect(status().isOk())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0000.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_0000.getMessage()))
        resultAction.andExpect(jsonPath('$.data').isNumber())

    }

    def "이미 등록된 사용자 요청"(){

        given:
        personService.addPerson(_) >> { throw new AlreadyPersonException() }

        def param = MockMvcRequestBuilders.post("/person")
                .param("name", "kody.kim")
                .param("address", "서울시 강북구 수유동")
                .param("age", "32")

        when:
        def resultAction = mockMvc.perform(param)
                .andDo(print())

        then:
        resultAction.andExpect(status().isBadRequest())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_1002.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(ErrorCode.CD_1002.getMessage()))
        resultAction.andExpect(jsonPath('$.data').doesNotExist())
    }

    def "필수값 누락 에러 발생 "(){

        given:
        def param = MockMvcRequestBuilders.post("/person")
                .param("name", name)
                .param("address", address)
                .param("age", age)

        when:
        def resultAction = mockMvc.perform(param)
                .andDo(print())

        then:
        resultAction.andExpect(status().is4xxClientError())
        resultAction.andExpect(jsonPath('$.code').value(ErrorCode.CD_0001.getCode()))
        resultAction.andExpect(jsonPath('$.message').value(message))
        resultAction.andExpect(jsonPath('$.data').doesNotExist())

        where:
        name     | address           | age  || message
        ""       | "서울시 강북구 수유동"  | "32" || "name 은 필수 입니다."
        "김용성"   | ""                | "32" || "address 는 필수 입니다."
        "김용성"   | "서울시 강북구 수유동"  | ""   || "age 는 필수 값 입니다."
        "김용성"   | "서울시 강북구 수유동"  | "0"  || "age 는 0보다 커야 합니다."

    }
}
