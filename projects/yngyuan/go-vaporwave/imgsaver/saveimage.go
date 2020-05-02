package imgsaver

import (
	"fmt"
	"image"
	"image/jpeg"
	"os"
)

func SaveImage(filename string, img image.Image) error{
	// Somewhere in the same package
	output, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer output.Close()

	// Specify the quality, between 0-100
	// Higher is better
	opt := jpeg.Options{
		Quality: 100,
	}
	err = jpeg.Encode(output, img, &opt)
	if err != nil {
		panic(err)
	}
	fmt.Println("Done vaporwave!")
	return nil
}
