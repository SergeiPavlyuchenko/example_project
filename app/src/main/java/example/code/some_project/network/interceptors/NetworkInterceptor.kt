package example.code.some_project.network.interceptors

import example.code.some_project.domain.FiveMinutesBlockedException
import example.code.some_project.domain.IncorrectNumberException
import example.code.some_project.domain.NeedUpdateTokensException
import example.code.some_project.domain.NoInternetException
import example.code.some_project.domain.NotEnoughMoneyException
import example.code.some_project.domain.TwentyFourHoursBlockedException
import example.code.some_project.domain.datasource.local.AuthDataSourceLocal
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class NetworkInterceptor(private val authDataSourceLocal: AuthDataSourceLocal) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${authDataSourceLocal.accessToken}")
        val result: Response

        try {
            result = chain.proceed(builder.build())

        } catch (e: Exception) {
            throw NoInternetException()
        } catch (e: SocketTimeoutException) {
            throw NoInternetException()
        } catch (e: SocketException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        } catch (e: UnknownHostException) {
            throw NoInternetException()
        } catch (e: TimeoutException) {
            throw NoInternetException()
        }

//            ToDo move to dataSource
        return when (result.code) {
            in 200..299 -> result
            401 -> throw NeedUpdateTokensException()
            402 -> throw NotEnoughMoneyException()
            422 -> throw IncorrectNumberException()
            423 -> throw TwentyFourHoursBlockedException()
            429 -> throw FiveMinutesBlockedException()
            else -> {
                throw RuntimeException()
            }
        }
    }
}