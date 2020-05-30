const datePickerElement = document.querySelector('.LSC-date-picker');
const selectedDateElement = document.querySelector('.LSC-date-picker .selected-date');
const datesElement = document.querySelector('.LSC-date-picker .dates');
const mthElement = document.querySelector('.LSC-date-picker .dates .month .mth');
const nextmthElement = document.querySelector('.LSC-date-picker .dates .month .next-mth');
const prevmthElement = document.querySelector('.LSC-date-picker .dates .month .prev-mth');
const daysElement = document.querySelector('.LSC-date-picker .dates .days');

const months = ['January', 'February', 'March', 'April', 'May', 'June',
                'July', 'August', 'September', 'October', 'Novemember', 'December'];

let date = new Date();
let day = date.getDate();
let month = date.getMonth();
let year = date.getFullYear();

let selectedDate = date;
let selectedDay = day;
let selectedMonth = month;
let selectedYear = year;

mthElement.textContent = `${months[month] } ${year}`;
selectedDateElement.value = FormatDate(date);
selectedDateElement.dataset.value = selectedDate; // allow to easily get value from element
populateDates();

//////////////////////////////////////////////////////
// EVENT LISTENERS
//////////////////////////////////////////////////////
datePickerElement.addEventListener('click', ToggleDatePicker);
nextmthElement.addEventListener('click', GoToNextMonth);
prevmthElement.addEventListener('click', GoToPrevMonth);

//////////////////////////////////////////////////////
// FUNCTIONS
//////////////////////////////////////////////////////
function ToggleDatePicker(e) 
{
    if (!CheckEventPathForClass(e.path, 'dates'))
    {
        datesElement.classList.toggle('active');
    }
}

function GoToNextMonth(e)
{
    month++;
    // if moving from December to January
    if (month > 11) {
        month = 0;
        year++;
    }
    mthElement.textContent = `${months[month] } ${year}`;
    populateDates();
}

function GoToPrevMonth(e)
{
    month--;
    // if moving from January to December
    if (month < 0) {
        month = 11;
        year--;
    }
    mthElement.textContent = `${months[month] } ${year}`;
    populateDates();
}

function populateDates(e) 
{
    daysElement.innerHTML = ''; //clear

    let amountDays = CalculateAmountOfDays(month, year);

    for (let i = 0; i < amountDays; i++) 
    {
        // create new element
        const dayElement = document.createElement('div');
        dayElement.classList.add('day');
        dayElement.textContent = i + 1;

        // highlight selected date
        if (selectedDay == (i + 1) && selectedMonth == month && selectedYear == year)
        {
            dayElement.classList.add('selected');
        }

        // let use click on newly created date element
        dayElement.addEventListener('click', function() {
            selectedDate = new Date(`${year}-${month + 1}-${i + 1}`);
            selectedDay = i + 1;
            selectedMonth = month;
            selectedYear = year;

            selectedDateElement.value = FormatDate(selectedDate);
            selectedDateElement.dataset.value = selectedDate;

            populateDates(); // rerender the day elements
        });

        // add element to document
        daysElement.appendChild(dayElement);
    }
}

//////////////////////////////////////////////////////
// HELPER FUNCTIONS
//////////////////////////////////////////////////////
function CheckEventPathForClass(path, selector) 
{
    for (let i = 0; i < path.length; i++)
    {
        if (path[i].classList && path[i].classList.contains(selector)) 
        {
            return true;
        }
    }

    return false;
}

function FormatDate(d) {
    let day = d.getDate();
    if (day < 10) 
    {
        day = '0' + day;
    }

    let month = d.getMonth() + 1;
    if (month < 10) 
    {
        month = '0' + month;
    }

    let year = d.getFullYear();

    return `${month}/${day}/${year}`;
}

function CalculateAmountOfDays(mth, yr) 
{
    // check if February
    if (mth == 1) 
    {
        // check if leap year
        if ((yr % 4) == 0) 
        {
            if ((yr % 100) != 0) 
            {
                return 29;
            }
            else if ((yr % 400) == 0)
            {
                return 29;
            }
            else {
                return 28;
            }
        }
        else 
        {
            return 28;
        }
    }
    // check if April, June, September, or November
    else if (mth == 3 || mth == 5 || mth == 8 || mth == 10) 
    {
        return 30;
    }
    else 
    {
        return 31;
    }
}