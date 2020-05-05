package imgprocesser

import (
	"image"
	"image/color"
	_ "image/jpeg"
)

func ProcessImage(img image.Image) (image.Image, error){
	bounds := img.Bounds()
	newRgba := image.NewRGBA(bounds)
	dx := bounds.Dx()
	dy := bounds.Dy()
	for i := 0; i < dx; i++ {
		for j := 0; j < dy; j++ {
			colorRgb := img.At(i, j)
			r, g, b, a := colorRgb.RGBA()
			r_uint8 := uint8(r >> 8)	//转换为 255 值
			g_uint8 := uint8(g >> 8)
			b_uint8 := uint8(b >> 8)
			a_uint8 := uint8(a >> 8)

			r_uint8 = 255 - r_uint8
			g_uint8 = 255 - g_uint8
			b_uint8 = 255 - b_uint8
			newRgba.SetRGBA(i, j, color.RGBA{r_uint8, g_uint8, b_uint8, a_uint8}) //设置像素点
		}
	}

	return newRgba, nil
}
