package repository

import model.Movie

object MovieRepository {
    fun getSampleMovies() = listOf(
        Movie(
            title = "The Shawshank Redemption",
            year = "1994",
            duration = "2h 22m",
            releaseDate = "October 14, 1994",
            genre = "Drama",
            shotDescription = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"
        ),
        Movie(
            title = "The Godfather",
            year = "1972",
            duration = "2h 55m",
            releaseDate = "March 24, 1972",
            genre = "Crime, Drama",
            shotDescription = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
        ),
        Movie(
            title = "The Dark Knight",
            year = "2008",
            duration = "2h 32m",
            releaseDate = "July 18, 2008",
            genre = "Action, Crime, Drama",
            shotDescription = "When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
        ),
        Movie(
            title = "Pulp Fiction",
            year = "1994",
            duration = "2h 34m",
            releaseDate = "October 14, 1994",
            genre = "Crime, Drama",
            shotDescription = "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
        ),
        Movie(
            title = "Forrest Gump",
            year = "1994",
            duration = "2h 22m",
            releaseDate = "July 6, 1994",
            genre = "Drama, Romance",
            shotDescription = "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
        )
    )
}
