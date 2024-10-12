package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Movie
import repository.MovieRepository

class MovieViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        _movies.value = MovieRepository.getSampleMovies()
    }
}