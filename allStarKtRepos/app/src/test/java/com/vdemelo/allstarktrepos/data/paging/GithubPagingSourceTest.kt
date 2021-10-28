package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.SearchResponse
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.Owner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class GithubPagingSourceTest {

    val query = "Random"

    lateinit var githubRepoFactory: GithubRepoFactory
    lateinit var myMockApi: GithubApi

    @Mock lateinit var mockitoApi: GithubApi

    lateinit var githubPagingSourceMyApi: GithubPagingSource
    lateinit var githubPagingSourceMockitoApi: GithubPagingSource

    @Before
    fun setup() {
        myMockApi = MockGithubApi()

        MockitoAnnotations.openMocks(this)

        githubPagingSourceMyApi = GithubPagingSource(myMockApi , query)
        githubPagingSourceMockitoApi = GithubPagingSource(mockitoApi , query)
    }

    // This test doesn't trigger Try Catch, that's why it fails
    @Test
    fun reviews404LoadError() = runBlockingTest {

        val error = RuntimeException("404", Throwable())
        given(mockitoApi.searchGithub()).willThrow(error)

        val expectedResult = PagingSource.LoadResult.Error<Int, GithubRepo>(error)
        assertEquals(
            expectedResult, githubPagingSourceMockitoApi.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    // This test doesn't trigger Try Catch, that's why it fails
    @Test
    fun reviewsReceivingNull() = runBlockingTest {
        given(mockitoApi.searchGithub()).willReturn(null)

        val expectedResult = PagingSource.LoadResult.Error<Int, GithubRepo>(NullPointerException())
        assertEquals(
            expectedResult.toString(), githubPagingSourceMockitoApi.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }

    @Test
    fun `reviews paging source refresh - success`() = runBlockingTest {
        given(mockitoApi.searchGithub()).willReturn(reviewsResponse)
        val expectedResult = PagingSource.LoadResult.Page(
            data = reviewsResponse.reviews.map { Review(it) },
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult, reviewsPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )


    }

class GithubRepoFactory {
    private val counter = AtomicInteger(0)
    fun createGithubRepo(query : String) : GithubRepo {
        val id = counter.incrementAndGet()

        return GithubRepo(
            id = id.toLong(),
            name = "name_$id",
            fullName = "fullName_$id",
            owner = Owner(
                login = "Owner Name $id",
                avatarUrl = "url to some image $id"
            ),
            stargazersCount = 150 * id,
            forksCount = 5 * id,
            description = "description_$id",
            html_url = "html_url_$id",
            language = "language_$id"
        )

    }
}

class MockGithubApi: GithubApi {

    override suspend fun searchGithub(
        query: String,
        sort: String,
        page: Int,
        per_page: Int
    ): SearchResponse {

        val factory = GithubRepoFactory()
        val items: MutableList<GithubRepo> = mutableListOf()

        for (i in 0..per_page) {
            items.add(factory.createGithubRepo(query))
        }

        return  SearchResponse(
            total = per_page,
            items = items
        )
    }

}