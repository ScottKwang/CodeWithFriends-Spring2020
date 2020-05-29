package playground

func Sum(numbers []int) int {
	sum := 0
	for _, k := range numbers {
		sum += k
	}
	return sum
}

func SumAll(numbers ...[]int) []int {
	var sums []int
	for _, numbers := range numbers {
		sums = append(sums, Sum(numbers))
	}
	return sums
}

// SumAllTails get sums except he first value
func SumAllTails(numbers ...[]int) []int {
	var sums []int
	for _, num := range numbers {
		if len(numbers) == 0 {
			sums = append(sums, 0)
		} else {
			tails := num[1:]
			sums = append(sums, Sum(tails))
		}
	}
	return sums
}
