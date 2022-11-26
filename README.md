# android-kotlin-lookInGitHub
This is an app that allows you to search and scroll through a list of GitHub repositories in descendent order by number of stars. You can see the repository's title, number of stars, forks, the owners picture and his name. Also, by clicking on the repository's card you can access it on your web browser or GitHub app.

# Components Used
- Paging 3
- Retrofit 2
- Okhttp 3
- Glide
- View Binding
- Coroutines
- Flow
- Timber

# For Testing
- Mockito
- JUnit 4

#Design Decisions

## How I structured ViewGroups
	I follow a simple basic guide: if the viewgroup has only one child I use a FrameLayout, if it has childs lined up I use LinearLayout, if it has a different arrangement I use a ConstraintLayout. This way we maximize efficiency and minimize complexity. Also, one should always try not to use a ViewGroup inside another one, specially a LinearLayout inside a LinearLayout, you will create needless performance issues.

## Why I don't use Data Class
	A data class has four methods that a common class doesn't, and they can be useful. They are: equals()/hashCode(), toString(), componentN(), copy(). If you're not going to use any of those methos there is no reason to not use just a common class. And actually in a large project it's not a good idea to flood the code base with needless data classes, because it adds up to compilation time. So, if a won't use any of the extra methods there is no reason to use a data class.

## Why I removed Data Binding from the project
	Data binding enables bad architectural practices by allowing business logic to be hidden inside XMLs and Binding Adapters. This is the reason I removed it. Also, it creates lots of generated classes so it can do it's magic, which in a large scale project will increase build time.