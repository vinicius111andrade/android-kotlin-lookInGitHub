package com.vdemelo.allstarktrepos.ui.reposlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class ReposListViewModel: ViewModel() {

    //    private val reposApi = ReposApiService()
    private val disposable = CompositeDisposable()

//    private var _repos = MutableLiveData<List<Repo>>()
//    val repos: LiveData<List<Repo>>
//        get() = _repos

    private val _loadError = MutableLiveData<Boolean>(false)
    val loadError: LiveData<Boolean>
        get() = _loadError

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
//        _loading.postValue(true)
//        disposable.add(
//            reposApi.getRepos()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object: DisposableSingleObserver<List<Repo>>() {
//
//                    override fun onSuccess(repos: List<Repo>) {
//                        _repos.postValue(repos)
//                        _loadError.postValue(false)
//                        _loading.postValue(false)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        _loadError.postValue(true)
//                        _loading.postValue(false)
//                        e.printStackTrace()
//                    }
//                })
//        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}