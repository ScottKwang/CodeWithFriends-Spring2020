# React Headless Wordpress Blog
## Overview
[![wilsonj806](https://circleci.com/gh/wilsonj806/react-headless-press-blog.svg?style=svg)](https://circleci.com/gh/wilsonj806/react-headless-press-blog)
[Repository Link](https://github.com/wilsonj806/react-headless-press-blog)

This is a quick gardening blog that'll be built with React for the frontend and headless Wordpress as the CMS. Since it's a blog, we're going to need to add content, so that's where Wordpress comes in. App deployment will be done in AWS at the end and there'll be some form of automated testing and deployments done.

## Current Progress
- Bare minimum landing page
- CircleCI based deploy of the React app to S3

## Additional Docs
As this is a learning project, additional documentation can be found below. Hope it helps on your coding journey!

- [Initial Research](./docs/init-research.md)

## Basic Requirements
Here are the basic requirements for this project:
- it needs to use React for the front end
- it needs to use Wordpress as the CMS
- the final app needs to be on Amazon Web Services

Not much to it, but there's a lot of room to grow this in complexity.

## Learning Objectives
The learning objectives for this project are simple. I want to learn how to deploy a moderately complex application to AWS, and I want to learn to use Wordpress. Kubernetes is kind of a bonus, but also would be nice and I'd be learning SSH and Linux concepts/ topics as a direct result of deploying to AWS.

### Final Reflection
So I didn't finish, but I made the blog and I got some version of a Docker-ized Wordpress app setup which is nice! It's not super wild to finish so hopefully I see the project through to the end! And this project was also an excuse to research indoor gardening/ hydroponics so I got that down!

### Challenges
So at least for this project, one of the biggest challenge was time management. Even though I'm usually pretty good about it, I spent a lot of time on weird Wordpress issues and didn't get to the React portion till the last two weeks. There was also some challenges navigating through the Wordpress environment as well as a bunch of AWS related issues that I pushed off to the last day.

### Lessons Learned
I learned several things during this project:
- Hosting is very involved, you have your static files which is great, but you also need to have a domain that points to your static files somehow. Playing with AWS S3 reinforced that
- Wordpress interestingly isn't as developer friendly as I would have thought, mostly in the repeatable deploys department which is a bit annoying
- Making your app's deployments repeatable is really nice, but it's also time consuming like in the case with Wordpress. Some times it's just better to cut that corner and get it done
