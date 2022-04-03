package example.code.some_project.presentation.mvp.util

class LazyLoadHelper(
    private val pageItemsCount: Int = 10,
    private val firstPageNumber: Int = 0
) {

    var pagesLoaded: Int = 0
        private set
    var allLoaded = false
        private set

    fun getInfo(): LazyLoadInfo? =
        if (allLoaded) {
            null
        } else {
            LazyLoadInfo(pagesLoaded, pageItemsCount, firstPageNumber)
        }

    fun onPageLoaded(page: List<Any>) {
        pagesLoaded++
        if (page.size < pageItemsCount) {
            allLoaded = true
        }
    }

    fun reset() {
        pagesLoaded = 0
        allLoaded = false
    }

}

data class LazyLoadInfo(
    val pagesLoaded: Int,
    val pageSize: Int,
    val firstPageNumber: Int
) {
    val pageNumber: Int get() = pagesLoaded + firstPageNumber
    val count: Int get() = pageSize
    val offset: Int = pagesLoaded * pageSize
}