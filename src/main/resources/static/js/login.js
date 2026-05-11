document
.getElementById("loginForm")

.addEventListener("submit", function(e){

    e.preventDefault();

    let username =
        document.getElementById("username").value;

    let password =
        document.getElementById("password").value;

    fetch("/login", {

        method: "POST",

        headers: {
            "Content-Type":
            "application/x-www-form-urlencoded"
        },

        body:
            `username=${encodeURIComponent(username)}` +
            `&password=${encodeURIComponent(password)}`
    })

    .then(res => res.json())

    .then(data => {

        if(data.success){

            localStorage.setItem(
                "username",
                username
            );

            window.location.href =
                "/dashboard";
        }

        else if(data.error === "user"){

            alert(
                "User does not exist"
            );
        }

        else if(data.error === "password"){

            alert(
                "Incorrect password"
            );
        }

        else{

            alert(
                "Login failed"
            );
        }
    });
});