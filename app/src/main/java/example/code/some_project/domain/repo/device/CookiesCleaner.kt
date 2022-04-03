package example.code.some_project.domain.repo.device

import io.reactivex.Completable

interface CookiesCleaner {
    fun clearCookies(): Completable
}