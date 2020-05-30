# Book-saver

Created by Yuhi Koizumi
Github: https://github.com/yuhijackman
Instagram: https://www.instagram.com/theyuuhijackman/?hl=en
LinkedIn: https://www.linkedin.com/in/yuuhi-koizumi-070813158
Source code: https://github.com/yuhijackman/booksaver2
Production: https://brave-chandrasekhar-d3acfd.netlify.app/

## Summary
Book-saver is a visual reminder of how much unread books on your bookshelf are worth and helps you stop letting books pile up unread.
I built it for Code With Friends.

![1](/projects/yuhijackman/toppagescreenshot.png)

![2](/projects/yuhijackman/booklistscreenshot.png)
## Inspiration and why I made this.
I felt like I need something that motivates me to pick up unfinished books on my bookshelf once again, when I saw tons of books on my bookshelf.
And I noticed many of the books aren't read through to the end.

I believe that I'm not the only person who has started dozens of books and never finished them.
So I decided to create this app.

## Challenges
To fetch book data from an external API on SPA was a bit of challenge.
I created it with Nuxt.js and Firebase as a SPA.
And I needed to request a resource that has a different origin to fetch book data.
Of course It's against CORS policy.
So I had to think about how to make it possible.

I decided to use a Netlify function as a place where all the requests to an external API is hit.

## Reflections

1. What did you learn during this proces
  - No SQL Data Modeling since I had zero experience of using it.
  - Atomic Desing.
  - Importance of finishing a project. I've tried to build many projects before but I always lost motivation and impetus halway through. The reason why I could finish it this time is because I have a clear purpose which is submitting it to CWF.
 
2. What would you have done differently if you could do it again?
  - I would build API with Laravel and stop relying on firebase and Netlify.

3. What resources did you find most helpful in working on your project?
  - Official documentation for Firebase and Vue.js.

# LINK
``https://brave-chandrasekhar-d3acfd.netlify.app/``

# Demo account
You can try it out with this account.
Email: demo@gmail.com
Password: Demoaccount