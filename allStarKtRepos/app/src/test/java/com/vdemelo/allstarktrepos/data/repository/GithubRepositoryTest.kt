package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.PagingData
import com.vdemelo.allstarktrepos.data.mock.MockGithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

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