package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.mock.MockGithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GithubPagingSourceTest {

    private lateinit var fakeApi: MockGithubApi
    private lateinit var pagingSource: GithubPagingSource
    private val query = "Random"

    @Mock
    private lateinit var mockitoApi: GithubApi

    private lateinit var mockitoGithubPagingSource: GithubPagingSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockitoGithubPagingSource = GithubPagingSource(mockitoApi , query)

        fakeApi = MockGithubApi()
        pagingSource = GithubPagingSource(fakeApi, query)
    }

    @Test
    fun reviewsReceivingNull() = runTest {
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

}
