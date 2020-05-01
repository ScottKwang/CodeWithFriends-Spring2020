package imgloader

import (
	"fmt"
	"image"
	"image/jpeg"
	_ "image/jpeg"
	"os"
)

type Imgloader interface {

}

const address = "projects/yngyuan/go-vaporwave/images/"

func loadImage(filename string) error {
	f, err := os.Open(filename)
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
	output, err := os.Create("projects/yngyuan/go-vaporwave/images/outimage.jpg")
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

	return nil
}