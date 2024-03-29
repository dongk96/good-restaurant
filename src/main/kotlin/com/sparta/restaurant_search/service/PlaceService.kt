package com.sparta.restaurant_search.service

import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.model.CityResponse
import com.maxmind.geoip2.record.Location
import com.sparta.restaurant_search.domain.dto.BestPlaceDto
import com.sparta.restaurant_search.domain.dto.KeywordDto
import com.sparta.restaurant_search.domain.dto.PlaceDto
import com.sparta.restaurant_search.domain.entity.DeliciousPlace
import com.sparta.restaurant_search.exception.BadRequestException
import com.sparta.restaurant_search.exception.KakaoApiException
import com.sparta.restaurant_search.exception.NaverApiException
import com.sparta.restaurant_search.exception.NotFoundException
import com.sparta.restaurant_search.kakao.KakaoClient
import com.sparta.restaurant_search.naver.NaverClient
import com.sparta.restaurant_search.naver.NaverGeolocation
import com.sparta.restaurant_search.repository.FollowRepository
import com.sparta.restaurant_search.repository.PlaceRepository
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.web.request.*
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.InetAddress
import java.util.concurrent.TimeUnit

@Service
class PlaceService(
    private val kakaoClient: KakaoClient,
    private val naverClient: NaverClient,
    private val naverGeolocation: NaverGeolocation,
    private val redisTemplate: StringRedisTemplate,
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val databaseReader: DatabaseReader,
    private val redissonClient: RedissonClient,
){
    private val lockPrefix = "lock:"


    fun findPlaces(request: String): List<PlaceDto> {
        redisStore(request)
        var answer: List<PlaceDto>
        try {
            val places = kakaoClient.localSearch(SearchKakaoRequest(request))
            answer = PlaceDto.fromKakao(places)
        } catch (kakaoException: KakaoApiException) {
            val places = naverClient.localSearch(SearchNaverRequest(request))
            answer = PlaceDto.fromNaver(places)
        } catch(naverException: NaverApiException) {
            answer = emptyList()
        }
        return answer
    }

    fun findPlacesAround(request: String, ipAddress: String): List<PlaceDto> {

        println(ipAddress)
        val naverGeoSearch = naverGeolocation.searchNaverGeo(ipAddress)
//        val ipAddressBytes = InetAddress.getByName(ipAddress).address
//        val response: CityResponse = databaseReader.city(InetAddress.getByAddress(ipAddressBytes))
//        val location: Location = response.location

        redisStore(request)
        val places = kakaoClient.localSearchWithLocation(SearchKakaoWithLocationRequest(request, naverGeoSearch.lat.toDouble(), naverGeoSearch.long.toDouble()))

        return PlaceDto.fromKakao(places)
    }

    // TODO TEST
    private fun redisStore(request: String) {
        val threadName = Thread.currentThread().name
//        println(threadName)
        val lock = redissonClient.getLock(lockPrefix + request)

        try {
            if (lock.tryLock(30, 10, TimeUnit.SECONDS)) {
                val operations: ValueOperations<String, String> = redisTemplate.opsForValue()

                val value = operations.get(request)

                if (value == null) {
                    operations.set(request, "1")
                } else {
                    // 키가 존재하면 값을 1씩 증가시키기
                    val incrementedValue = (value.toInt() + 1).toString()
                    operations.set(request, incrementedValue)
                }
                println("$threadName : ${operations.get(request)}")
            }

        }catch(e: InterruptedException) {
            Thread.currentThread().interrupt()
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    @Transactional
    fun deliciousClick(request: DeliciousPlaceRequest, userId: Long): Boolean {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }
        val place = placeRepository.findByUserAndPlaceId(user, request.placeId)

        if(place != null) {
            placeRepository.delete(place)
            return false
        }

        val deliciousPlace = DeliciousPlace(
            user,
            request.placeId,
            request.placeName,
            request.placeAddress,
            request.placeUrl
        )

        placeRepository.save(deliciousPlace)
        return true


    }

    fun findMyPlaces(userId: Long): List<PlaceDto> {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val deliciousPlaces = placeRepository.findByUser(user)

        return PlaceDto.fromEntities(deliciousPlaces)
    }

    fun findFollowingUserPlaces(hostId: Long, userId: Long): List<PlaceDto> {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val host = userRepository.findById(hostId).orElseThrow {
            NotFoundException("회원이 존재하지 않습니다.")
        }

        val followCheck = followRepository.findByFollowerAndHost(user, host)
        if(!followCheck.statement) throw BadRequestException("팔로우 승인 대기 상태입니다.")

        val followingUserPlaces = placeRepository.findByUser(host)

        return PlaceDto.fromEntities(followingUserPlaces)
    }

    fun findBestPlaces(): List<BestPlaceDto> {
        return placeRepository.findBestPlaceList()
    }


    // TODO TEST
    fun findBestKeywords(): List<KeywordDto> {
        val operations: ValueOperations<String, String> = redisTemplate.opsForValue()

        val allKeys: Set<String> = redisTemplate.keys("*")

        val keywords: List<KeywordDto> = allKeys.mapNotNull { key ->
            val value = operations.get(key)
            value?.let {
                try {
                    KeywordDto(key, it.toString().toLong())
                } catch (e: NumberFormatException) {
                    null
                }
            }
        }

        // 값으로 내림차순 정렬하여 상위 5개 가져오기
        val top5Keywords = keywords.sortedByDescending { it.count }.take(5)

        return top5Keywords
    }


}