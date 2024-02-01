package com.sparta.restaurant_search.service

import com.sparta.restaurant_search.domain.dto.PromiseDto
import com.sparta.restaurant_search.domain.entity.Promise
import com.sparta.restaurant_search.exception.NotFoundException
import com.sparta.restaurant_search.repository.PlaceRepository
import com.sparta.restaurant_search.repository.PromiseRepository
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.web.reponse.ListResponse
import com.sparta.restaurant_search.web.request.PromiseRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PromiseService (
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository,
) {
    @Transactional
    fun makePromise(request: PromiseRequest, requesterId: Long) {
        val place = placeRepository.findByPlaceName(request.placeName)
            ?: throw NotFoundException("존재하지 않는 식당입니다.")

        val requester = userRepository.findById(requesterId).orElseThrow{
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val respondent = userRepository.findById(request.respondentId).orElseThrow{
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val promise = Promise(place, requester, respondent)
        promiseRepository.save(promise)
    }

    fun promiseReceiveList(respondentId: Long): List<PromiseDto> {
        val respondent = userRepository.findById(respondentId).orElseThrow{
            NotFoundException("회원이 존재하지 않습니다.")
        }
        val receiveList = promiseRepository.findReceiveList(respondent)

        return PromiseDto.fromReceiveEntities(receiveList)
    }

    fun promiseRequestList(requesterId: Long): List<PromiseDto> {
        val requester = userRepository.findById(requesterId).orElseThrow{
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val requestList = promiseRepository.findRequestList(requester)

        return PromiseDto.fromRequestEntities(requestList)
    }

    @Transactional
    fun promiseAccept(promiseId: Long) {
        val promise = promiseRepository.findById(promiseId).orElseThrow {
            NotFoundException("약속이 존재하지 않습니다.")
        }

        promise.updateState()
    }

    @Transactional
    fun deletePromise(promiseId: Long) {
        val promise = promiseRepository.findById(promiseId).orElseThrow {
            NotFoundException("약속이 존재하지 않습니다.")
        }

        promiseRepository.delete(promise)
    }
}