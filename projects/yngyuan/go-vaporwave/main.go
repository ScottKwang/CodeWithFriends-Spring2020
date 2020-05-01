package main

import (
	"fmt"
	"image"
	"image/jpeg"
	"os"
)

const address = "/Users/youngyuan/CodeWithFriends-Spring2020/projects/yngyuan/go-vaporwave/images/"

func main(){
	// TODO loading image
	f, err := os.Open(address+"demo.jpg")
	if err != nil {
		panic(err)
	}
	defer f.Close()

	img, fmtName, err := image.Decode(f)
	if err != nil {
		panic(err)
	}
	fmt.Printf("input format: %s", fmtName)

	// Somewhere in the same package
	output, err := os.Create(address+"outimage.jpg")
	if err != nil {
		panic(err)
	}
	defer output.Close()

	// Specify the quality, between 0-100
	// Higher is better
	opt := jpeg.Options{
		Quality: 90,
	}
	err = jpeg.Encode(output, img, &opt)
	if err != nil {
		panic(err)
	}

	// TODO processing image
	// TODO saving image
	fmt.Println("Done vaporwave!")

}
