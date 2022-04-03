package example.code.some_project.data.device

import android.webkit.CookieManager
import io.reactivex.Completable
import example.code.some_project.domain.repo.device.CookiesCleaner

class CookiesCleanerImpl: CookiesCleaner {

    override fun clearCookies(): Completable = Completable.fromAction {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

}