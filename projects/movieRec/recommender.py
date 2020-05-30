import requests
import json


class Recommender:
    def __init__(self, result_list):
        self.result_list = result_list

    # takes in a result_list and returns a list that only contains the 'like' movies
    def get_liked(self, result_list):
        liked = []
        for pair in result_list:
            if pair[1] == 'like':
                liked.append(pair[0])
        return liked

    # takes in a result_list and returns a list that only contains the 'dislike' movies
    def get_disliked(self, result_list):
        liked = []
        for pair in result_list:
            if pair[1] == 'dislike':
                liked.append(pair[0])
        return liked

    # takes in a Movie object and returns a list of recommended Movie objects
    def get_recommended(self, movie):
        movie_id = movie["id"]

        recommended = []

        for page in range(1, 6):
            # get data using API
            response = requests.get("https://api.themoviedb.org/3/movie/" + str(movie_id) +
                                    "/recommendations?api_key=3dbf9b55bdf367747f40974789edbff7&language=en-US&page=" + str(page))

            # convert data to dictionary
            dictionary = json.loads(response.text)

            # get list of dictionaries of movies
            rec_movies = dictionary["results"]

            # add movies as dictionaries to recommended list
            for movie in rec_movies:
                recommended.append({
                    "popularity": movie["popularity"],
                    "poster_path": movie["poster_path"],
                    "id": movie["id"],
                    "original_title": movie["original_title"],
                    "title": movie["title"],
                    "overview": movie["overview"],
                    "release_date": movie["release_date"]
                })

        return recommended

    def get_result(self):
        # for each 'liked' movie, create lists of recommended movies
        # iterate through each list and add it to a hashmap/dictionary
        # where the key is the movie id and the value is the number of time
        # the movie has shown up in the reommended list
        # return the value in the dictionary with the highest number of occurences

        liked_movies = self.get_liked(self.result_list)
        disliked_movies = self.get_disliked(self.result_list)

        # lists of lists of recommended movies
        recommended_lists = []

        # lists of lists of not recommended movies
        not_recommended_lists = []

        for liked in liked_movies:
            recommended_lists.append(self.get_recommended(liked))

        for disliked in disliked_movies:
            not_recommended_lists.append(self.get_recommended(disliked))

        combine_recs = {}

        for each_rec_list in recommended_lists:
            for ele in each_rec_list:
                if (ele["id"] in combine_recs):
                    current_val = combine_recs[ele["id"]]
                    del combine_recs[ele["id"]]
                    combine_recs[ele["id"]] = current_val+1
                else:
                    combine_recs[ele["id"]] = 1

        for each_unrec_list in not_recommended_lists:
            for ele in each_unrec_list:
                if (ele["id"] in combine_recs):
                    current_val = combine_recs[ele["id"]]
                    del combine_recs[ele["id"]]
                    combine_recs[ele["id"]] = current_val-1
                else:
                    combine_recs[ele["id"]] = -1

        results = []
        for _ in range(1, 6):
            # find result_id with the largest approval int
            result_id = max(combine_recs, key=combine_recs.get)

            # remove this key:value from list of recs
            del combine_recs[result_id]

            # get data using API
            response = requests.get("https://api.themoviedb.org/3/movie/" + str(
                result_id) + "?api_key=3dbf9b55bdf367747f40974789edbff7&language=en-US")

            # convert data to dictionary
            dictionary = json.loads(response.text)

            # create movie dictionary
            result = {
                "popularity": dictionary["popularity"],
                "poster_path": dictionary["poster_path"],
                "id": dictionary["id"],
                "original_title": dictionary["original_title"],
                "title": dictionary["title"],
                "overview": dictionary["overview"],
                "release_date": dictionary["release_date"]
            }

            results.append(result)

        return results
