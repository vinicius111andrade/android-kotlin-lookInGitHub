# android-kotlin-lookInGitHub
This is an app that allows you to search and scroll through a list of GitHub repositories in descendent order by number of stars. You can see the repository's title, number of stars, forks, the owners picture and his name. Also, by clicking on the repository's card you can access it on your web browser or GitHub app.

# Components Used
- Paging 3
- Retrofit 2
- Okhttp 3
- Glide
- View Binding
- Data Binding
- Coroutines
- Flow
- Timber

# For Testing
- Mockito
- JUnit 4

#Design Decisions

## Why I don't use Data Class
	A data class has four methods that a common class doesn't, and they can be useful. They are: equals()/hashCode(), toString(), componentN(), copy(). If you're not going to use any of those methos there is no reason to not use just a common class. And actually in a large project it's not a good idea to flood the code base with needless data classes, because it adds up to compilation time. So, if a won't use any of the extra methods there is no reason to use a data class.
