# Cov(id-19)say

Created by Julian 
 - [GitHub](https://github.com/julianbd)

## Summary

For CWF Spring 2020 I wrote a small CLI application that borrows off of the classic CLI application "cowsay" that prints a user's desired message in a text bubble spoken by an ACII cow (or other animal). For CWF, I combined this with an API exposed by The Atlantic that posts national COVID statistics for the US.

## Inspiration

Originally, I was planning on creating a React application that allowed seniors or anyone else desiring technical help for getting setup with Internet communication methods during quarantine to pair with technologically savvy people willing to offer help. As I'll discuss below, that proved to gargantuan of a task and I was learning Golang concurrently, so I decided on the CLI instead.

## Challenges

There just wasn't enough time to get the React app done. As a devops engineer, I haven't done much frontend work and learning all of the required tools and frameworks within a month proved to ambitious. However, I've been refreshing my skills with algorithms and data structrues while learning Go, so I figured I'd add this challenge as more learning opportunities of the language's features. 

## Reflections

I learned how to make HTTPS requests in Go and how write a short CLI using the Go standard library.

I'd add way more features, including more command-line flags to pull different data from the API. I'd also break the code into more source files to modularize the logic.

The Golang spec, gobyexample, http://cowsay.morecode.org/, and https://covidtracking.com/ proved most useful/necessary
