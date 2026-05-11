document
.getElementById("loginForm")

.addEventListener("submit", function(e){

    e.preventDefault();

    let username =
        document.getElementById("username").value;

    let password =
        document.getElementById("password").value;

    fetch(
        `/login?username=${username}&password=${password}`,
        {
            method: "POST"
        }
    )

    .then(res => res.text())

    .then(data => {

        if(data.includes("dashboard")){

            localStorage.setItem(
                "username",
                username
            );

            window.location.href =
                "/dashboard";
        }

        else{

            alert(
                "User does not exist or password is incorrect"
            );
        }
    });
});