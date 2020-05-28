'use strict';

var api_url;

// api_url="http://localhost:5000/";
api_url = "https://commensals.herokuapp.com/"


var swipe = new Swiper('.swiper-container');

swipe.slideTo(1);

swipe.on('slideChange', function () {
  document.documentElement.scrollTop = 0;
});

document.querySelector("#search_button").addEventListener("click", () => {
    console.log("search");
    swipe.slideTo(2)

});

document.querySelector("#add").addEventListener("click", () => {
    swipe.slideTo(0)
});

let go_to = (name) => {
    swipe.slideTo(1)
    var xhr = new XMLHttpRequest()
    xhr.open("GET", api_url + "get?name=" + name)

    xhr.send()

    xhr.onreadystatechange = function() {
        if (this.status == 200 && this.readyState == 4) {

            // Basics for this page, this like title and imgs of the restaurant

            console.log(JSON.parse(this.response))
            let res = JSON.parse(this.response)
            document.querySelector("#main").innerHTML = ""

            let img = document.createElement("img");

            img.src = "https://estud-io.s3-us-west-1.amazonaws.com/s3crts_imgs/"+ res.img

            img.id = "img"

            let a_img = document.createElement("a")
            a_img.href = "https://estud-io.s3-us-west-1.amazonaws.com/s3crts_imgs/"+img.src

            a_img.appendChild(img)


            let title = document.createElement("h1")

            title.className = "text"

            title.append(document.createTextNode(res.name))

            document.querySelector("#main").append(a_img)
            document.querySelector("#main").append(title)

            document.querySelector("#menu_bar_button").innerHTML = "arrow_back";



            let search = res.location.search("@")

            if (search == 0) {
                let loc = document.createElement("a")

                loc.href = "https://www.google.com/maps/" + res.location

                loc.innerText = "See in Google Maps"

                loc.className = "buttons"

                document.querySelector("#main").append(loc)

            } else {
                let loc = document.createElement("h3")
                loc.append(document.createTextNode("Street: "))
                loc.append(document.createTextNode(res.location))
                loc.className = "text"
                document.querySelector("#main").append(loc)

            }

            for(let i=0;i<5;i++){
                let br=document.createElement("br")
                document.querySelector("#main").append(br)
            }


            // Comment Box/Form

            const formComments = `
                <form id="AddComment">
                    <h2 class="text">Please Write a Comment</h2>
                    <h3 class="text">Name:</h3>
                    <input type="text" class="inputField" name="name" placeholder="Your Name">
                    <h3 class="text">Comment</h3>
                    <textarea name="comment" id="" cols="30" rows="10" style="width: 100%;"></textarea>
                    <input type="text" name="restaurant" value="${res.name}" style="display: none;">
                </form>
                <button class="buttons" style="margin-left: 40%;" onclick="send_comment('${res.name}')">Send</button>`

            const CommentBox = document.createElement("div")

            CommentBox.innerHTML = formComments

            document.querySelector("#main").append(CommentBox)



            // Fetch comments

            let div_comments = document.createElement("div")

            div_comments.id = "div_comments"

            document.querySelector("#main").append(div_comments)

            get_comments(res.name)

        }
    }
};

let add_card = (name, where) => {
    /*
    <!-- Card -->
    <div class="mdc-card demo-card" onclick="go_to(this)">
      <div class="mdc-card__primary-action demo-card__primary-action" tabindex="0">
        <div class="mdc-card__media mdc-card__media--16-9 demo-card__media" style="background-image: url(&quot;img.jpg&quot;);"></div>
        <div class="demo-card__primary">
          <h2 class="demo-card__title mdc-typography mdc-typography--headline6"><i>La Locanda</i></h2>
        </div>
      </div>
    </div>
    <!-- End -->
    */

    document.querySelector(where).innerHTML = ""

    var xhr = new XMLHttpRequest();
    let url = api_url + 'get?name=' + name
    xhr.open("GET", url)
    xhr.send()
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            let res = JSON.parse(this.response)
            console.log(res)
            let card_img = "https://estud-io.s3-us-west-1.amazonaws.com/s3crts_imgs/"+ res.img
            let card = '<div class="mdc-card demo-card" onclick="go_to(this.id)" id="' + name + '"><div class="mdc-card__primary-action demo-card__primary-action" tabindex="0"><div class="mdc-card__media mdc-card__media--16-9 demo-card__media" style="background-image: url(&quot;' + card_img + '&quot;);"></div><div class="demo-card__primary"><h2 class="demo-card__title mdc-typography mdc-typography--headline6"><i>' + name + '</i></h2></div></div></div>'
            let card_dump = document.createElement("div");
            card_dump.innerHTML = card

            document.querySelector(where).append(card_dump)

            document.querySelector("#menu_bar_button").innerHTML = "home"
        }
    }

};

