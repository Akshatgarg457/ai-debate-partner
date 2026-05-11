document
.getElementById("signupForm")

.addEventListener("submit", function(e){

    e.preventDefault();

    let fullName =
        document.getElementById("fullName").value;

    let email =
        document.getElementById("email").value;

    let username =
        document.getElementById("username").value;

    let password =
        document.getElementById("password").value;

    let confirmPassword =
        document.getElementById("confirmPassword").value;

    let securityQ =
        document.getElementById("securityQ").value;

    let answer =
        document.getElementById("answer").value;

    fetch("/signup", {

        method: "POST",

        headers: {
            "Content-Type":
            "application/x-www-form-urlencoded"
        },

        body:
            `fullName=${fullName}` +
            `&email=${email}` +
            `&username=${username}` +
            `&password=${password}` +
            `&confirmPassword=${confirmPassword}` +
            `&securityQ=${securityQ}` +
            `&answer=${answer}`
    })

    .then(res => res.text())

    .then(data => {

        if(data.includes("login")){

            alert(
                "Account created successfully"
            );

            window.location.href =
                "/login";
        }

        else{

            alert(
                "Signup failed"
            );
        }
    });
});