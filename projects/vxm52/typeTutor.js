//KEYCODE BASE
var keycodeArray = [];
for (keycodeNumber = 97; keycodeNumber <= 122; keycodeNumber++)
{
    keycodeArray.push(keycodeNumber);
}

var myMusic = new Audio("sound/bensound-littleidea.mp3"); //Credits to Bensound.com
myMusic.volume = 0.3;
var successSound = new Audio("sound/success.mp3");
var errorSound= new Audio("sound/error.mp3");


//LETTER BASE
var letterBase, gamePlaying;
letterBase = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

var randomLetter = Math.floor(Math.random() * 25);

//Pulls a random letter from letter base and displays it
var loopyLetter = function() {
    randomLetter = Math.floor(Math.random() * 25);
    randLetterGetter = document.getElementById('randomLetter').textContent = (letterBase[randomLetter]);
    //console.log(`Random Number: ${randomLetter}, Random Letter: ${randLetterGetter}`);
    letterColor.style.color = "black";
}

var isGameRunning = false;
var score = 0;
var letterColor = document.getElementById("randomLetter");

//Start the game
function startGame()
{
    myMusic.play();
    var speedPref = prompt('How many milliseconds do you want between each word?')
    console.log('The game has started');

    //Makes randomLetter visible
    var rLetterMan = document.getElementById("randomLetter");
    if (rLetterMan.style.display = "none")
    {
        rLetterMan.style.display = "block";
    }
    else
    {
        rLetterMan.style.display = "none";
    }

    //Makes "Start" button invisible
    var sButtonMan = document.getElementById("startButton");
    if (sButtonMan.style.display = "block")
    {
        sButtonMan.style.display = "none";
    }
    else
    {
        sButtonMan.style.display = "block";
    }

    //Makes "End Game" button visible
    var eButtonMan = document.getElementById("endButton");
    if (eButtonMan.style.display = "none")
    {
        eButtonMan.style.display = "block";
    }
    else
    {
        eButtonMan.style.display = "none";
    }
    isGameRunning = true;

    //Displays one letter every second for a total of 15 letters
    (function theLoop(i) {
        setTimeout(function () {
            if (i-- && isGameRunning)
            {          // If i > 0 AND isGameRunning = true, then keep going
                loopyLetter();
                theLoop(i);
                     // Call the loop again, and pass it the current value of i
            }
            if (i <= 0)
            {
                isGameRunning = false;
            }
        },  speedPref);
        })(15);
};

//Point display
function pointDisplay() {
    document.getElementById('pointsBar').textContent = ('Points: ' + score);
};

//Keypress function + point system
var trackerRight = 0;
var trackerWrong = 0;
document.addEventListener('keypress', function(event) {
    var x, keycodeLogger,
    x = event.keyCode;
    keycodeLogger = (keycodeArray.indexOf(x));

    if (keycodeLogger === randomLetter) //Correct key pressed
    {
      //console.log('you score a point!');
      if (isGameRunning)
      {
        //Make colorBox Green
        if (letterColor.style.color = "black")
        {
            letterColor.style.color = "#3DFF5F";
        }
        else
        {
            letterColor.style.color = "black";
        }

        successSound.play();
        //Add to score
        trackerRight = trackerRight + 1;
        score = score + 1;
        pointDisplay();
      }
      else //Countdown has finished (game ended)
      {
          console.log("Game ended. You cannot spam keys");
      }
    }
    else //Wrong key pressed
    {
         if (isGameRunning)
        {
          //Make letterColor Red
          if (letterColor.style.color = "black")
          {
              letterColor.style.color = "#FF0000";
          }
          else
          {
              letterColor.style.color = "black";
          }

          errorSound.play();
          //Deduct score
          trackerWrong = trackerWrong + 1;
          score = score - 1;
          pointDisplay();
          //console.log('You don\'t score a point');
        }
        else //Countdown has finished (game ended)
        {
            console.log("Game ended. You cannot spam keys");
        }
    }

    var average = trackerRight/(trackerRight + trackerWrong);
    var easyAverage = Math.floor(100 * average)
    document.getElementById('accuracyBar').textContent = ('Accuracy: ' + easyAverage + '%');
})

//End Game button
document.getElementById("endButton").addEventListener("click", function() {
    isGameRunning = false;
    score = 0;
    pointDisplay();
    console.log("isGameRunning set to: " + isGameRunning + ". Game has ended");

    //Makes "Start" button visible again
    var sButtonMan = document.getElementById("startButton");
    if (sButtonMan.style.display = "none")
    {
        sButtonMan.style.display = "block";
    }
    else
    {
        sButtonMan.style.display = "none";
    }

    //Makes "End Game" button invisible again
    var eButtonMan = document.getElementById("endButton");
    if (eButtonMan.style.display = "block")
    {
        eButtonMan.style.display = "none";
    }
    else
    {
        eButtonMan.style.display = "block";
    }

});

//DATE DISPLAY
var dateMaker = function() {
    var now, months, month, year, date;

    now = new Date();

    months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    month = now.getMonth();
    date = now.getDate();
    year = now.getFullYear();
    // document.getElementById('dateBar').textContent = 'Date: ' + date + ' ' + months[month] + ' ' + year;
    document.getElementById('dateBar').textContent = `Date: ${date} ${months[month]} ${year}`;
}();
