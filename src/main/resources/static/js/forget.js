const questions = {

    anime:
    "Your School Name",

    superhero:
    "Your Favorite Superhero",

    cricketer:
    "Your Favorite Cricketer",

    lucky:
    "Your Lucky Number",

    nickname:
    "Your Childhood Nickname"
};

let currentUser = "";
let currentQuestion = "";

// SEARCH USER
function searchUser(){

    let user =
        document.getElementById("username").value;

    fetch("/get-question?username=" + user)

    .then(res => res.text())

    .then(data => {

        if(data === "NOT_FOUND"){

            alert("User not found");

            return;
        }

        currentUser = user;

        currentQuestion = data;

        document.getElementById(
            "question"
        ).style.display = "block";

        document.getElementById(
            "answer"
        ).style.display = "block";

        document.getElementById(
            "verifyBtn"
        ).style.display = "block";

        document.getElementById(
            "question"
        ).value = questions[data];
    });
}

// VERIFY
function verifyAnswer(){

    let answer =
        document.getElementById("answer").value;

    fetch("/verify-answer", {

        method: "POST",

        headers: {
            "Content-Type":
            "application/json"
        },

        body: JSON.stringify({

            username: currentUser,

            question: currentQuestion,

            answer: answer
        })
    })

    .then(res => res.text())

    .then(data => {

        if(data === "VALID"){

            document.getElementById(
                "resetForm"
            ).style.display = "block";

            document.getElementById(
                "hiddenUsername"
            ).value = currentUser;
        }

        else{

            alert("Wrong answer");
        }
    });
}

// RESET PASSWORD
document
.getElementById("resetForm")

.addEventListener("submit", function(e){

    e.preventDefault();

    let username =
        document.getElementById(
            "hiddenUsername"
        ).value;

    let newPassword =
        document.getElementById(
            "newPassword"
        ).value;

    let confirmPassword =
        document.getElementById(
            "confirmPassword"
        ).value;

    fetch("/reset-password", {

        method: "POST",

        headers: {
            "Content-Type":
            "application/x-www-form-urlencoded"
        },

        body:
            `username=${encodeURIComponent(username)}` +
            `&newPassword=${encodeURIComponent(newPassword)}` +
            `&confirmPassword=${encodeURIComponent(confirmPassword)}`
    })

    .then(res => res.json())

    .then(data => {

        if(data.success){

            alert(
                "Password reset successful"
            );

            window.location.href =
                "/login";
        }

        else if(data.error === "password"){

            alert(
                "Passwords do not match"
            );
        }

        else if(data.error === "user"){

            alert(
                "User not found"
            );
        }

        else{

            alert(
                "Password reset failed"
            );
        }
    });
});