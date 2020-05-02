package main

import (
	"go-vaporwave/imgloader"
	"go-vaporwave/imgsaver"
)

const Address = "/Users/youngyuan/CodeWithFriends-Spring2020/projects/yngyuan/go-vaporwave/images/"
const filename = "demo.jpg"
func main(){
	// TODO loading image
	img, err := imgloader.LoadImage(Address+filename)
	if(err != nil){
		panic(err)
	}
	// TODO processing image

	// TODO saving image
	err = imgsaver.SaveImage(Address+"vaporwave_"+filename, img)
	if(err != nil){
		panic(err)
	}
}
