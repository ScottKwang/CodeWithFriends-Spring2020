package main

import (
	"go-vaporwave/imgloader"
	"go-vaporwave/imgprocesser"
	"go-vaporwave/imgsaver"
)

const Address = "/Users/youngyuan/CodeWithFriends-Spring2020/projects/yngyuan/go-vaporwave/images/"
const filename = "demo.jpg"
func main(){
	// loading image
	img, err := imgloader.LoadImage(Address+filename)
	if(err != nil){
		panic(err)
	}
	// TODO processing image
	processed, err := imgprocesser.ProcessImage(img)
	// saving image
	err = imgsaver.SaveImage(Address+"vaporwave_"+filename, processed)
	if(err != nil){
		panic(err)
	}
}
