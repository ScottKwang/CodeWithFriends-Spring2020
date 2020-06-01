package imgloader

import (
	"fmt"
	"image"
	_ "image/jpeg"
	"os"
)

func LoadImage(filename string) (img image.Image, err error) {
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

	return img, nil
}