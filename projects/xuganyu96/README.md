# PyArchive: Integrated file backup solution
Created by Bruce Xu:
* My GitHub Profile: https://github.com/xuganyu96
* Project source code: https://github.com/xuganyu96/PyArchive
* Watch video demo here: https://youtu.be/OxYqd7Sc_kI

## Introduction
PyArchive is an integrated file backup solution with a web GUI that provides a number of features 
that make it superior to conventional backup solutions:
* It can run on a Raspberry Pi, which can serve as a local backup, and which can run 24/7 without 
user supervision
* Upload and download between local backup and remote backup are done in batches, thus limiting 
progress loss in the event of a system outage
* Flexible and extensible system admin scripting utilities

## Inspiration 
On many occasions when I wanted to backup important data to Google Drive, I had to go through tedious 
long upload process because the file I wanted to upload is several gigabytes in size, but my home 
internet connection only has 600 kilobytes of bandwidth. The long upload time also introduces the 
possibility of being interrupted by an internet outage, or my laptop accidentally going to sleep, 
which could make me lose hours of upload progress. 

My frustration with online data backup solution inspired me to create this project, which combined 
local backup solution with remote backup solution and addressed a number of shortcomings from either 
category of data backup solutions. Local backups are fast and convenience, but is prone to catastrophic 
hardware failure and data loss, so I introduced a remote component that serves as a more robust 
secondary archival backup; remote backups are slow and prone to disruption, so I introduced a batch 
processing mechanism to limit progress loss in the event of a system outage. In addition, by deploying 
the entire project onto a Raspberry Pi, the entire remote backup process can be done on the background 
without user supervision, thus saving time and mental energy.

During the development process, I was also bothered by the amount of under-the-hood work that I had 
to do to debug and finetune system behavior. This inspired me to also develop an `AdminTool` module, 
which allowed the system admin to write, save, and deploy scripts entirely from the Web GUI, without 
needing to do any remote debugging through SSH.

## Challenges
* I have to learn Django entirely from scratch, and later on during the development process, I also 
have to learn to implement a websocket connection within the Django frameowrk, from scratch. It's
incredibly fortunate for me that there is a lot of resource online regarding the fundamentals of 
Django, and the tutorial for `channels` is also very helpful.
* I can work on the project for no more than an hour everyday, so I needed to track my progress and 
know where I left off for the previous work session. In the end I made a mock-up Kanban board on 
GitHub and was able to clearly articulate the various problems and features that I worked on as 
issues linked to pull requests.

## Reflection 
1. Learning new thing is fun! 
2. There is great value in streamlining a system admin's work, which I have learned through the 
development process of this project as much as I have learned from work (I work on data platform 
and development platform). A developer/system admin is capable of going under the hood and make 
scrappy fixes, but that doesn't mean that he/she could not make certain processes more streamlined. 