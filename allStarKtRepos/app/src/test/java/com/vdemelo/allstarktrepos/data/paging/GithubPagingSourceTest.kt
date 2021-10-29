package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.SearchResponse
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.Owner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class GithubPagingSourceTest {

    lateinit var fakeApi: MockGithubApi
    lateinit var pagingSource: GithubPagingSource
    private val query = "Random"
    private val numberOfItems = 2
    private val itemsPerPage = 3
    private val pageIndex = 1

    @Mock
    lateinit var mockitoApi: GithubApi

    lateinit var mockitoGithubPagingSource: GithubPagingSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockitoGithubPagingSource = GithubPagingSource(mockitoApi , query)

        fakeApi = MockGithubApi()
        pagingSource = GithubPagingSource(fakeApi, query)
    }

    @Test
    fun reviewsReceivingNull() = runBlockingTest {
        given(mockitoApi.searchGithub()).willReturn(null)

        val expectedResult = PagingSource.LoadResult.Error<Int, GithubRepo>(NullPointerException())

        val actualResult = mockitoGithubPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult.toString(), actualResult.toString())

    }

    @Test
    fun loadRefresh() = runBlockingTest {

        val key = if(pageIndex == 1) null else pageIndex - 1

        val expectedResult = PagingSource.LoadResult.Page(
            data = fakeApi.searchGithub(
                query = query,
                per_page = numberOfItems
            ).items,
            prevKey = key,
            nextKey = pageIndex + (numberOfItems / itemsPerPage)
        )

        val actualResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = key,
                loadSize = numberOfItems,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult.toString(), actualResult.toString())

    }

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