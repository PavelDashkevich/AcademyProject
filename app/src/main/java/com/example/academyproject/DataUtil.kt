package com.example.academyproject

data class Actor(
        var name: String,
        var photo: String
)

data class Movie(
        var name: String,
        var imageInList: String,
        var imageInDetails: String,
        var actors: List<Actor>,
        var rating: Float,
        var storyline: String,
        var reviewsNumber: Int,
        var duration: Int,
        var genre: String,
        var contentRating: String,
        var like: Boolean
)

class DataUtil {
    companion object {
        var movies = generateData()

        fun generateData(): List<Movie> {
            return listOf(
                    Movie(
                            "Dolittle",
                            "pic_movie_image_in_list_dolittle",
                            "pic_movie_image_in_details_dolittle",
                            listOf(
                                    Actor("Robert Downey Jr.", "pic_actor_photo_robert_downey_jr"),
                                    Actor("Antonio Banderas", "pic_actor_photo_antonio_banderas"),
                                    Actor("Michael Sheen", "pic_actor_photo_michael_sheen"),
                                    Actor("Jim Broadbent", "pic_actor_photo_jim_broadbent"),
                                    Actor("Jessie Buckley", "pic_actor_photo_jessie_buckley"),
                                    Actor("Harry Colett", "pic_actor_photo_harry_colett")
                            ),
                            2.3F,
                            "A physician who can talk to animals embarks on an adventure to find a legendary island with a young apprentice and a crew of strange pets.",
                            87,
                            101,
                            "Adventure, Comedy, Family",
                            "PG",
                            true
                    ),
                    Movie(
                            "Underwater",
                            "pic_movie_image_in_list_underwater",
                            "pic_movie_image_in_details_underwater",
                            listOf(
                                    Actor("Kristen Stewart", "pic_actor_photo_kristen_stewart"),
                                    Actor("Vincent Cassel", "pic_actor_photo_vincent_cassel"),
                                    Actor("Mamoudou Athie", "pic_actor_photo_mamoudou_athie"),
                                    Actor("T. J. Miller", "pic_actor_photo_tj_miller"),
                                    Actor("John Gallagher Jr.", "pic_actor_photo_john_gallagher_jr"),
                                    Actor("Jessica Henwick", "pic_actor_photo_jessica_henwick"),
                                    Actor("Gunner Wright", "pic_actor_photo_gunner_wright"),
                                    Actor("Fiona Rene", "pic_actor_photo_fiona_rene"),
                                    Actor("Amanda Troop", "pic_actor_photo_amanda_troop")
                            ),
                            2.6F,
                            "A crew of oceanic researchers working for a deep sea drilling company try to get to safety after a mysterious earthquake devastates their deepwater research and drilling facility located at the bottom of the Mariana Trench.",
                            113,
                            95,
                            "Action, Horror, Sci-Fi",
                            "18+",
                            false
                    ),
                    Movie(
                            "The Call Of The Wild",
                            "pic_movie_image_in_list_the_call_of_the_wild",
                            "pic_movie_image_in_details_the_call_of_the_wild",
                            listOf(
                                    Actor("Harrison Ford", "pic_actor_photo_harrison_ford"),
                                    Actor("Omar Sy", "pic_actor_photo_omar_sy"),
                                    Actor("Cara Gee", "pic_actor_photo_cara_gee"),
                                    Actor("Dan Stevens", "pic_actor_photo_dan_stevens"),
                                    Actor("Bradley Whitford", "pic_actor_photo_bradley_whitford"),
                                    Actor("Jean Louisa Kelly", "pic_actor_photo_jean_louisa_kelly")
                            ),
                            3.4F,
                            "A sled dog struggles for survival in the wilds of the Yukon.",
                            321,
                            119,
                            "Adventure, Drama, Family",
                            "PG",
                            false
                    ),
                    Movie(
                            "Last Christmas",
                            "pic_movie_image_in_list_last_christmas",
                            "pic_movie_image_in_details_last_christmas",
                            listOf(
                                    Actor("Emilia Clarke", "pic_actor_photo_emilia_clarke"),
                                    Actor("Madison Ingoldsby", "pic_actor_photo_madison_ingoldsby"),
                                    Actor("Emma Thompson", "pic_actor_photo_emma_thompson"),
                                    Actor("Boris Isakovic", "pic_actor_photo_boris_isakovich"),
                                    Actor("Maxim Baldry", "pic_actor_photo_maxim_baldry")
                            ),
                            3.3F,
                            "Kate is a young woman subscribed to bad decisions. Working as an elf in a year round Christmas store is not good for the wannabe singer. However, she meets Tom there. Her life takes a new turn. For Kate, it seems too good to be true.",
                            208,
                            121,
                            "Comedy, Drama, Romance",
                            "13+",
                            false
                    ),
                    Movie(
                            "The Invisible Guest",
                            "pic_movie_image_in_list_the_invisible_guest",
                            "pic_movie_image_in_details_the_invisible_guest",
                            listOf(
                                    Actor("Mario Casas", "pic_actor_photo_mario_casas"),
                                    Actor("Ana Wagener", "pic_actor_photo_ana_wagener"),
                                    Actor("Barbara Lennie", "pic_actor_photo_barbara_lennie"),
                                    Actor("Francesc Orella", "pic_actor_photo_francesc_orella"),
                                    Actor("Paco Tous", "pic_actor_photo_paco_tous")
                            ),
                            4F,
                            "A successful entrepreneur accused of murder and a witness preparation expert have less than three hours to come up with an impregnable defense.",
                            173,
                            106,
                            "Crime, Drama, Mystery",
                            "16+",
                            false
                    ),
                    Movie(
                            "Fantasy Island",
                            "pic_movie_image_in_list_fantasy_island",
                            "pic_movie_image_in_details_fantasy_island",
                            listOf(
                                    Actor("Michael Pena", "pic_actor_photo_michael_pena"),
                                    Actor("Maggie Q", "pic_actor_photo_maggie_q"),
                                    Actor("Lucy Hale", "pic_actor_photo_lucy_hale"),
                                    Actor("Austin Stowell", "pic_actor_photo_austin_stowell"),
                                    Actor("Jimmy O. Yang", "pic_actor_photo_jummy_o_yang"),
                                    Actor("Portia Doubleday", "pic_actor_photo_portia_doubleday")
                            ),
                            2.4F,
                            "When the owner and operator of a luxurious island invites a collection of guests to live out their most elaborate fantasies in relative seclusion, chaos quickly descends.",
                            181,
                            109,
                            "Action, Adventure, Fantasy",
                            "13+",
                            false
                    )
            )
        }
    }
}