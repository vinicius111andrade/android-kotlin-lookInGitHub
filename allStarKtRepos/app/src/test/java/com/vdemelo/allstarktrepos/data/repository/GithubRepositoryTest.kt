package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.vdemelo.allstarktrepos.data.mock.MockGithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.paging.GithubPagingSource
import com.vdemelo.allstarktrepos.utils.Constants
import com.vdemelo.allstarktrepos.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito

@ExperimentalCoroutinesApi
class GithubRepositoryTest {

    private lateinit var fakeApi: MockGithubApi
    private lateinit var repository: GithubRepository

    private val query = "Random"

    @Before
    fun setUp() {
        fakeApi = MockGithubApi()
        repository = GithubRepository(fakeApi)
    }

    @Test
    fun returnNonNull() = runBlockingTest {

        val actualResult: PagingData<GithubRepo> = repository.getSearchResultStream(query).first()

        assertNotNull(actualResult)

    }
}