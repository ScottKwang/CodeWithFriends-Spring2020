# imports
from flask import Flask, render_template, request, redirect, url_for, session
import json
import random
import requests
from recommender import Recommender

# instance of flask class
app = Flask(__name__)

# secret key
app.config["SECRET_KEY"] = 'By-jy9JI9TVojxj2gT45Zg'

# TMDB API
api_key = "3dbf9b55bdf367747f40974789edbff7"
base_url = "https://api.themoviedb.org/3"

discover = "/discover/movie/?api_key=" + \
    api_key+"&sort_by=popularity.desc&page="

# entire list of movie objects
movie_lib = []

# return api_url


def api_url(page_num):
    return base_url+discover+str(page_num)


# iterate through pages and add movies to movie_lib
for page in range(1, 11):
    url = api_url(page)

    # get data using API
    response = requests.get(url)

    # convert data to dictionary
    dictionary = json.loads(response.text)

    # get list of dictionaries of movies
    discover_movies = dictionary["results"]

    # store all dictionaries as movies in movie_lib
    for movie_dict in discover_movies:
        movie_lib.append({
            "popularity": movie_dict["popularity"],
            "poster_path": movie_dict["poster_path"],
            "id": movie_dict["id"],
            "original_title": movie_dict["original_title"],
            "title": movie_dict["title"],
            "overview": movie_dict["overview"],
            "release_date": movie_dict["release_date"]
        })

# return a movie that has not yet been rated


def choose_unique_film(rated_history):
    unique = False
    while (not unique):
        exists = False
        random_movie = random.choice(movie_lib)
        for rated in rated_history:
            if (random_movie == rated) and (random_movie["poster_path"] != None):
                exists = True
        if exists == False:
            unique = True

    return random_movie

# iterates through rated_history and returns movie object


def get_movie(id, rated_history):
    for movie in rated_history:
        if movie["id"] == int(id):
            return movie


def check_eight(personal_ratings, num_rated):
    return (num_rated >= 8)


def check_personal_ratings(personal_ratings):
    not (personal_ratings == [])


# routing

# route try_again
@app.route('/try_again')
def try_again():
    session.clear()
    return redirect(url_for('main'))


# routing to main page
@app.route('/', methods=['GET', 'POST'])
def main():

    # if session is empty, create a user
    if session == {}:
        session["personal_ratings"] = []
        session["rated_history"] = []
        session["num_rated"] = 0
        session["has_started_rating"] = False

    if request.method == 'POST'and session["has_started_rating"]:
        # movie was rated via form

        # gather data from form
        rated_movie_id = request.form.get('movie_id_rated')
        rating = request.form.get('rating')

        if (rating == 'like' or rating == 'dislike'):
            session["personal_ratings"].append(
                [get_movie(rated_movie_id, session["rated_history"]), rating])
            session["num_rated"] += 1

    if check_eight(session["personal_ratings"], session["num_rated"]):
        return redirect(url_for('results'))

    # get a random + unique movie from movie list
    random_movie = choose_unique_film(session["rated_history"])

    # add to the history of movies that have been shown so far
    session["rated_history"].append(random_movie)

    session["has_started_rating"] = True

    return render_template('index.html', movie=random_movie, num_rated=session["num_rated"])


# routing to results page
@app.route('/results')
def results():

    # if personal_rating list is empty, reroute to 404 page
    if check_personal_ratings(session["personal_ratings"]):
        return redirect(url_for('to404'))

    try:
        # create recommender object
        recommender = Recommender(session["personal_ratings"])

        # get array of five results
        results = recommender.get_result()

        # store first result
        first_result = results.pop(0)

        # store first result release year
        first_result_year = (first_result["release_date"])[0: 4]
        return render_template('results.html',
                               title="Results",
                               first_result=first_result,
                               first_result_year=first_result_year,
                               results=results)

    except Exception as e:
        print(e)
        return redirect(url_for('to404'))


# 404 error page
@app.errorhandler(404)
def error(e):
    return redirect(url_for('to404'))


@app.route('/404')
def to404():
    return render_template('404.html', title="404 error")


if __name__ == '__main__':
    # if code is changed, the web app will automatically reload
    app.run(debug=True)
