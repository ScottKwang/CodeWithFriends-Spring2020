package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

type USData struct {
	PositiveCases int `json:"positive"`
	NegativeCases int `json:"negative"`
}

func main() {
	positive := flag.Bool("p", false, "Cow says how many positive COVID-19 cases there are in the USA")
	negative := flag.Bool("n", false, "Cow says how many negative COVID-19 cases there are in the USA")
	flag.Parse()

	var data []USData
	body := getData("https://covidtracking.com/api/v1/us/current.json")

	err := json.Unmarshal(*body, &data)
	if err != nil {
		log.Fatalln(err)
	}

	var message string
	if *positive {
		message = fmt.Sprintf("There+are+currently+%d+positive+COVID-19+cases+in+the+USA", data[0].PositiveCases)
	}

	if *negative {
		message = fmt.Sprintf("There+are+currently+%d+negative+COVID-19+cases+in+the+USA", data[0].NegativeCases)
	}

	if !*positive && !*negative {
		log.Fatalln("Use 'covsay --help' for usage")
	}
	requestURL := "http://cowsay.morecode.org/say?message=" + message + "&format=text"

	cowsay := getData(requestURL)
	fmt.Println(string(*cowsay))
}

func getData(url string) *[]byte {
	resp, err := http.Get(url)
	if err != nil {
		log.Fatalln("Error fetching from URL")
	}

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatalln("Error reading retrieved data")
	}

	return &body
}