function get_cards() {
    swipe.slideTo(1);

    document.querySelector("#main").innerHTML = '';

    let xhr = new XMLHttpRequest()
    xhr.open("GET", api_url)
    xhr.send()
    xhr.onreadystatechange = function() {

        if (this.status == 200 && this.readyState == 4) {

            var res = JSON.parse(this.response)

            res.restaurants.forEach(i => {
                add_card(i, "#main")
            })
        } else if (this.status == 0) {

            document.querySelector("#main").innerHTML = '<img class="offline" src="offline.png">';

            setTimeout("location.reload()", 5000);
        }
    }
};


function notification() {
    cordova.plugins.notification.local.schedule({
        title: "Are you hungry?",
        text: "Check out this new restaurant nearby you",
        foreground: true
    });
}

function search(value) {
    document.querySelector("#search_page").innerHTML = ""
    var xhr = new XMLHttpRequest();
    let url = api_url + "search?q=" + value;
    xhr.open("GET", url);
    xhr.send()
    xhr.onreadystatechange = function() {
        console.log(this.status)
        if (this.status == 200 && this.readyState == 4) {
            var res = JSON.parse(this.response);

            console.log(res)

            res.res.forEach(i => {
                add_card(i, "#search_page")
            })
        } else if (this.status == 0) {
            document.querySelector("#search_page").innerHTML = '<img class="offline" src="offline.png">';
            setTimeout(search(value), 5000);
        }
    }
}


function send() {
    let name = document.querySelector("#name").value;
    if (name) {
        let location = document.querySelector("#Location").value;
        if (location) {
            var form = document.querySelector("#form");
            var formData = new FormData(form);
            var xhr = new XMLHttpRequest();
            xhr.open("POST", `${api_url}/add`, true);
            xhr.send(formData)
            xhr.onreadystatechange = function() {
                console.log(this.response);
                get_cards();
                swipe.slideTo(1)
            }
        } else {
            alert("You must type a location")
        }
    } else {
        console.log("name")
        alert("You must type a name")
    }
}

function click_file() {
    document.querySelector("#file").click()
}



function get_geo() {
    navigator.geolocation.getCurrentPosition(function(obj) {
        console.log(obj)
        let Location = document.querySelector("#Location")

        Location.value = `@${obj.coords.latitude},${obj.coords.longitude},${obj.coords.altitude}z`
    });
}

document.addEventListener('deviceready', function() {
    // setTimeout("notification()", 5000)
}, false);
function check () {
    let xhr = new XMLHttpRequest();
    xhr.open("GET",`${api_url}`)
    xhr.send();
    setTimeout("check()",2000)
}

// Comments

function send_comment (restaurant) {
    const form = document.querySelector("#AddComment")
    const formData = new FormData(form)
    var xhr = new XMLHttpRequest();
    xhr.open("POST",`${api_url}/comment`,true);
    xhr.send(formData)
    xhr.onreadystatechange = function () {
        if (this.status == 200 && this.readyState == 4){
           var res = this.response
           console.log(res)

           go_to(restaurant)
        }
        else{
            // Do Other Thing
        }
    }
}

function get_comments (restaurant) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET",`${api_url}/comments?name=${restaurant}`,true);
    xhr.send()
    xhr.onreadystatechange = function () {
        if (this.status == 200 && this.readyState == 4){
           var res = JSON.parse(this.response)

           console.log(res)

           res.res.forEach(i =>{

               if (i != ""){
                   const data = i.split("=>")

                   let comment_div = document.createElement('div')

                   const comment = `
                    <div class="comments">
                        <b class="user_name">${data[0]}</b>
                        <p class="comment">${data[1]}</p>
                    </div>
                   `

                   document.querySelector("#div_comments").append(comment_div)

                   comment_div.innerHTML = comment
               }

           })
        }
        else{
            // Do Other Thing
        }
    }
}