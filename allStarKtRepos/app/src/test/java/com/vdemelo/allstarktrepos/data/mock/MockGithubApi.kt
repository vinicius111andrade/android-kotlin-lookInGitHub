package com.vdemelo.allstarktrepos.data.mock

import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.SearchResponse
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.Owner
import java.util.concurrent.atomic.AtomicInteger

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
        perPage: Int
    ): SearchResponse {

        val factory = GithubRepoFactory()
        val items: MutableList<GithubRepo> = mutableListOf()

        for (i in 0..perPage) {
            items.add(factory.createGithubRepo(query))
        }

        return  SearchResponse(
            total = perPage,
            items = items
        )
    }

}
