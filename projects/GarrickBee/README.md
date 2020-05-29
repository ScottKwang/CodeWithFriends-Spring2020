# Garrick.monster && MaskFinder.com

My name is Garrick , a person who like to code and wish to work at US one day. Glad that Mayuko and Scott created this challenge that kept me going on doing something for my personal projects.

There are two projects I will like to present which is my personal blog/portfolio [garrick.monster](https://garrick.monster) and a mask crowd-sourcing platform [maskfinder.com](https://maskfinder.com) 
 - [Garrick.monster - Client Repo](https://github.com/GarrickBee/Garrick.monster-client) 
 -  [Maskfinder.com - Repo](https://github.com/GarrickBee/Mask-Finder-CrowdSourcing)
## Personal Blog ( Garrick.monster)
### Abstract
I have a thought of writing some technical articles to share what i have learned throughout the journey especially during this pandemic season such as system design, Leetcode problems, engineering, SpaceX ( Big Fan ) and so on. So I started looking for free platform such as google blogspot, Medium, WordPress etc but it just don't feel cool as a programmer. Besides , I find PHP is kind of dead in lot of big company and NodeJs is the big trend now. So is time for me to give NodeJs a shot. 
I start to discover some framework such as Angular, React , Nestjs , MongoDb. Since I don't wanna just deploy it on a normal server , so I do some research on Google Cloud, AWS and walah! The 0.5V of garrick.monster is born . ( Been busy these few weeks can really manage to complete version 1.0 ) 

This project will not only serve as my blog but as well as my portfolio zone as well. Will continue to build it little by little. Stay tune.

### System Design 
![enter image description here](https://raw.githubusercontent.com/GarrickBee/Garrick.monster-client/master/system_design.png)
 
The overall design concept was to create a scale-able website using RESTFUL API ( for a small starting is too much though but important is the learning process ). 

 - Front end is powered by [Angular 9](https://angular.io/) 
 - Back end is powered by [NestJs](https://nestjs.com/) a framework build on top of ExpressJs, mongoose on Nodejs using typescript- it will be a benefit if you know angular as the way they structure their framework is inspire by Angular. But currently this i only build till halfway , current server is powered by ExpressJs 
 **Server**
 - [Google Cloud](https://cloud.google.com/) Ubuntu Server 
- Image Server - [Cloudinary](https://cloudinary.com) 
- Server Engine using NGINX 
 **Database**
 - Database is powered by  [MongoDB](https://www.mongodb.com/) 
 - [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
  **Cached**
 - Redis as cached layer
   **DNS && CDN**
 -  CloudFlare server as the DNS as well as CDN 

## Maskfinder.com
![enter image description here](https://github.com/GarrickBee/Mask-Finder-CrowdSourcing/raw/master/assets/images/logo/transparent.png)
### Abstract
During this corona outbreak, the first two weeks of our country are completely short of  mask supply and there are my sellers raise their price to a unreasonable number during this season. So I decided to create an app that able to serve as a platform for my friends to share some info about the supply of mask around their neighborhood. But soon the idead became more like a crowdsourcing apps. Similar as the traffic report function of Waze that they collect public feed back to determine the traffic level of certain roads or places.

There are a team of volunteer from Germany soon reach out to me to have some collaboration on their website call [maskradar.de](https://maskradar.de) and [maskfinder.de](maskfinder.de). Is good to help people in need with your daily working skills. Feels good.

### System Design 
This project system design is much more simple that using Codeigniter , PHP framework to build with the help of google api. Is a small project that i completed in 1 week and I have added api from [http://corona.lmao.ninja/](http://corona.lmao.ninja/)  to trace the current number of cases happen around in Malaysia and the country. ( Api is deprecated and move to new version and i havent updated it) 

### Thank you
That's it, if you like my works feel free to drop a like or comment will be much more appreciated.