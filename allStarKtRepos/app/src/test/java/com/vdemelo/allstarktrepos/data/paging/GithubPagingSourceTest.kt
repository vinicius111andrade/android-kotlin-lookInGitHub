package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingSource.LoadParams.Refresh
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.SearchResponse
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.Owner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class GithubPagingSourceTest {

    lateinit var fakeApi: MockGithubApi
    lateinit var pagingSource: GithubPagingSource
    private val query = "Random"
    private val numberOfItems = 3
    private val itemsPerPage = 5
    private val pageIndex = 1

    @Before
    fun setUp() {
        fakeApi = MockGithubApi()
        pagingSource = GithubPagingSource(MockGithubApi(), query)
    }

    @Test
    fun load() = runBlockingTest {

        val key = if(pageIndex == 1) null else pageIndex - 1

        val a = Page(
            data = fakeApi.searchGithub(
                query = query,
                per_page = numberOfItems
            ).items,
            prevKey = key,
            nextKey = numberOfItems / itemsPerPage
        )

        val b = pagingSource.load(
            Refresh(
                key = key,
                loadSize = numberOfItems,
                placeholdersEnabled = false
            )
        )


        assertEquals(
            expected = Page(
                data = fakeApi.searchGithub(
                    query = query,
                    per_page = numberOfItems
                ).items,
                prevKey = key,
                nextKey = numberOfItems / itemsPerPage
            ),

            actual = pagingSource.load(
                Refresh(
                    key = key,
                    loadSize = numberOfItems,
                    placeholdersEnabled = false
                )
            )
        )



    }

    @Test
    fun getRefreshKey() {
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