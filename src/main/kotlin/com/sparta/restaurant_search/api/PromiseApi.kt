package com.sparta.restaurant_search.api

import com.sparta.restaurant_search.domain.dto.PromiseDto
import com.sparta.restaurant_search.security.UserPrincipal
import com.sparta.restaurant_search.service.PromiseService
import com.sparta.restaurant_search.web.reponse.ListResponse
import com.sparta.restaurant_search.web.reponse.SingleResponse
import com.sparta.restaurant_search.web.request.PromiseRequest
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v3/places")
@RequiredArgsConstructor
class PromiseApi (
    private val promiseService: PromiseService
) {
    @PostMapping("/promise")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun makePromise(
        @RequestBody request: PromiseRequest,
        @AuthenticationPrincipal requester: UserPrincipal
    ): ResponseEntity<SingleResponse<String>> {
        promiseService.makePromise(request, requester.id)

        return ResponseEntity(SingleResponse.success("약속 요청 성공!"), HttpStatus.OK)
    }

    @GetMapping("/promise-receive-list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun promiseReceiveList(
        @AuthenticationPrincipal respondent: UserPrincipal
    ): ResponseEntity<ListResponse<PromiseDto>> {
        val receiveList = promiseService.promiseReceiveList(respondent.id)

        return ResponseEntity(ListResponse.successOf(receiveList), HttpStatus.OK)
    }

    @GetMapping("/promise-request-list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun promiseRequestList(
        @AuthenticationPrincipal requester: UserPrincipal
    ): ResponseEntity<ListResponse<PromiseDto>> {
        val requestList = promiseService.promiseRequestList(requester.id)

        return ResponseEntity(ListResponse.successOf(requestList), HttpStatus.OK)
    }

    @PutMapping("/accept-promise/{promiseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun acceptPromise(
        @PathVariable promiseId: Long
    ): ResponseEntity<SingleResponse<String>> {
        promiseService.promiseAccept(promiseId)

        return ResponseEntity(SingleResponse.success("약속 승인 완료!"), HttpStatus.OK)
    }

    @DeleteMapping("/promise/delete/{promiseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun deletePromise(
        @PathVariable promiseId: Long
    ): ResponseEntity<SingleResponse<String>> {
        promiseService.deletePromise(promiseId)

        return ResponseEntity(SingleResponse.success("약속 취소 완료!"), HttpStatus.OK)
    }

}