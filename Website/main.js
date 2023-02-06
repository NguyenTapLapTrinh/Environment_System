// Time change auto
var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

function currentTime() {
    let date = new Date();
    let hh = date.getHours();
    let mm = date.getMinutes();
    let ss = date.getSeconds();
    let weekday = days[date.getDay()];
    let day = date.getDate();
    let month = months[date.getMonth()];
    let year = date.getFullYear();
    let setday = "";


    hh = (hh < 10) ? "0" + hh : hh;
    mm = (mm < 10) ? "0" + mm : mm;
    ss = (ss < 10) ? "0" + ss : ss;
    if (hh > 0 && hh <= 11) {
        setday = "Good Morning";
    } else if (hh > 11 && hh <= 18) {
        setday = "Good Afternoon";
    } else {
        setday = "Good Evening";
    }
    let time = hh + ":" + mm + ":" + ss;
    let daytime = weekday + ", " + day + " " + month + " " + year;
    document.getElementById("clock").innerText = time;
    document.getElementById("date").innerText = daytime;
    document.getElementById("app__setday").innerText = setday;
    let t = setTimeout(function() { currentTime() }, 1000);
}
currentTime();