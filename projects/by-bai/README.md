# Fitness Bud
![home](/images/fitnessbud-home.png)


Created by: Bai ( [Github](https://github.com/by-bai), [Linkedin](https://www.linkedin.com/in/baihe/) )
Demo: [by-bai.github.io/fitnessbud](https://by-bai.github.io/fitnessbud/) 



## Summary
Fitness Bud is (currently, a client-side only) web application that generates a random workout video from Youtube, according to the user's choice of workout category (e.g. abs workout). When the user selects a workout category, the web application sends a request to Youtube's API to search for videos that belong to the chosen category, then returns one random video from the search.

Random video generated:

![video](/images/fitnessbud-video.png)

<p align="center">
  It has a responsive design :)<br><br>
  
  <img src="/images/fitnessbud-mobile.png" width="500">
</p>


## Inspiration
Code With Friends Spring 2020's theme of **Quarantine Improvement** inspired me to start this project. As home workouts have become a part of my daily routine during quarantine, I thought it would be fun to add some randomness to my current workout routine. I also wanted to use this application to encourage my friends to workout at home and introduce more workout videos to them. 


## Challenges
As there are limited calls that can be made to Youtube's API per day, I decided to use two API keys, one as backup and add an error message: 

![error](/images/fitnessbud-error.png)

Furthermore, being a client-side only application, the API keys are not protected. I hope to improve this application in the future by developing a backend server. For now, I have restricted key usage requests to my demo site. 


## Reflections
I realised the importance of designing the functionalities of a web application & having an idea of the system architecture before jumping straight into coding. During the first few days of CWF2020, I felt lost because I did not plan, and did not have a clear image of the final product in mind. Feeling stuck, I took a piece of paper to pen down my thoughts carefully and drew the layout of the web application. This helped me to visualise how I wanted Fitness Bud to look like, which really pushed me to code with a clear goal in mind. Eventually, I became more comfortable and efficient at coding frontend, working from the layout - the big picture - to the smaller elements on the page.


## Possible Extensions
* Frontend with React.js 
* Backend with Nodejs - perhaps store the links retrieved from Youtube API in a database, then handle API calls from frontend to backend 
* Possible functionalities:
```
user login function
user can save their favourite workouts and access easily
user can log details of their daily home workouts (e.g. time, youtube link, workout category)
user can visualise insights from their workout logs on an analytical dashboard
```


If you have the time, [feedbacks](https://docs.google.com/forms/d/e/1FAIpQLSeHaMXkMuaVVFtlVqdxekAxBRyzywVsGW9Oyvqf8_xWAPEZqQ/viewform) are appreciated :> 

I hope to encourage more friends to workout at home with Fitness Bud and stay healthy!
