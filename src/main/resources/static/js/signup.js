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

    fetch("/auth/signup", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            fullName,
            email,
            username,
            password,
            confirmPassword,
            securityQ,
            answer
        })
    })

    .then(res => res.json())

    .then(data => {

        if(data.success){

            alert(
                "Account created successfully"
            );

            window.location.href =
                "/login";
        }

        else if(data.error === "password"){

            alert(
                "Passwords do not match"
            );
        }

        else if(data.error === "username"){

            alert(
                "Username already exists"
            );
        }

        else if(data.error === "email"){

            alert(
                "Email already in use"
            );
        }

        else{

            alert(
                "Signup failed"
            );
        }
    });
});