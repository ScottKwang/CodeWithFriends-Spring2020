package playground

import "testing"

func TestArea(t *testing.T) {
	check := func(t *testing.T, got, want float64) {
		if got != want {
			t.Errorf("got %.2f want %.2f", got, want)
		}
	}

	t.Run("check permeter not safe", func(t *testing.T) {
		got := Area(10.0, 10.0)
		want := 40.0
		check(t, got, want)
	})
	t.Run("check permeter not safe", func(t *testing.T) {
		got := Area(20., 20.)
		want := 80.
		check(t, got, want)
	})
}
