# Spotify Radio
Created by [Nico](https://twitter.com/also_nico), Source code: https://github.com/NicoWasHere/SpotifyRadio

## Summary
I created an application that allows for people host their own Spotify radio stations that other Spotify Premium members can listen to in realtime.

## Inspiration
I kept running out of music to listen to when I worked on stuff and I had gotten really tired of my old music, so I came up with the idea that other people could host their Spotify for everyone to listen to so it would be easier to find new music. Also I wanted to listen to music with my friends but that's hard to do in sync online.

## Challenges
At the start of the month I commited to this project thinking I would have plently of time. That led me to not working on my project the entire month until it was a few days from the due date. So I had to do the entire project in a few days which obviously made things a bit difficult. Another challenge I faced was trying to limit server usage. I didn't want to have to pay for server space so I based my communication network around websockets which I knew I could host for free online. Also the Spotify API didn't have webhooks and they limited the number of requests so I couldn't poll them for data. This was a big issue until they realesed a new web SDK a couple of days ago which did have listeners, giving me everything I needed.  

## Reflections
I definitely learned that I'm bad at time management but also that I am super productive in a time crunch. I think that's how I like working the most. I also definitely learned a lot about server architecture and how to make a secure database. I had never built a server side project to quite this scale before so I definitely encountered a lot of new elements about the web. Also I'm terrible about just sitting down and reading documentation even though it's actually the best thing ever. 

I'm still working on the project after this event. It's not entirely finsihed because it looks terrible at the moment and there are a lot of features I want to add. There are a lof of holes in the site where you can get to a page that you aren't suppose to if you aren't logged in. That is going to be the first thing I solve with some simple redirect scripts. As for new features, I want to add station titles, generes and a browse features so I can see new music.

The most helpful resource in my project was the express and Spotify documentations. Who knew that they literally wrote instructions on how to use their APIs? I also found some articles on password hashing and security authentication pretty useful for designing my backend. 

## Usage
Right now there are no servers running this code on the internet. I plan to move it over to a hosting service or buy server space soon but for now there is nothing. To try it out, you can run the node files with server.js as an entry point. 
