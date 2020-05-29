# Countdown Timer

![Screenshot](https://raw.githubusercontent.com/ScottKwang/CodeWithFriends-Spring2020/master/assets/images/banner_new.png)

This project was made for submission to the CWF-Spring 2020.
Check out CWF's [github page](https://scottkwang.github.io/CodeWithFriends-Spring2020/) to learn more!

Created by [L4TTiCe](https://github.com/L4TTiCe)</br>
Source code: [CountdownTimer-Angular](https://github.com/L4TTiCe/CountdownTimer-Angular)</br>
Hosted at: [countdowntimer-angular.web.app](https://countdowntimer-angular.web.app/)

## Summary

This project is a simple Angular implementation of a Catalogue of Countdowns, to anything. It allows you to create and track the countdown to any Event or Activity and then, view the countdown to the event in real-time.

![countdown-landing-page](https://github.com/L4TTiCe/CountdownTimer-Angular/blob/master/docs/assets/landing_new.png?raw=true)

## Inspiration

With Quarantines and lockdowns being the new norm, I found myself indulging more and more in TV Shows like Westworld, etc.. The most frustrating part was waiting for new episodes to air. Since I'm not from the USA, I had to manually count how long I had to wait by comparing it with the local Timezone. In order to simplify this tiresome mental math, I decided to build a simple web app, that lets you track the episode premiere with the calculated time.

Later, I decided to expand the scope of this project to also track Events aside from only episodes, so it can be used to track Birthdays, Online Concerts, etc.. To ensure sharing of countdowns among their friends' circles, I made sure the events had unique shareable URLs and user pages, so users can check on each other to see what they are up to. 

## Challenges and Reflections

I built this project as my first major Angular project, to get myself more familiar with the Angular ecosystem. Though it initially took a bit of time to get myself familiar with it, later I felt more comfortable using it. Some of the challenges I somewhat felt overwhelmed by were the fragmented nature of the Angular libraries, with many libraries not being compatible with the newer Angular version 9. I ran into a lot of compatibility issues and had to reduce my reliance on 3rd party
libraries.

I wanted to implement a searchable index to make sure the Events are users could be searchable with a single search box. But even after searching through many search indexers, I could not find a good search indexer which was free. I still wish to pursue implementing a search system in place, later when I find a good alternative.

I am yet to implement the following and tracking system, that enables users to track events created by other users, so it shows up in their home feed, along with other countdowns created by users whom they follow. But due to lack of time and a tight college schedule, I was unable to complete the Home Feed in time for the May 29 submission deadline.
