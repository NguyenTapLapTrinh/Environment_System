const firebaseConfig = {
    aapiKey: "AIzaSyAZgaGFF92R4g7W_--UvdrItdKZ3l4zy70",
    authDomain: "main-7d188.firebaseapp.com",
    databaseURL: "https://main-7d188-default-rtdb.firebaseio.com/",
    projectId: "main-7d188",
    storageBucket: "main-7d188.appspot.com",
    messagingSenderId: "47792734845",
    appId: "1:47792734845:web:e1b0c21ac2e3cca97ee714",
    measurementId: "G-69SZWELJP5"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
var database = firebase.database();
// Setup
const container = document.querySelector(".app__body-container");
const container_heading = document.querySelector(".app__body-container-heading");
const container_select = document.querySelector(".app__body-select-list");
const app_add = document.querySelector(".add_device");
const input_add = document.querySelector(".modal_body-input");
const input_accept = document.querySelector(".modal__button-accept");
const input_click = document.querySelector(".add_area-click");
const input_cancel = document.querySelector(".modal__button-cancel");
var data_select = '';
var data = '';
// RANDOM NUMBER
function getRandomNumberBetween(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}
var randomID;

// OPEN MODAL ADD AREA
input_click.onclick = function() {
    app_add.style.display = "block";
    input_add.focus();
}

// CLOSE MODAL ADD AREA
input_cancel.onclick = function() {
    app_add.style.display = "none";
}
var x = 0;
database.ref("ThongSo/").on("value", function(snapshot) {
    data_select = '';
    data = '';
    var value = snapshot.val();
    console.log(value);
    const array = Object.keys(value).map((key) => [key, value[key]]);
    console.log(array);
    array.forEach((item, index) => {
            data_select += `<li class="app__body-select-item" title="Area">${item[1].Area}<i class="fa-solid fa-xmark remove_app-select"></i></div>
        </li>`
            container_select.innerHTML = data_select;
            data += `
        <div class="app__body-container-info">
                        <div class="app__body-container-id" title="ID">${item[0]}</div>
                        <div class="grid__row">
                            <div class="app_info-body  app_temp">
                                <div class="app_info-body-heading">
                                    <div class="app_info-body-header">Temperature</div>
                                    <img src="./assects/img/thermometer.png" alt="" class="app_info-body-header-img">
                                </div>
                                <div class="app_info-body-content">
                                    <div class="app_info-body-content-text temp_value" title="Temperature">${item[1].Temp}</div>
                                    <div class="app_info-body-content-period">°C</div>
                                </div>
                                <div class="app_info-body-footer">
                                    <div class="app_info-body-footer-label">Status: </div>
                                    <h4 class="app_info-body-footer-text temp_status"></h4>
                                </div>
                            </div>
                            <div class="app_info-body app_humi">
                                <div class="app_info-body-heading ">
                                    <div class="app_info-body-header">Humidity</div>
                                    <img src="./assects/img/humidity.png" alt="" class="app_info-body-header-img">
                                </div>
                                <div class="app_info-body-content">
                                    <div class="app_info-body-content-text humi_value" title="Humidity">${item[1].Humidity}</div>
                                    <div class="app_info-body-content-period">%</div>
                                </div>
                                <div class="app_info-body-footer">
                                    <div class="app_info-body-footer-label">Status: </div>
                                    <h4 class="app_info-body-footer-text humi_status"></h4>
                                </div>
                            </div>
                            <div class="app_info-body app_heat">
                                <div class="app_info-body-heading">
                                    <div class="app_info-body-header">Heat Index</div>
                                    <img src="./assects/img/fire.png" alt="" class="app_info-body-header-img">
                                </div>
                                <div class="app_info-body-content">
                                    <div class="app_info-body-content-text heat_value" title="Heat Index">${item[1].Heat_index}</div>
                                    <div class="app_info-body-content-period"> w/m2</div>
                                </div>
                                <div class="app_info-body-footer">
                                    <div class="app_info-body-footer-label">Status: </div>
                                    <h4 class="app_info-body-footer-text heat_status">Good</h4>
                                </div>
                            </div>
                            <div class="app_info-body app_air">
                                <div class="app_info-body-heading">
                                    <div class="app_info-body-header">Air Qualitiy</div>
                                    <img src="./assects/img/carbon-dioxide.png" alt="" class="app_info-body-header-img">
                                </div>
                                <div class="app_info-body-content">
                                    <div class="app_info-body-content-text air_value" title="Air Quality">${item[1].Air_Quality}</div>
                                    <div class="app_info-body-content-period">PPM</div>
                                </div>
                                <div class="app_info-body-footer">
                                    <div class="app_info-body-footer-label">Status: </div>
                                    <h4 class="app_info-body-footer-text air_status"></h4>
                                </div>
                            </div>
                        </div>
                    </div>`
            container.innerHTML = data;
            const remove_accept = document.querySelector(".modal__button-accept--remove");
            const remove_cancel = document.querySelector(".modal__button-cancel--remove")
            const remove_ques = document.querySelectorAll(".remove_ques");
            const air_displays = document.querySelectorAll(".app_air");
            const air_value = document.querySelectorAll(".air_value");
            const air_status = document.querySelectorAll(".air_status");
            const humi_displays = document.querySelectorAll(".app_humi");
            const humi_value = document.querySelectorAll(".humi_value");
            const humi_status = document.querySelectorAll(".humi_status");
            const heat_displays = document.querySelectorAll(".app_heat");
            const heat_value = document.querySelectorAll(".heat_value");
            const heat_status = document.querySelectorAll(".heat_status");
            const temp_displays = document.querySelectorAll(".app_temp");
            const temp_value = document.querySelectorAll(".temp_value");
            const temp_status = document.querySelectorAll(".temp_status");
            const remove__btn = document.querySelectorAll(".remove_app-select");
            const close_btn = document.querySelectorAll(".modal__close");
            const id_display = document.querySelectorAll(".app__body-container-id");
            //AIR QUALITIES DISPLAY
            for (let i = 0; i < air_value.length; i++) {
                if (400 <= air_value[i].innerText && air_value[i].innerText < 700) {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #32a54f, #6caf65, #93b880, #b2c29f, #cacdc2)";
                    air_status[i].innerHTML = "Excellent";
                } else if (700 <= air_value[i].innerText && air_value[i].innerText < 900) {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #9bc73c, #a9c962, #b5ca83, #c0cca3, #cacdc2)";
                    air_status[i].innerHTML = "Good";
                } else if (900 <= air_value[i].innerText && air_value[i].innerText < 1100) {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #9bc73c, #a9c962, #b5ca83, #c0cca3, #cacdc2)";
                    air_status[i].innerHTML = "Fair";
                } else if (1100 <= air_value[i].innerText && air_value[i].innerText < 1600) {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #e49a31, #d5ac54, #cabb79, #c5c59e, #cacdc2)";
                    air_status[i].innerHTML = "Mediocre";
                } else if (1600 <= air_value[i].innerText && air_value[i].innerText <= 2100) {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #eb1d27, #da6e26, #c9984d, #c0b786, #cacdc2)";
                    air_status[i].innerHTML = "Bad";
                } else {
                    air_displays[i].style.backgroundImage = "linear-gradient(to bottom, #949f9c, #a0aba5, #adb6af, #bbc2b8, #cacdc2)";
                    air_status[i].innerHTML = "";
                }
            }
            //TEMP DISPLAY
            for (let i = 0; i < temp_value.length; i++) {
                if (0 < temp_value[i].innerText && temp_value[i].innerText < 10) {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #01a0e2, #2db2cc, #73beb6, #a7c5b1, #cacdc2)";
                    temp_status[i].innerHTML = "Cold";
                } else if (10 <= temp_value[i].innerText && temp_value[i].innerText < 20) {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #4dc0af, #7ec3a8, #9fc6a9, #b9c9b2, #cacdc2)";
                    temp_status[i].innerHTML = "Cold";
                } else if (20 <= temp_value[i].innerText && temp_value[i].innerText < 30) {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #d0de63, #cddb7e, #cbd796, #cad2ad, #cacdc2)";
                    temp_status[i].innerHTML = "Normal";
                } else if (30 <= temp_value[i].innerText && temp_value[i].innerText < 40) {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #ffe90c, #eae557, #dadf81, #cfd7a4, #cacdc2)";
                    temp_status[i].innerHTML = "Hot";
                } else if (40 <= temp_value[i].innerText && temp_value[i].innerText < 100) {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #f26a30, #de9041, #cdab66, #c4bf94, #cacdc2);";
                    temp_status[i].innerHTML = "Very Hot";
                } else {
                    temp_displays[i].style.backgroundImage = "linear-gradient(to bottom, #949f9c, #a0aba5, #adb6af, #bbc2b8, #cacdc2)";
                    temp_status[i].innerHTML = "";
                }
            }
            // HUMIDITY DISPLAY
            for (let i = 0; i < humi_value.length; i++) {
                if (0 < humi_value[i].innerText && humi_value[i].innerText < 20) {
                    humi_displays[i].style.backgroundImage = "linear-gradient(to bottom, #f6ce24, #e3d258, #d4d380, #ccd1a3, #cacdc2)";
                    humi_status[i].innerHTML = "Dry";
                } else if (20 <= humi_value[i].innerText && humi_value[i].innerText < 40) {
                    humi_displays[i].style.backgroundImage = "linear-gradient(to bottom, #a5c436, #aec75f, #b8c982, #c1cba3, #cacdc2)";
                    humi_status[i].innerHTML = "Comfortable";
                } else if (40 <= humi_value[i].innerText && humi_value[i].innerText < 60) {
                    humi_displays[i].style.backgroundImage = "linear-gradient(to bottom, #66bb45, #89bf63, #a3c481, #b9c8a1, #cacdc2)";
                    humi_status[i].innerHTML = "Comfortable";
                } else if (60 <= humi_value[i].innerText && humi_value[i].innerText <= 100) {
                    humi_displays[i].style.backgroundImage = "linear-gradient(to bottom, #0079c1, #0096b9, #5caca9, #9dbca9, #cacdc2)";
                    humi_status[i].innerHTML = "Wet";
                } else {
                    humi_displays[i].style.backgroundImage = "linear-gradient(to bottom, #949f9c, #a0aba5, #adb6af, #bbc2b8, #cacdc2)";
                    humi_status[i].innerHTML = "";
                }
            }
            // HEAT INDEX DISPLAY
            for (let i = 0; i < heat_value.length; i++) {
                if (27 <= heat_value[i].innerText && heat_value[i].innerText < 32) {
                    heat_displays[i].style.backgroundImage = "linear-gradient(to bottom, #31adce, #60b8bd, #8cc0b2, #b0c6b3, #cacdc2)";
                    heat_status[i].innerHTML = "Caution";
                } else if (32 <= heat_value[i].innerText && heat_value[i].innerText < 41) {
                    heat_displays[i].style.backgroundImage = "linear-gradient(to bottom, #f7ce10, #e3d252, #d4d37c, #ccd1a1, #cacdc2)";
                    heat_status[i].innerHTML = "Extreme caution";
                } else if (41 <= heat_value[i].innerText && heat_value[i].innerText < 54) {
                    heat_displays[i].style.backgroundImage = "linear-gradient(to bottom, #ff8c00, #e5a738, #d2b966, #c7c695, #cacdc2)";
                    heat_status[i].innerHTML = "Danger";
                } else if (54 <= heat_value[i].innerText) {
                    heat_displays[i].style.backgroundImage = "linear-gradient(to bottom, #ff8c00, #e5a738, #d2b966, #c7c695, #cacdc2)";
                    heat_status[i].innerHTML = "Extreme danger";
                } else {
                    heat_displays[i].style.backgroundImage = "linear-gradient(to bottom, #949f9c, #a0aba5, #adb6af, #bbc2b8, #cacdc2)";
                    heat_status[i].innerHTML = "";
                }
            }
            // REMOVE AREA
            for (let i = 0; i < remove__btn.length; i++) {
                remove__btn[i].onclick = function() {
                    remove_ques[0].style.display = "block";
                    remove_accept.onclick = function() {
                        database.ref(`ThongSo/${id_display[i].innerText}`).remove();
                        location.reload();
                    };
                    remove_cancel.onclick = function() {
                        remove_ques[0].style.display = "none";
                    };
                    close_btn[1].onclick = function() {
                        remove_ques[0].style.display = "none";
                    }
                }
            }
            for (let i = 0; i < close_btn.length; i++) {
                close_btn[0].onclick = function() {
                    app_add.style.display = "none";
                }
            }
        })
        // SELECTION MENU
    const select_ids = document.querySelectorAll(".app__body-select-item");
    const bodys = document.querySelectorAll(".app__body-container-info");
    select_ids.forEach((select_id, index) => {
        const body = bodys[index];
        if (index == x) {
            body.classList.add("active")
            select_id.classList.add("active_select")
        }
        select_id.onclick = function() {
            x = index;
            document.querySelector(".app__body-select-item.active_select").classList.remove("active_select");
            select_id.classList.add("active_select");
            document.querySelector(".app__body-container-info.active").classList.remove('active');
            body.classList.add("active")
        }
    })

    // ADD NEW AREA
    input_accept.onclick = function() {
        if (input_add.value != "") {
            randomID = getRandomNumberBetween(10000, 99999);
            database.ref(`ThongSo/${randomID}/`).update({
                "Area": input_add.value,
                "Temp": 0,
                "Humidity": 0,
                "Air_Quality": 0,
                "Heat_index": 0
            });
            alert(`New area: ${input_add.value} has been add with the ID ${randomID}`);
            app_add.style.display = "none";
            input_add.value = '';
        } else {
            alert("The Input is empty!!!");
        }
    }
});