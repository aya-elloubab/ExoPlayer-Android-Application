package ma.ensaj.exoplayerproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ma.ensaj.exoplayerproject.classes.Media

class MediaViewModel : ViewModel() {
    private val _mediaList = MutableLiveData<List<Media>>()
    val mediaList: LiveData<List<Media>> = _mediaList

    private var originalMediaList: List<Media> = emptyList()
    private val _showFavoritesOnly = MutableLiveData(false)

    fun setInitialMediaList(list: List<Media>) {
        originalMediaList = list
        _mediaList.value = list
    }

    fun filterMediaByTitle(query: String) {
        val filteredList = originalMediaList.filter {
            it.title.contains(query, ignoreCase = true)
        }
        _mediaList.value = if (_showFavoritesOnly.value == true) {
            filteredList.filter { it.favorite }
        } else {
            filteredList
        }
    }

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !(_showFavoritesOnly.value ?: false)
        _mediaList.value = if (_showFavoritesOnly.value == true) {
            originalMediaList.filter { it.favorite }
        } else {
            originalMediaList
        }
    }

    fun toggleFavorite(media: Media) {
        originalMediaList = originalMediaList.map {
            if (it.id == media.id) it.copy(favorite = !it.favorite) else it
        }
        filterMediaByTitle("") // Update current filter state
    }
}
