package mashup.data.api

import io.reactivex.rxjava3.core.Single
import mashup.data.model.Signup
import mashup.data.model.Token
import mashup.data.request.LocationRequest
import mashup.data.request.NicknameCheckRequest
import mashup.data.request.SignupRequest
import mashup.data.response.BaseResponse
import retrofit2.http.*

interface UserApi {

    @POST("users/signup")
    fun postSignup(
        @Body request: SignupRequest
    ): Single<BaseResponse<Signup>>

    @POST("users")
    fun postSignUp(
        @Header("Authorization") snsToken: String,
        @Body request: SignupRequest
    ): Single<BaseResponse<Signup>>

    @GET("users")
    fun getUser(): Single<BaseResponse<Signup>>

    @PATCH("users")
    fun putLocation(
        @Body request: LocationRequest
    ): Single<BaseResponse<Signup>>

    @POST("users/refresh")
    fun postRefreshToken(): Single<BaseResponse<Token>>

    @POST("users/nickname/check")
    fun postNickNameCheck(
        @Body request: NicknameCheckRequest
    ): Single<BaseResponse<Boolean>>

    @GET("users/address")
    fun getUserAddress(): Single<BaseResponse<String>>
}