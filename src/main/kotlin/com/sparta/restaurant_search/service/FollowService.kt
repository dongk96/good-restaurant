package com.sparta.restaurant_search.service

import com.sparta.restaurant_search.domain.dto.FollowDto
import com.sparta.restaurant_search.domain.entity.Follow
import com.sparta.restaurant_search.domain.entity.User
import com.sparta.restaurant_search.exception.NotFoundException
import com.sparta.restaurant_search.repository.FollowRepository
import com.sparta.restaurant_search.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun follow(hostId: Long, followerId: Long) {
        val host = userRepository.findById(hostId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val follower = userRepository.findById(followerId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val result = Follow(follower, host)

        followRepository.save(result)
    }

    //내가 팔로우 하는 유저 리스트
    fun followingList(followerId: Long): List<FollowDto> {
        //팔로워 유저 데이터 추출
        val follower = userRepository.findById(followerId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        //팔로워 유저 기준 팔로우 데이터 리스트
        val followingList = followRepository.findFollowingList(follower)

        return FollowDto.fromFollowingEntities(followingList)
    }

    fun standByFollowerList(hostId: Long): List<FollowDto> {
        //팔로잉 유저 데이터 추출
        val host = userRepository.findById(hostId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        //팔로잉 유저 기준 팔로우 데이터 리스트
        val followerList = followRepository.findFollowerListStandBy(host)

        return FollowDto.fromFollowerEntities(followerList)
    }

    //나를 팔로우 하는 유저 리스트
    fun acceptFollowerList(hostId: Long): List<FollowDto> {
        //팔로잉 유저 데이터 추출
        val host = userRepository.findById(hostId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        //팔로잉 유저 기준 팔로우 데이터 리스트
        val followerList = followRepository.findFollowerListAccept(host)

        return FollowDto.fromFollowerEntities(followerList)
    }

    //팔로우 승인
    @Transactional
    fun followAccept(followId: Long) {
        val follow = followRepository.findById(followId).orElseThrow {
            NotFoundException("해당 팔로우 요청이 존재하지 않습니다.")
        }
        follow.updateState()
    }

    @Transactional
    fun followDelete(followerId: Long, hostId: Long) {
        val host = userRepository.findById(hostId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val follower = userRepository.findById(followerId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val findFollow = followRepository.findByFollowerAndHost(follower, host)
        followRepository.delete(findFollow)

    }
}