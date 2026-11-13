package plat.lab7.data.remote

import kotlinx.coroutines.delay
import retrofit2.HttpException

private const val MAX_ATTEMPTS = 5
private const val BACKOFF_MS = 2000L

// Downloads every page of an endpoint: each page gives the URL of the next one, until it is null.
// The API rate-limits bursts (HTTP 429), so a rate-limited page is retried with a growing pause.
suspend fun <T> fetchAllPages(firstUrl: String, fetchPage: suspend (String) -> ApiPage<T>): List<T> {
    val all = mutableListOf<T>()
    var url: String? = firstUrl
    while (url != null) {
        val page = fetchPageWithRetry(url, fetchPage)
        all += page.results
        url = page.info.next
    }
    return all
}

private suspend fun <T> fetchPageWithRetry(url: String, fetchPage: suspend (String) -> ApiPage<T>): ApiPage<T> {
    var attempt = 1
    while (true) {
        try {
            return fetchPage(url)
        } catch (e: HttpException) {
            if (e.code() != 429 || attempt >= MAX_ATTEMPTS) throw e
            delay(BACKOFF_MS * attempt)
            attempt++
        }
    }
}
