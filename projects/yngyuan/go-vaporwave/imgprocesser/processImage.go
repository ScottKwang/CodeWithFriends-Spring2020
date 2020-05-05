package imgprocesser

import (
	"image"
	"image/color"
	_ "image/jpeg"
	"math"
)

func ProcessImage(img image.Image) (image.Image, error){

	processed, err := reverseColor(img)
	if err != nil {
		panic(err)
	}
	return processed, nil
}

func reverseColor(img image.Image) (image.Image, error) {
	bounds := img.Bounds()
	newRgba := image.NewRGBA(bounds)
	dx := bounds.Dx()
	dy := bounds.Dy()

	for i := 0; i < dx; i++ {
		for j := 0; j < dy; j++ {
			colorRgb := img.At(i, j)
			r, g, b, a := colorRgb.RGBA()
			r_uint8 := uint8(r >> 8) //转换为 255 值
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

func rgb2yuv(r,g,b float64) (y, u, v float64){

y = r *  0.299000 + g *  0.587000 + b *  0.114000
u = r * -0.168736 + g * -0.331264 + b *  0.500000 + 128
v = r *  0.500000 + g * -0.418688 + b * -0.081312 + 128

y = math.Floor(y)
u = math.Floor(u)
v = math.Floor(v)

return y, u, v
}

func yuv2rgb(y,u,v float64) (r,g,b float64){

r = y + 1.4075 * (v - 128);
g = y - 0.3455 * (u - 128) - (0.7169 * (v - 128));
b = y + 1.7790 * (u - 128);

r = math.Floor(r);
g = math.Floor(g);
b = math.Floor(b);

if r<0 || r>255 {
	if r<0 {r = 0}
	if r>255 {r = 255}
}

if g<0 || g>255 {
		if g<0 {g = 0}
		if g>255 {g = 255}
}

if r<0 || r>255 {
	if g<0 {g = 0}
	if g>255 {g = 255}
}

return r, g, b
}

